package com.example.project_android.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project_android.R;

public class CreateCourseDialog extends Dialog {
    private TextView yes,no,chooseImage,useDefault;
    private EditText nameEdit,introduceEdit;

    public CreateCourseDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_course_create);

        initView();
    }

    private void initView() {

    }
}
