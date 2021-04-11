package com.example.project_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.example.project_android.R;
import com.example.project_android.entity.Record;
import com.example.project_android.util.ProjectStatic;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.smssdk.ui.companent.CircleImageView;

public class AttendDetailAdapter extends RecyclerView.Adapter<AttendDetailAdapter.ViewHolder> {
    private List<Record> records;

    public AttendDetailAdapter(List<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_record_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record = records.get(position);
        holder.name.setText(record.getRecordName());
        holder.account.setText(record.getRecordAccount());

        if (record.getRecordTime() != null){
            holder.time.setText(new SimpleDateFormat("yyyy-MM-dd HH-mm").format(record.getRecordTime()));
            holder.location.setText(record.getRecordLocation());
        } else {
            holder.time.setText("---");
            holder.location.setText("---");
        }
        switch (record.getRecordResult()){
            case "0":
                holder.result.setText("缺 勤");
                holder.result.setTextColor(ColorUtils.getColor(R.color.purple_700));
                break;
            case "1":
                holder.result.setText("签到失败");
                holder.result.setTextColor(ColorUtils.getColor(R.color.cancel_red));
                break;
            case "2":
                holder.result.setText("签到成功");
                holder.result.setTextColor(ColorUtils.getColor(R.color.green));
                break;
            case "3":
                holder.result.setText("请 假");
                holder.result.setTextColor(ColorUtils.getColor(R.color.soft_blue));
                break;
            default:break;
        }

        Picasso.with(holder.view.getContext())
                .load(ProjectStatic.SERVICE_PATH + record.getAvatarUrl())
                .fit()
                .error(R.drawable.ic_net_error)
                .into(holder.avatar);

        holder.view.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "你点击了" + position, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,account,time,location,result;
        public CircleImageView avatar;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.record_item_name);
            account = itemView.findViewById(R.id.record_item_account);
            time = itemView.findViewById(R.id.record_item_time);
            location = itemView.findViewById(R.id.record_item_location);
            result = itemView.findViewById(R.id.record_item_result);
            avatar = itemView.findViewById(R.id.record_item_avatar);
        }
    }
}
