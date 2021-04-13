package com.example.project_android.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;
import com.example.project_android.entity.Leave;
import com.example.project_android.util.MyApplication;
import com.example.project_android.util.ProjectStatic;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        public TextView name,account;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            avatar = itemView.findViewById(R.id.teacher_member_item_avatar);
            account = itemView.findViewById(R.id.teacher_member_item_account);
            name = itemView.findViewById(R.id.teacher_member_item_name);
        }
    }
}
