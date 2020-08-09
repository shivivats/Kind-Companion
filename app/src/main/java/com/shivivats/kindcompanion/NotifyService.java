package com.shivivats.kindcompanion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotifyService extends Service {

    private static String CHANNEL_ID = "KIND_COMPANION_REMINDERS";
    private static int NOTIFICATION_ID = 1;
    Intent notificationIntent;
    PendingIntent pendingIntent;
    NotificationCompat.Builder builder;

    public NotifyService() {
    }

    @Override
    public void onCreate() {
        notificationIntent = new Intent(getApplicationContext(), LoginActivity.class);
        notificationIntent.putExtra("FROM_NOTIFICATION", true);
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addNextIntentWithParentStack(intent);
        //pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_favorite_24)
                .setContentTitle("Kind Reminder")
                .setContentText("Reminder to look at your notes!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true) // automatically removes the notification when the user taps it
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        // Create the NotificationChannel but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder Notifications";
            String description = "Reminders set by the User.";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register this channel with the system; you cant change the importance
            // or other notification behaviours after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }  //Log.d("NOTIFICATIONS_TAG", "notification manager for channel null");


        }


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());

        }  //Log.d("NOTIFICATIONS_TAG", "notification manager null");


        //Log.d("NOTIFICATIONS_TAG", "notification set");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // To-do: Return the communication channel to the service.


        throw new UnsupportedOperationException("Not yet implemented");
    }
}
