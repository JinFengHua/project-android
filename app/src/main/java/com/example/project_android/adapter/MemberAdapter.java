package com.example.project_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;
import com.example.project_android.entity.Student;
import com.example.project_android.util.MyApplication;
import com.example.project_android.util.ProjectStatic;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.smssdk.ui.companent.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private List<Student> studentList;
    private Integer courseId;

    public MemberAdapter(List<Student> studentList, Integer courseId) {
        this.studentList = studentList;
        this.courseId = courseId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_teacher_member_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.name.setText(student.getStudentName());
        holder.account.setText(student.getStudentAccount());

        Picasso.with(MyApplication.getContext())
                .load(ProjectStatic.SERVICE_PATH + student.getStudentAvatar())
                .fit()
                .error(R.drawable.ic_net_error)
                .into(holder.avatar);

        String userType = MyApplication.getContext().getSharedPreferences("localRecord", Context.MODE_PRIVATE).getString("userType","");
        if (userType.equals("1")) {
            holder.view.setOnClickListener(v -> {
                Intent intent = new Intent(ProjectStatic.MEMBER_DETAIL);
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", student);
                intent.putExtras(bundle);
                intent.putExtra("courseId", courseId);
                v.getContext().startActivity(intent);
            });
        } else {
            holder.arrow.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avatar;
        public TextView name,account;
        public ImageView arrow;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            avatar = itemView.findViewById(R.id.teacher_member_item_avatar);
            account = itemView.findViewById(R.id.teacher_member_item_account);
            name = itemView.findViewById(R.id.teacher_member_item_name);
            arrow = itemView.findViewById(R.id.member_arrow_right);
        }
    }
}
