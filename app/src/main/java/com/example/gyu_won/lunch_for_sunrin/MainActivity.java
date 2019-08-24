package com.example.gyu_won.lunch_for_sunrin;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    final String path = "http://www.sunrint.hs.kr/index.do";
    String menu;
    Handler handler = new Handler();
    String menus[];
    String data3;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("Reloading");
                GetData();
            }
        });

        GetData();
    }

    void GetData() {


        new Thread() {
            public void run() {
                try {
                    Document doc = Jsoup.connect(path).get();
                    Element data = doc.getElementsByClass("menu").get(0);
                    Element data1 = data.previousElementSibling();
                    menu = data.text();
                    data3 = data1.text();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        menus = menu.split(",");
                        menus[menus.length - 1] = menus[menus.length - 1].split(" ")[0];
                        menu = "";
                        menu = data3 + "\n\n";
                        tv.setText(menu);
                        for (int i = 0; i < menus.length; i++) {
                            menu.concat(menus[i] + "\n");
                            tv.append(menus[i] + "\n");
                        }
                        menu = tv.getText().toString();
                    }
                });
            }
        }.start();
    }


}