package com.example.project_android.fragment.teacher;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

import com.example.project_android.R;
import com.example.project_android.activity.CourseViewModel;
import com.example.project_android.entity.Statistics;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ViewUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class TeacherCourseTotal extends Fragment {
    @BindView(R.id.refresh_teacher_total)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.content_not_found_layout)
    LinearLayout notFoundLayout;
    @BindView(R.id.total_layout)
    LinearLayout totalLayout;

    private TeacherCourseTotalViewModel mViewModel;
    private CourseViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_teacher_course_total, container, false);
        ButterKnife.bind(this,inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeacherCourseTotalViewModel.class);
        viewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getStatisticsList().observe(getViewLifecycleOwner(),statistics -> {
            if (statistics.size() < 1){
                notFoundLayout.setVisibility(View.VISIBLE);
                totalLayout.setVisibility(View.GONE);
            } else {
                notFoundLayout.setVisibility(View.GONE);
                totalLayout.setVisibility(View.VISIBLE);
                ViewUtils.setRecycler(getActivity(),R.id.recycler_course_total_list,new TotalAdapter(statistics));
            }
        });
        Integer courseId = viewModel.getCourse().getValue().getCourseId();
        Map<String, String> map = new HashMap<>();
        map.put("courseId",String.valueOf(courseId));
        NetUtil.getNetData("record/findAllStudentRecord",map,new Handler(msg -> {
            if (msg.what == 1){
                mViewModel.updateStatistics(msg.getData().getString("data"));
            }
            Toast.makeText(getActivity(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            return false;
        }));
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshColor());
        refreshLayout.setOnRefreshListener(() -> NetUtil.getNetData("record/findAllStudentRecord",map,new Handler(msg -> {
            if (msg.what == 1){
                mViewModel.updateStatistics(msg.getData().getString("data"));
            }
            Toast.makeText(getActivity(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
            return false;
        })));
    }

    private class TotalAdapter extends RecyclerView.Adapter<TotalAdapter.ViewHolder>{
        private List<Statistics> lists;

        public TotalAdapter(List<Statistics> lists) {
            this.lists = lists;
        }

        @NonNull
        @Override
        public TotalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_course_total_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull TotalAdapter.ViewHolder holder, int position) {
            Statistics statistics = lists.get(position);
            holder.name.setText(statistics.getStudentName());
            holder.account.setText(statistics.getStudentAccount());
            holder.absent.setText(String.valueOf(statistics.getAbsentCount()));
            holder.leave.setText(String.valueOf(statistics.getLeaveCount()));
            holder.failed.setText(String.valueOf(statistics.getFailedCount()));
            holder.success.setText(String.valueOf(statistics.getSuccessCount()));

            if (position % 2 == 0){
                holder.view.setBackgroundColor(Color.WHITE);
            } else {
                holder.view.setBackgroundColor(Color.parseColor("#d8e3e7"));
            }

        }

        @Override
        public int getItemCount() {
            return lists.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name,account,absent,leave,success,failed;
            public View view;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
                name = itemView.findViewById(R.id.total_item_name);
                account = itemView.findViewById(R.id.total_item_account);
                absent = itemView.findViewById(R.id.total_item_absent);
                leave = itemView.findViewById(R.id.total_item_leave);
                success = itemView.findViewById(R.id.total_item_success);
                failed = itemView.findViewById(R.id.total_item_failed);
            }
        }
    }

}