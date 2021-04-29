package com.example.project_android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.project_android.R;
import com.example.project_android.util.ViewUtils;

import org.angmarch.views.NiceSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class RecordModifyDialog extends Dialog {
    @BindView(R.id.spinner_record_result)
    public NiceSpinner spinner;

    public String result;
    private onYesClickedListener yesClickedListener;

    public void setYesClickedListener(onYesClickedListener yesClickedListener) {
        this.yesClickedListener = yesClickedListener;
    }

    public RecordModifyDialog(@NonNull Context context, String type) {
        super(context, R.style.Dialog_Msg);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_modify_record, null);
        ButterKnife.bind(this,view);
        setContentView(view);
        this.result = type;
        Integer position =  type.equals("缺 勤") ? 0 : ( type.equals("请 假") ? 1 : 2);
        spinner.setSelectedIndex(position);
    }

    @OnClick({R.id.yes,R.id.no,R.id.spinner_record_result})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.yes:
                result = spinner.getText().toString();
                yesClickedListener.onYesClicked();
                break;
            case R.id.no:
                dismiss();
                break;
        }
    }

    public interface onYesClickedListener{
        void onYesClicked();
    }

    @Override
    public void show() {
        super.show();
        ViewUtils.show(getWindow());
    }
}
