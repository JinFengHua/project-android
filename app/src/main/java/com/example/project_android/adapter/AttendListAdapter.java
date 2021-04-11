package com.example.project_android.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;
import com.example.project_android.entity.AttendList;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class AttendListAdapter extends RecyclerView.Adapter<AttendListAdapter.ViewHolder> {
    private List<AttendList> attendLists;

    public AttendListAdapter(List<AttendList> attendLists) {
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
        holder.timeText.setText(new Time(attendList.getStartTime().getTime()).toString() + "-" + new Time(attendList.getEndTime().getTime()).toString());
        holder.attendMethod.setText("人脸识别+GPS");
        holder.state.setText(attendList.getState());

        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent("com.example.project_android.activity.teacher.TeacherRecordDetail");
            Bundle bundle = new Bundle();
            bundle.putSerializable("attend",attendList);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
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
