package com.example.project_android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;

import static com.blankj.utilcode.util.ColorUtils.getColor;

public class ViewUtils {

    public static void show(Window window){
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(layoutParams);
    }
    /**
     * 创建一个提示框
     */
    public static AlertDialog getLoadingDialog(Context context,String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        return builder.create();
    }

    /**
     * 获取属性圆圈的颜色列
     */
    public static int[] getRefreshColor(){
        int blue = getColor(R.color.blue);
        int red = getColor(R.color.cancel_red);
        int green = getColor(R.color.green);
        return new int[]{blue, red, green};
    }

    /**
     * 绑定recyclerview列表
     */
    public static void setRecycler(View view, int resourceId, RecyclerView.Adapter adapter){
        RecyclerView recyclerView = view.findViewById(resourceId);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public static void setRecycler(Activity view, int viewId, RecyclerView.Adapter adapter){
        RecyclerView recyclerView = view.findViewById(viewId);
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
