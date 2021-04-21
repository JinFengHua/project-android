package com.example.project_android.fragment.student;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.example.project_android.R;
import com.example.project_android.activity.CourseViewModel;
import com.example.project_android.adapter.AttendListAdapter;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.entity.AttendList;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("NonConstantResourceId")
public class StudentCourseAttend extends Fragment {
    @BindView(R.id.refresh_student_attend)
    SwipeRefreshLayout refreshLayout;

    private StudentCourseAttendViewModel mViewModel;
    private CourseList course;

    Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course_attend, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentCourseAttendViewModel.class);
        CourseViewModel courseViewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
        CourseList course = courseViewModel.getCourse().getValue();
        // TODO: Use the ViewModel
        mViewModel.getAttendLists().observe(getViewLifecycleOwner(),attendLists -> ViewUtils.setRecycler(getActivity(),R.id.recycler_attend_list_student,new MyAdapter(attendLists)));

        Map<String, String> map = new HashMap<>();
        map.put("courseId",String.valueOf(course.getCourseId()));
        map.put("joinTime",course.getJoinTime().toString());
        NetUtil.getNetData("attend/findStudentAttend",map,new Handler(msg -> {
            if (msg.what == 1) {
                mViewModel.updateAttendList(msg.getData().getString("data"));
            } else {
                Toast.makeText(getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            }
            return false;
        }));
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshColor());
        refreshLayout.setOnRefreshListener(() -> {
            NetUtil.getNetData("attend/findStudentAttend",map,new Handler(msg -> {
                if (msg.what == 1) {
                    mViewModel.updateAttendList(msg.getData().getString("data"));
                } else {
                    Toast.makeText(getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
                return false;
            }));
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private List<AttendList> attendLists;

        public MyAdapter(List<AttendList> attendLists) {
            this.attendLists = attendLists;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_attend_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AttendList attendList = attendLists.get(position);
            holder.dateText.setText(String.valueOf(new Date(attendList.getStartTime().getTime())));
            String time = new Time(attendList.getStartTime().getTime()).toString() + "-" + new Time(attendList.getEndTime().getTime()).toString();
            holder.timeText.setText(time);

            holder.attendMethod.setText("人脸识别");

            holder.state.setText(attendList.getState());

            holder.view.setOnClickListener(v -> {
                LoadingDialog loadingDialog = new LoadingDialog(v.getContext());
                loadingDialog.setTitle("考勤");
                if (attendList.getState().equals("未开始")){
                    loadingDialog.setMessage("当前考勤任务未开始！");
                    loadingDialog.showSingleButton();
                    loadingDialog.show();
                } else {
                    loadingDialog.setMessage(StringUtils.getString(R.string.wait_message));
                    loadingDialog.show();
                    Map<String, String> map = new HashMap<>();
                    map.put("student_id",v.getContext().getSharedPreferences("localRecord", Context.MODE_PRIVATE).getString("id",""));
                    map.put("attend_id",String.valueOf(attendList.getAttendId()));
                    NetUtil.getNetData("record/findRecordByMap",map,new Handler(msg -> {
                        if (msg.what == 1){
                            loadingDialog.dismiss();
                            String data = msg.getData().getString("data");
                            JSONArray array = JSON.parseArray(data);
                            JSONObject jsonObject = array.getJSONObject(0);
                            String recordResult = jsonObject.getString("recordResult");

                            Intent intent = new Intent();
                            Bundle bundle = new Bundle() ;
                            if (attendList.getState().equals("进行中")) {
                                intent.setAction(recordResult.equals("2") ? ProjectStatic.STUDENT_RECORD : ProjectStatic.STUDENT_DO_RECORD);
                            } else {
                                intent.setAction(ProjectStatic.STUDENT_RECORD);
                            }
                            bundle.putSerializable("attend",attendList);
                            bundle.putString("record",jsonObject.toJSONString());
                            intent.putExtras(bundle);
                            v.getContext().startActivity(intent);

                        } else {
                            loadingDialog.setMessage(msg.getData().getString("message"));
                            loadingDialog.showSingleButton();
                        }
                        return false;
                    }));
                }

            });
        }

        @Override
        public int getItemCount() {
            return attendLists.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View view;
            TextView dateText,timeText,attendMethod,state;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
                dateText = itemView.findViewById(R.id.attend_item_start);
                timeText = itemView.findViewById(R.id.attend_item_end);
                attendMethod = itemView.findViewById(R.id.attend_item_method);
                state = itemView.findViewById(R.id.attend_item_current_state);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}