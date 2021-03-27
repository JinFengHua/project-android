package com.example.project_android;

import com.example.project_android.util.NetUtil;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> map = initMap();
/*            try {
                String body = Jsoup.connect("http://192.168.1.109:8080/account/addStudent")
                        .data(map)
                        .ignoreContentType(true)
                        .timeout(8000)
                        .get()
                        .body()
                        .text();;
                System.out.println(body);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        getTest("account/addStudent",map);
    }

    public void getTest(String url,Map<String, String> map){
        new Thread(() -> {
            try {
                String body = Jsoup.connect("http://192.168.1.109:8080/account/addStudent")
                        .data(map)
                        .ignoreContentType(true)
                        .timeout(8000)
                        .get()
                        .body()
                        .text();;
                System.out.println(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}