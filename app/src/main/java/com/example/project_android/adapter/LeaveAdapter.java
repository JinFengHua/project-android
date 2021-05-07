package com.example.project_android.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.example.project_android.R;
import com.example.project_android.entity.Leave;
import com.example.project_android.util.MyApplication;
import com.example.project_android.util.ProjectStatic;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.smssdk.ui.companent.CircleImageView;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {
    private List<Leave> leaves;

    public LeaveAdapter(List<Leave> leaves) {
        this.leaves = leaves;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_teacher_member_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Leave leave = leaves.get(position);
        holder.name.setText(leave.getStudentName());
        holder.account.setText(leave.getStudentAccount());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ProjectStatic.DATE_FORMAT_DAY, Locale.CHINA);
        String s = simpleDateFormat.format(leave.getLeaveTime()) + "至" + simpleDateFormat.format(leave.getBackTime());
        holder.time.setText(s);

        switch (leave.getApprovalResult()){
            case 0:
                holder.state.setText("未审批");
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

        holder.leaveLinear.setVisibility(View.VISIBLE);
        holder.state.setVisibility(View.VISIBLE);
        holder.arrow.setVisibility(View.GONE);

        Picasso.with(MyApplication.getContext())
                .load(ProjectStatic.SERVICE_PATH + leave.getStudentAvatar())
                .fit()
                .error(R.drawable.ic_net_error)
                .into(holder.avatar);

        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent("com.example.project_android.activity.teacher.TeacherLeaveActivity");
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
        public CircleImageView avatar;
        public ImageView arrow;
        public TextView name,account,time,state;
        public LinearLayout leaveLinear;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            avatar = itemView.findViewById(R.id.teacher_member_item_avatar);
            account = itemView.findViewById(R.id.teacher_member_item_account);
            name = itemView.findViewById(R.id.teacher_member_item_name);
            time = itemView.findViewById(R.id.member_leave_time);
            leaveLinear = itemView.findViewById(R.id.layout_leave_time);
            state = itemView.findViewById(R.id.member_leave_state);
            arrow = itemView.findViewById(R.id.member_arrow_right);
        }
    }
}
