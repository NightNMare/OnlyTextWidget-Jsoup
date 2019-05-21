package com.example.gyu_won.lunch_for_sunrin;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    static int i=0;
    static String menu;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        Log.e("4","4");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        views.setTextViewText(R.id.appwidget_text, menu);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.e("1","1");
        if(i ==0){
            update u = new update(context, appWidgetManager, appWidgetIds);
            u.thread.start();
            Log.e("2", "2");
            i++;
        }
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private static class update {
        Context context;
        AppWidgetManager appWidgetManager;
        int[] appWidgetIds;
        update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
            this.context = context;
            this.appWidgetIds = appWidgetIds;
            this.appWidgetManager = appWidgetManager;
        }
        final String path = "http://www.sunrint.hs.kr/index.do";
        Handler handler = new Handler();

        String menus[];

        Thread thread = new Thread() {
            public void run() {
                try {
                    Document doc = Jsoup.connect(path).get();
                    Log.e("try", "try");
                    Element data = doc.getElementsByClass("menu").get(0);
                    menu = data.text();
                    Log.e("data", menu);
                } catch (IOException e) {
                    Log.e("data", "Error");
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("3","3");

                        menus = menu.split(",");
                        Log.e("menu===",menus[0]);
                        menus[menus.length - 1] = menus[menus.length - 1].split(" ")[0];
                        Log.e("혹시","안돌아가니?");
                        Log.e("menu=", String.valueOf(menus.length));
                        menu = "";
                        for (int i = 0; i < menus.length; i++) {
                            menu= menu + menus[i] + "\n";
                            Log.e("menu=", String.valueOf(i));
                        }
                        new NewAppWidget().onUpdate(context,appWidgetManager,appWidgetIds);
                    }
                });

            }
        };
    }
}

