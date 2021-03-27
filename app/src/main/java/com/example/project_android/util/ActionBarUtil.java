package com.example.project_android.util;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_android.R;

public class ActionBarUtil {

    /**
     * 加载导航栏
     */
    public static void initActionBar(Activity activity, String titleName){
        TextView title = activity.findViewById(R.id.action_bar_title);
        ImageView back = activity.findViewById(R.id.action_bar_back);

        title.setText(titleName);
        back.setOnClickListener(v -> activity.finish());
    }
}
