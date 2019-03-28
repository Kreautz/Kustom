package com.example.kustom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class wedege extends AppWidgetProvider {
    private static final String SHARED_PREF_FILE = "com.example.kustom";
    // private static final String COUNT_KEY = "count";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_FILE, 0);
        // int count = prefs.getInt(COUNT_KEY + appWidgetId, 0);
        // count++;

        String timeString   = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        String dateString   = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date());
        String yearString  = DateFormat.getDateInstance(DateFormat.YEAR_FIELD).format(new Date());
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wdg);
        views.setTextViewText(R.id.appwidget_date, dateString);
        views.setTextViewText(R.id.appwidget_time, timeString);

        SharedPreferences.Editor prefEditor = prefs.edit();
        // prefEditor.putInt(COUNT_KEY + appWidgetId, count);
        // prefEditor.apply();

        // Event on click update
        Intent intentUpdate = new Intent(context, wedege.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context,
                appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

        // open activity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.section_update, pendingIntent);

        // Auto update
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE)+1);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC,calendar.getTimeInMillis()
                , 60*1000, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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
}

