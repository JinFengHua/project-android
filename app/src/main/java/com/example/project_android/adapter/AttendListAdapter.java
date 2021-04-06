package com.example.project_android.adapter;

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
        holder.dateText.setText("日期：" + new Date(attendList.getStartTime().getTime()));
        holder.startTime.setText("开始时间：" + new Time(attendList.getStartTime().getTime()));
        holder.endTime.setText("截止时间：" + new Time(attendList.getEndTime().getTime()));
        holder.attendMethod.setText("人脸识别+GPS");
        holder.state.setText(attendList.getState());

        holder.view.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "你点击了" + position, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return attendLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView dateText,startTime,endTime,attendMethod,state;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            dateText = itemView.findViewById(R.id.attend_item_date);
            startTime = itemView.findViewById(R.id.attend_item_start_time);
            endTime = itemView.findViewById(R.id.attend_item_end_time);
            attendMethod = itemView.findViewById(R.id.attend_item_method);
            state = itemView.findViewById(R.id.attend_item_current_state);
        }
    }
}
