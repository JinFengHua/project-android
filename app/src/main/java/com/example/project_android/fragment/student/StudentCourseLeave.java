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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ColorUtils;
import com.example.project_android.R;
import com.example.project_android.activity.CourseViewModel;
import com.example.project_android.dialog.LeaveCreateDialog;
import com.example.project_android.entity.Leave;
import com.example.project_android.fragment.LeaveViewModel;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class StudentCourseLeave extends Fragment {
    @BindView(R.id.refresh_student_leave)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.content_not_found_layout)
    LinearLayout notFoundLayout;

    private LeaveViewModel mViewModel;
    private CourseViewModel viewModel;

    private Integer courseId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course_leave, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LeaveViewModel.class);
        // TODO: Use the ViewModel
        viewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
        mViewModel.getLeaveList().observe(getViewLifecycleOwner(), leaves -> {
            if (leaves.size() < 1){
                notFoundLayout.setVisibility(View.VISIBLE);
                return;
            } else {
                notFoundLayout.setVisibility(View.GONE);
                ViewUtils.setRecycler(getActivity(),R.id.recycler_student_leave_list,new LeaveAdapter(leaves));
            }
        });
        courseId = viewModel.getCourse().getValue().getCourseId();

        Map<String, String> map = new HashMap<>();
        map.put("courseId",String.valueOf(courseId));
        map.put("studentId",getActivity().getSharedPreferences("localRecord", Context.MODE_PRIVATE).getString("id",""));
        NetUtil.getNetData("leave/findAllLeave",map,new Handler(msg -> {
            if (msg.what == 1){
                String data = msg.getData().getString("data");
                mViewModel.updateLeaveList(data);
            }
            Toast.makeText(getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            return false;
        }));

        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshColor());
        refreshLayout.setOnRefreshListener(() -> NetUtil.getNetData("leave/findAllLeave",map,new Handler(msg -> {
            if (msg.what == 1){
                String data = msg.getData().getString("data");
                mViewModel.updateLeaveList(data);
                refreshLayout.setRefreshing(false);
            }
            Toast.makeText(getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            return false;
        })));
    }

    @OnClick(R.id.student_leave_create)
    public void onClicked(View view){
        LeaveCreateDialog dialog = new LeaveCreateDialog(getContext(),courseId);
        dialog.show();
    }

    private class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder>{
        private List<Leave> leaves;

        public LeaveAdapter(List<Leave> leaves) {
            this.leaves = leaves;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_student_leave_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Leave leave = leaves.get(position);
            SimpleDateFormat format = new SimpleDateFormat(ProjectStatic.DATE_FORMAT_DAY, Locale.CHINA);

            holder.start.setText(format.format(leave.getLeaveTime()));
            holder.end.setText(format.format(leave.getBackTime()));
            int i = (int) Math.ceil((double) (leave.getBackTime().getTime() - leave.getLeaveTime().getTime()) / (1000 * 3600 * 24));
            holder.duration.setText(i + "天");

            switch (leave.getApprovalResult()){
                case 0:
                    holder.state.setText("审批中");
                    holder.state.setTextColor(ColorUtils.getColor(R.color.soft_blue));
                    break;
                case 1:
                    holder.state.setText("不批准");
                    holder.state.setTextColor(ColorUtils.getColor(R.color.cancel_red));
                    break;
                case 2:
                    holder.state.setText("批准");
                    holder.state.setTextColor(ColorUtils.getColor(R.color.green));
                    break;
            }

            holder.view.setOnClickListener(v -> {
                Intent intent = new Intent(ProjectStatic.STUDENT_LEAVE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("leave",leave);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return leaves.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView start,end,duration,state;
            public View view;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
                start = itemView.findViewById(R.id.student_leave_item_start);
                end = itemView.findViewById(R.id.student_leave_item_end);
                duration = itemView.findViewById(R.id.student_leave_item_duration);
                state = itemView.findViewById(R.id.student_leave_item_leave_state);
            }
        }
    }

}