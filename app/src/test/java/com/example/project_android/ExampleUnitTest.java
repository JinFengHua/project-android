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

    private String test = "[{\"courseId\":2,\"teacherId\":1,\"courseName\":\"云计算\",\"courseAvatar\":\"faceimages/face.jpg\",\"courseIntroduce\":\"云计算云计算云计算云计算云计算云计算\",\"courseCode\":\"647904\",\"teacher\":{\"teacherId\":1,\"adminId\":1,\"teacherAccount\":\"000001\",\"teacherPassword\":\"000000\",\"teacherName\":\"张老师\",\"teacherSex\":false,\"teacherPhone\":\"13137749525\",\"teacherEmail\":\"2116161338@qq.com\",\"teacherAvatar\":\"faceimages/face.jpg\",\"courses\":null}},{\"courseId\":4,\"teacherId\":1,\"courseName\":\"数据库\",\"courseAvatar\":\"faceimages/face.jpg\",\"courseIntroduce\":\"云计算云计算云计算云计算云计算云计算\",\"courseCode\":\"322989\",\"teacher\":{\"teacherId\":1,\"adminId\":1,\"teacherAccount\":\"000001\",\"teacherPassword\":\"000000\",\"teacherName\":\"张老师\",\"teacherSex\":false,\"teacherPhone\":\"13137749525\",\"teacherEmail\":\"2116161338@qq.com\",\"teacherAvatar\":\"faceimages/face.jpg\",\"courses\":null}},{\"courseId\":5,\"teacherId\":1,\"courseName\":\"计算机原理\",\"courseAvatar\":\"faceimages/face.jpg\",\"courseIntroduce\":\"云计算云计算云计算云计算云计算云计算\",\"courseCode\":\"353964\",\"teacher\":{\"teacherId\":1,\"adminId\":1,\"teacherAccount\":\"000001\",\"teacherPassword\":\"000000\",\"teacherName\":\"张老师\",\"teacherSex\":false,\"teacherPhone\":\"13137749525\",\"teacherEmail\":\"2116161338@qq.com\",\"teacherAvatar\":\"faceimages/face.jpg\",\"courses\":null}}]";

    private Map<String,String> initMap(){
        Map<String,String> map = new HashMap<>();
        map.put("name","江道宽");
        map.put("account","081417158");
        map.put("sex","true");
        map.put("major","计科一班");
        map.put("password","1234566778");
        map.put("phone","13137749525");
        map.put("email","2116161338@qq.com");
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