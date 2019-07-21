package com.dyr.photogallery;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final int POLL_INTERVAL = 1000 * 60 * 15;
    public static final String PREF_IS_ALARM_ON = "isAlarmOn";
    public static final String ACTION_SHOW_NOTIFICATION = "com.dyr.photogallery.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "com.dyr.photogallery.PRIVATE";

    public PollService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        boolean isNetWorkAvailable = cm.getBackgroundDataSetting() &&
                (cm.getActiveNetworkInfo() != null);
        if(!isNetWorkAvailable){
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String query = prefs.getString(FlickerFetchr.PREF_SEARCH_QUERY, null);
        String lastResultId = prefs.getString(FlickerFetchr.PREF_LAST_RESULT_ID, null);

        ArrayList<GalleryItem> items;
        if(query != null){
            items = new FlickerFetchr().search(query);
        }else{
            items = new FlickerFetchr().fetchItems();
        }

        if(items.size() == 0){
            return;
        }

        String resultId = items.get(0).getId();
        if(!resultId.equals(lastResultId)){
            Log.i(TAG,"Got a new result: " + resultId);

            Resources r =getResources();
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, PhotoGalleryActivity.class), 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(r.getString(R.string.new_picture_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(r.getString(R.string.new_picture_title))
                    .setContentText(r.getString(R.string.new_picture_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            showBackgroundNotification(0, notification);
        }else{
            Log.i(TAG,"Got a old result: " + resultId);
        }

        prefs.edit().putString(FlickerFetchr.PREF_LAST_RESULT_ID, resultId).commit();

        Log.i(TAG,"Received an intent: " + intent);
    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i,0);

        AlarmManager alarmManger = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(isOn){
            alarmManger.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
        }else{
            alarmManger.cancel(pi);
            pi.cancel();
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(PollService.PREF_IS_ALARM_ON, isOn)
                .commit();
    }

    public static boolean isServiceAlarmOn(Context context){
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);

        return pi != null;
    }

    void showBackgroundNotification(int requestCode, Notification notification){
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra("REQUEST_CODE", requestCode);
        i.putExtra("NOTIFICATION", notification);

        sendOrderedBroadcast(i, PERM_PRIVATE, null, null,
                Activity.RESULT_OK, null, null);
    }

}
