package com.example.project_android.util;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import cz.msebera.android.httpclient.Header;

public class NetUtil {

    public static void getNetData(String url, Map<String, String> data, final Handler handler){
        Log.d("NET-->URL",ProjectStatic.SERVICE_PATH + url);
        new Thread(()  -> {
            String result = null;
            try {
                result = Jsoup.connect(ProjectStatic.SERVICE_PATH + url)
                        .data(data)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(8000)
                        .get()
                        .body()
                        .text();
                Log.d("NET-->url:",ProjectStatic.SERVICE_PATH + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            if (result == null){
                message.what = 0;
                bundle.putString("message","网络请求超时");
            } else {
                Log.d("NET-->",result);
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getInteger("code") != null && jsonObject.getInteger("code") == 200) {
                    String arrayStr = jsonObject.getString("data");
                    bundle.putString("data", arrayStr);
                    message.what = 1;
                } else {
                    message.what = 0;
                }
                bundle.putString("message", jsonObject.getString("message"));
            }
            message.setData(bundle);
            handler.sendMessage(message);
        }).start();

    }

    public static void getNetData(String url, Map<String, String> data, int timeout, final Handler handler){
        new Thread(()  -> {
            String result = null;
            try {
                result = Jsoup.connect(ProjectStatic.SERVICE_PATH + url)
                        .data(data)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(timeout)
                        .get()
                        .body()
                        .text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            if (result == null){
                message.what = 0;
                bundle.putString("message","网络请求超时");
            } else {
                Log.d("NET-->",result);
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getInteger("code") != null && jsonObject.getInteger("code") == 200) {
                    String arrayStr = jsonObject.getString("data");
                    bundle.putString("data", arrayStr);
                    message.what = 1;
                } else {
                    message.what = 0;
                }
                bundle.putString("message", jsonObject.getString("message"));
            }
            message.setData(bundle);
            handler.sendMessage(message);
        }).start();

    }

}
