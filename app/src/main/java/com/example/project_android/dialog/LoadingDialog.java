package com.example.project_android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project_android.R;

public class LoadingDialog extends Dialog {
    public TextView message;
    public TextView titleText;
    public Button yes;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        message = findViewById(R.id.loading_dialog_message);
        titleText = findViewById(R.id.loading_dialog_title);

        yes = findViewById(R.id.loading_dialog_yes);
        yes.setVisibility(View.INVISIBLE);
        yes.setOnClickListener(v -> dismiss());
    }

    public void setMessage(String message){
        this.message.setText(message);
    }

    public void setTitle(String title){
        this.titleText.setText(title);
    }

    public void setVisibility(int visibility){
        yes.setVisibility(visibility);
    }
}
