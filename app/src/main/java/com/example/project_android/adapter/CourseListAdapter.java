package com.example.project_android.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ImageUtils;
import com.example.project_android.R;
import com.example.project_android.entity.CourseList;

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
        holder.lecturer.setText(courseList.getCourseTeacherName());
        holder.name.setText(courseList.getCourseName());
        Bitmap bitmap = ImageUtils.getBitmap(R.mipmap.dog);
        bitmap = ImageUtils.toRoundCorner(bitmap,50f,true);
        holder.img.setImageBitmap(bitmap);
        holder.view.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "你点击了课程" + position, Toast.LENGTH_SHORT).show();
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
