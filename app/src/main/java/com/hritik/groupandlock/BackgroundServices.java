package com.hritik.groupandlock;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class BackgroundServices extends Service {
    private BroadcastReceiver receiver;
    public int counter=0;
    LockScreen obj = new LockScreen();
    // context is important    // every gui or view or activity have context    // i will use context of NotificationService class
    Context mContext;
    // database class
    MyHelper dpHelper = new MyHelper(this);

    // flag is used for stopping or running loop check of    // current app running
    static  int flag = 0;
    static int start_counter=0;
    static int flag2 = 0;
    // when any app is launch and it have password set on it    // that app name save in current_app varaible
    String current_app = "";
    String prev_app = "";
    public BackgroundServices (){}


    @Override    public void onCreate() {
        super.onCreate();
        // add context of NotificationService to mContext variable
        mContext = this;
        // oreo used different approach for background services        // other use same approach
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

    }

    //  oreo api approach
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        int requestID = (int) System.currentTimeMillis();


        /*
        Intent notificationIntent = new Intent(mContext,LockScreen.class);
        notificationIntent.putExtra("pack","AppInfo");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        //**edit this line to put requestID as requestCode**
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        */

        manager.createNotificationChannel(chan);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .build();

        startForeground(2, notification);
    }

    @Override    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, RestartService.class);
        this.sendBroadcast(broadcastIntent);
    }

    // set timer of one second repeat itself
    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {

                if (flag == 0) {
                    // get data from database
                    Cursor res = dpHelper.getAllData("app_dts");
                    // create two list of name or pass
                    ArrayList<String> name = new ArrayList<String>();
                    while (res.moveToNext()) {
                        // add data to list
                        Log.i("Count", "=========  " + getRecentApps(mContext));
                        //Log.i("Status","========--"+isAppRunning(mContext,printForegroundTask()));
                        name.add(res.getString(2));
                    }
                    // if current app have password set on it                    // lanuch lock screen
                    if (name.contains(getRecentApps(mContext))) {
                        // flag = 1 means stop loop
                        current_app = printForegroundTask();
                        if (start_counter<6){
                            if (start_counter==0){
                                prev_app=current_app;
                                start_counter+=1;
                            }
                            else{
                                if (prev_app.equals(printForegroundTask())){
                                    start_counter+=1;
                                }
                            }
                        }
                        else {
                            flag=1;
                            Intent lockIntent = new Intent(mContext, LockScreen.class);
                            //NAP means Not A Package
                            lockIntent.putExtra("pack", "NAP");
                            //lockIntent.putExtra("pack", printForegroundTask());
                            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            mContext.startActivity(lockIntent);
                        }

                    }
                }
                if (flag==1){
                    if ((!printForegroundTask().equals(current_app))) {
                        flag = 0;
                        start_counter=0;
                    }
                }
                if (printForegroundTask().equals("com.hritik.groupandlock")) {
                    flag = 2;
                }
                if (flag==2){
                    if (!printForegroundTask().equals("com.hritik.groupandlock")) {
                        flag=1;
                        current_app=printForegroundTask();
                    }
                }

                // Log.i("Count", "=========  "+ printForegroundTask());
            }
        };
        timer.schedule(timerTask, 0, 100);
    }
    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    @Nullable    @Override    public IBinder onBind(Intent intent) {
        return null;
    }


    /*public String getRecentActivity(Context context) {
        String topActivityName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            UsageEvents usageEvents = mUsageStatsManager.queryEvents(time - 1000 * 30, System.currentTimeMillis() + (10 * 1000));
            UsageEvents.Event event = new UsageEvents.Event();
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
            }
            if (event != null && !TextUtils.isEmpty(event.getPackageName()) && event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                return event.getClassName();
            } else {
                topActivityName = "";
            }
        } else {
            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            topActivityName = tasks.get(0).processName;
        }
        return topActivityName;
    }
     */


    public String getRecentApps(Context context) {
        String topPackageName = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

            long time = System.currentTimeMillis();

            UsageEvents usageEvents = mUsageStatsManager.queryEvents(time - 1000 * 30, System.currentTimeMillis() + (10 * 1000));
            UsageEvents.Event event = new UsageEvents.Event();
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
            }

            if (event != null && !TextUtils.isEmpty(event.getPackageName()) && event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                if (AndroidUtils.isRecentActivity(event.getClassName())) {
                    return event.getClassName();
                }
                return event.getPackageName();
            } else {
                topPackageName = "";
            }
        } else {
            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            topPackageName = tasks.get(0).processName;
        }


        return topPackageName;
    }

    // get string of current app running
    private String printForegroundTask() {
        String currentApp = "NULL";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        // Log.e("AppLockerService", "Current App in foreground is: " + currentApp)
        return currentApp;
    }
}