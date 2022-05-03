package com.example.project_android;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ArrayUtils;
import com.example.project_android.util.CommenUtil;
import com.example.project_android.util.NetUtil;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    private String test = "[]";
    private Map<String,String> initMap(){
        Map<String,String> map = new HashMap<>();
       
        return map;
    }

    @Test
    public void netTest(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(i);
        }
        String s = CommenUtil.list2String(list);
        System.out.println(s);
        List<Character> list1 = Arrays.asList(Objects.requireNonNull(ArrayUtils.toObject(s.toCharArray())));
        for (Character character : list1) {
            System.out.println(Integer.valueOf(character.toString()));
        }
    }

    @Test
    public void getTest() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
    }
}
