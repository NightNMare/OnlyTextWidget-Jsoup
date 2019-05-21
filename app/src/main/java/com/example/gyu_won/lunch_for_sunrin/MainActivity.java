package com.example.gyu_won.lunch_for_sunrin;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    final String path = "http://www.sunrint.hs.kr/index.do";
    String menu;
    Handler handler =new Handler();
    String menus[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        new Thread(){
            public void run(){
                try {
                    Document doc = Jsoup.connect(path).get();
                    Log.e("try123","try");
                    Element data = doc.getElementsByClass("menu").get(0);
                    menu = data.text();
                    Log.e("data123",menu);
                }catch(IOException e){
                    Log.e("data123","Error");
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        menus = menu.split(",");
                        menus[menus.length-1] = menus[menus.length-1].split(" ")[0];
                        menu="";
                        tv.setText("");
                        for(int i=0;i<menus.length;i++){
                            menu.concat(menus[i]+"\n");
                            tv.append(menus[i]+"\n");
                        }
                    }
                });

            }
        }.start();

    }

}
