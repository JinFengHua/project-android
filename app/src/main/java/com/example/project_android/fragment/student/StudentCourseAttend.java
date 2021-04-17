package com.example.project_android.fragment.student;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project_android.R;
import com.example.project_android.activity.CourseViewModel;
import com.example.project_android.adapter.AttendListAdapter;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.entity.AttendList;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StudentCourseAttend extends Fragment {

    private StudentCourseAttendViewModel mViewModel;
    private CourseViewModel courseViewModel;

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
        courseViewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getAttendLists().observe(getViewLifecycleOwner(),attendLists -> ViewUtils.setRecycler(getActivity(),R.id.recycler_attend_list_student,new MyAdapter(attendLists)));
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
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle() ;
                    intent.setAction(attendList.getState().equals("进行中") ? ProjectStatic.STUDENT_DO_RECORD :ProjectStatic.STUDENT_RECORD);
                    bundle.putSerializable("attend",attendList);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
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
                dateText = itemView.findViewById(R.id.attend_item_date);
                timeText = itemView.findViewById(R.id.attend_item_time);
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