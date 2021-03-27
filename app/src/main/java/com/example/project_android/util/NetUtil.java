package com.example.project_android.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

public class NetUtil {

    public static void getNetData(String url, Map<String, String> data, final Handler handler){
        new Thread(()  -> {
            String result = null;
            try {
                result = Jsoup.connect("http://192.168.1.109:8080/" + url)
                        .data(data)
                        .ignoreContentType(true)
                        .timeout(80000)
                        .get()
                        .body()
                        .text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            JSONObject jsonObject = JSON.parseObject(result);
            Bundle bundle = new Bundle();
            if (jsonObject.getInteger("code") != null && jsonObject.getInteger("code") == 200){
                String arrayStr = jsonObject.getString("data");
                bundle.putString("data",arrayStr);
                message.what = 1;
            } else {
                message.what = 0;
            }
            bundle.putString("message",jsonObject.getString("message"));
            message.setData(bundle);
            handler.sendMessage(message);
        }).start();
    }

}
