package com.test.android.plugin1.activity.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * des:
 * author: libingyan
 * Date: 18-9-4 19:28
 */
public class NotificationUtils {

    private static final int NOTIFICATION_TEST_ID = 1000;

    public static String ACTION_NOTIFICATION_TEST = "com.android.test.host.demo.action.test_notification";

    public static String NOTIFICATION_EXTRA = "notification_extrat";

    public static void sendNotification(Context context, String title, String content, String extrat) {
        Notification notification = new Notification.Builder(context).
            setContentText(content)
            .setContentTitle(title)
            .setLargeIcon(getAppIcon(context))
            //.setSmallIcon(Icon.createWithBitmap(getAppIcon(context)))
            .build();

        notification.tickerText = title;
        notification.when = System.currentTimeMillis();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.contentIntent = getNotificationIntent(context, extrat);

        try {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(NOTIFICATION_TEST_ID, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PendingIntent getNotificationIntent(Context context, String extrat) {
        Intent intent = new Intent(ACTION_NOTIFICATION_TEST);
        intent.putExtra(NOTIFICATION_EXTRA, extrat);

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap getAppIcon(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return ((BitmapDrawable) info.loadIcon(pm)).getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
