package com.example.project_android.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;

public class ViewUtils {

    public static void setRecycler(View view, int resourceId, RecyclerView.Adapter adapter){
        RecyclerView recyclerView = view.findViewById(resourceId);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public static void setRecycler(Activity view, int resourceId, RecyclerView.Adapter adapter){
        RecyclerView recyclerView = view.findViewById(resourceId);
        LinearLayoutManager manager = new LinearLayoutManager(view.getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }


    /**
     * 加载导航栏
     */
    public static void initActionBar(Activity activity, String titleName){
        TextView title = activity.findViewById(R.id.action_bar_title);
        ImageView back = activity.findViewById(R.id.action_bar_back);

        title.setText(titleName);
        back.setOnClickListener(v -> activity.finish());
    }


    /**
     * alex
     * 隐藏软键盘
     */
    public static void hiddenSoftKey(EditText edt) {
        ((InputMethodManager) MyApplication.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edt.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
