package com.hritik.groupandlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
public class RestartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i("Broadcast Listened", "Service tried to stop");
            //Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, BackgroundServices.class));
            } else {
                context.startService(new Intent(context, BackgroundServices.class));
            }

            //   context.startService(new Intent(context.getApplicationContext(), NotificationService.class));        }

            Log.i("Broadcast Listened", "Service tried to stop");
            //Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, BackgroundServices.class));
            } else {
                context.startService(new Intent(context, BackgroundServices.class));
            }

        }
    }
}