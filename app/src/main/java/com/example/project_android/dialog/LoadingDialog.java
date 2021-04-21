package com.example.project_android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.example.project_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 在进行耗时操作时显示出来与用户进行交互
 */
@SuppressLint("NonConstantResourceId")
public class LoadingDialog extends Dialog {
    protected TextView message;
    protected TextView titleText;
    protected Button yes;
    public Button no;

    public interface OnYesClickedListener{
        void onYesClicked(View view);
    }

    public void setOnYesClickedListener(OnYesClickedListener onYesClickedListener) {
        if (onYesClickedListener != null){
            yes.setOnClickListener(onYesClickedListener::onYesClicked);
            no.setVisibility(View.VISIBLE);
            yes.setVisibility(View.VISIBLE);
        }
    }

    public LoadingDialog(@NonNull Context context) {
        super(context,R.style.Dialog_Msg);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        message = inflate.findViewById(R.id.loading_dialog_message);
        message.setText(StringUtils.getString(R.string.wait_message));
        titleText = inflate.findViewById(R.id.loading_dialog_title);
        yes = inflate.findViewById(R.id.loading_dialog_yes);
        no = inflate.findViewById(R.id.loading_dialog_no);

        no.setOnClickListener(v -> dismiss());
        setContentView(inflate);
    }

    public void setMessage(String m){
        message.setText(m);
    }

    public void setTitle(String title){
        titleText.setText(title);
    }

    public void showSingleButton(){
        yes.setOnClickListener(v -> dismiss());
        yes.setVisibility(View.VISIBLE);
        no.setVisibility(View.GONE);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
