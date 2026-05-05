package com.example.smartfridgelite;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.WorkManager;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationHelper extends Worker {

    private static final String CHANNEL_ID = "expiry_channel";

    public NotificationHelper(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        createNotificationChannel(context);

        AppDatabase db = AppDatabase.getInstance(context);
        long now = System.currentTimeMillis();
        long twoDays = now + (2L * 24 * 60 * 60 * 1000);
        List<Product> expiring = db.productDao().getExpiringSoonSync(now, twoDays);

        if (!expiring.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Product p : expiring) {
                sb.append("• ").append(p.name)
                        .append(" (").append(p.getDaysUntilExpiration()).append(" días)\n");
            }

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("🧊 Productos por vencer")
                            .setContentText(expiring.size() + " productos necesitan atención")
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(sb.toString()))
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1001, builder.build());
        }

        return Result.success();
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alertas de vencimiento",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    public static void scheduleDaily(Context context) {
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                NotificationHelper.class, 1, TimeUnit.DAYS)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "expiry_check",
                ExistingPeriodicWorkPolicy.KEEP,
                request
        );
    }
}
