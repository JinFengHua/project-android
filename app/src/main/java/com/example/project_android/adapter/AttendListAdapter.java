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
import com.example.project_android.util.CommenUtil;
import com.example.project_android.util.ProjectStatic;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        String method = attendList.getType() == 0 ? "位置定位" : attendList.getType() == 1 ? "人脸识别" : "手势签到";
        holder.method.setText(method);

        SimpleDateFormat format = new SimpleDateFormat(ProjectStatic.DATE_FORMAT_MINUTE, Locale.CHINA);
        holder.startText.setText(format.format(attendList.getStartTime()));
        long time = attendList.getEndTime().getTime() - attendList.getStartTime().getTime();
        holder.duration.setText(CommenUtil.long2String(time));
        holder.state.setText(attendList.getState());

        holder.view.setOnClickListener(v -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (currentTime.before(attendList.getStartTime())){
                Toast.makeText(v.getContext(), "考勤尚未开始", Toast.LENGTH_SHORT).show();
                return;
            }
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
        TextView method,startText,state,duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            method = itemView.findViewById(R.id.attend_item_method);
            startText = itemView.findViewById(R.id.attend_item_start);
            duration = itemView.findViewById(R.id.attend_item_duration);
            state = itemView.findViewById(R.id.attend_item_current_state);
        }
    }


}
