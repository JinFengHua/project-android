package com.example.project_android.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.platform.comapi.map.OnLongPressListener;
import com.blankj.utilcode.util.ColorUtils;
import com.example.project_android.R;
import com.example.project_android.dialog.RecordModifyDialog;
import com.example.project_android.dialog.ShowFaceDialog;
import com.example.project_android.dialog.ShowImageDialog;
import com.example.project_android.entity.Record;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.ui.companent.CircleImageView;

public class AttendDetailAdapter extends RecyclerView.Adapter<AttendDetailAdapter.ViewHolder> {
    private List<Record> records;
    private Integer type;
    private onResultChangedListener resultChangedListener;

    public void setResultChangedListener(onResultChangedListener resultChangedListener) {
        this.resultChangedListener = resultChangedListener;
    }

    public interface onResultChangedListener{
        void onResultChanged();
    }

    public AttendDetailAdapter(List<Record> records, Integer type) {
        this.records = records;
        this.type = type;
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

        String temp = "";
        switch (record.getRecordResult()){
            case "0":
                temp = "缺 勤";
                holder.result.setTextColor(ColorUtils.getColor(R.color.purple_700));
                break;
            case "1":
                temp = "失 败";
                holder.result.setTextColor(ColorUtils.getColor(R.color.cancel_red));
                break;
            case "2":
                temp = "成 功";
                holder.result.setTextColor(ColorUtils.getColor(R.color.green));
                break;
            case "3":
                temp = "请 假";
                holder.result.setTextColor(ColorUtils.getColor(R.color.soft_blue));
                break;
        }
        holder.result.setText(temp);

        Picasso.with(holder.view.getContext())
                .load(ProjectStatic.SERVICE_PATH + record.getAvatarUrl())
                .fit()
                .error(R.drawable.ic_net_error)
                .into(holder.avatar);

        if (type == 1 && (record.getRecordResult().equals("1") || record.getRecordResult().equals("2"))) {
            holder.view.setOnClickListener(v -> {
                ShowImageDialog imageDialog = new ShowImageDialog(v.getContext());
                imageDialog.setImage(ProjectStatic.SERVICE_PATH + record.getRecordPhoto());
                imageDialog.show();
            });
        }

        holder.view.setLongClickable(true);
        final String initial = temp;
        holder.view.setOnLongClickListener(v -> {
            RecordModifyDialog dialog = new RecordModifyDialog(v.getContext(), initial);
            dialog.setYesClickedListener(() -> {
                String result = getResult(dialog.result);
                Map<String, String> map = new HashMap<>();
                map.put("attendId",record.getAttendId());
                map.put("studentId",record.getStudentId());
                map.put("result",result);
                NetUtil.getNetData("record/modifyRecord",map,new Handler(msg -> {
                    if (msg.what == 1){
                        record.setRecordResult(result);
                        dialog.setOnDismissListener(dialog1 -> resultChangedListener.onResultChanged());
                    } else {
                        Toast.makeText(v.getContext(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    return false;
                }));
            });
            dialog.show();
            return true;
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

    public String getResult(String s){
        switch (s){
            case "成 功":
                return "2";
            case "缺 勤":
                return "0";
            case "请 假":
                return "3";
        }
        return "";
    }
}
