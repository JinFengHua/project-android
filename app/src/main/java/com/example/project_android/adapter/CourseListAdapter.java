 package com.example.project_android.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.MyApplication;
import com.example.project_android.util.MyTransForm;
import com.example.project_android.util.ProjectStatic;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {
    private List<CourseList> courseLists;

    public CourseListAdapter(List<CourseList> courseLists) {
        this.courseLists = courseLists;
    }

    @NonNull
    @Override
    public CourseListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_course_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListAdapter.ViewHolder holder, int position) {
        CourseList courseList = courseLists.get(position);
        holder.lecturer.setText(courseList.getUesrName());
        holder.name.setText(courseList.getCourseName());

        Picasso.with(MyApplication.getContext())
                .load(ProjectStatic.SERVICE_PATH + courseList.getCourseAvatar())
                .fit()
                .transform(new MyTransForm.RoundCornerTransForm(30f))
                .error(R.drawable.ic_net_error)
                .into(holder.img);

        holder.view.setOnClickListener(v -> {
            SharedPreferences localRecord = v.getContext().getSharedPreferences("localRecord", Context.MODE_PRIVATE);
            String userType = localRecord.getString("userType", "");
            Intent intent = new Intent();
            if (userType.equals("2")){
                intent.setAction(ProjectStatic.STUDENT_COURSE_DETAIL);
            }else {
                intent.setAction(ProjectStatic.TEACHER_COURSE_DETAIL);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("course",courseList);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView img;
        public TextView name;
        public TextView lecturer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            img = itemView.findViewById(R.id.course_list_avatar);
            name = itemView.findViewById(R.id.course_list_course_name);
            lecturer = itemView.findViewById(R.id.course_list_teacher_name);
        }
    }
}
