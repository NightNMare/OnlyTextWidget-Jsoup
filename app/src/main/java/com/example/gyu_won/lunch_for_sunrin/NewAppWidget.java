package com.example.gyu_won.lunch_for_sunrin;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    static String menu;
    private static final String ACTION_BUTTON1 = "com.example.gyu_won.lunch_for_sunrin.Refresh";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        views.setTextViewText(R.id.appwidget_text, menu);


        Intent intentSync = new Intent(context, NewAppWidget.class);
        intentSync.setAction(ACTION_BUTTON1); //You need to specify the action for the intent. Right now that intent is doing nothing for there is no action to be broadcasted.

        PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT); //You need to specify a proper flag for the intent. Or else the intent will become deleted.
        views.setOnClickPendingIntent(R.id.button,pendingSync);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), NewAppWidget.class.getName());
        int[] appWidgets = appWidgetManager.getAppWidgetIds(thisAppWidget);

//        String action = intent.getAction();
        if(intent.getAction().equals(ACTION_BUTTON1)){
            onUpdate(context,appWidgetManager,appWidgets);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.e("1", "1");
        update u = new update(context, appWidgetManager, appWidgetIds);
        u.thread.start();
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
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

        update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
            this.context = context;
            this.appWidgetIds = appWidgetIds;
            this.appWidgetManager = appWidgetManager;
        }

        final String path = "http://www.sunrint.hs.kr/index.do";
        Handler handler = new Handler();

        String menus[];
        String data3;


        Thread thread = new Thread() {
            public void run() {
                menu = "Reloading";
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                }

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
                        menu = data3+"\n\n";
                        for (int i = 0; i < menus.length; i++) {
                            menu = menu + menus[i] + "\n";
                        }
//                        new NewAppWidget().onUpdate(context, appWidgetManager, appWidgetIds);
                        for (int appWidgetId : appWidgetIds) {
                            updateAppWidget(context, appWidgetManager, appWidgetId);
                        }
                    }
                });

            }
        };
    }
}