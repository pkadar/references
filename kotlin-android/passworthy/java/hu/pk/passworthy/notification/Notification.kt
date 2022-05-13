package hu.pk.passworthy.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hu.pk.passworthy.R

class Notification {
    companion object {
        var onStartHappenedOnce = false

        fun sendNotification(ctx: Context, title: String, text: String){
            val builder = NotificationCompat.Builder(ctx)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel("Passworthy Notification", "Passworthy Notification", NotificationManager.IMPORTANCE_DEFAULT)
                val manager = ctx.getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(channel)
                builder.setChannelId("Passworthy Notification")
            }
            val manCompat = NotificationManagerCompat.from(ctx)
            manCompat.notify(1, builder.build())
        }
    }
}