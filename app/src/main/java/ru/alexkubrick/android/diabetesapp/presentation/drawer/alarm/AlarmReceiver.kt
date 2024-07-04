package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.alexkubrick.android.diabetesapp.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        displayNotification(context, intent)
    }

    fun displayNotification(context: Context, intent: Intent?) {
        val intent = Intent(context, AlarmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Code to display the notification
        val builder = NotificationCompat.Builder(context, "alexKubrick")
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Diabetes Alarm Manager")
            .setContentText("Check the app")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setContentIntent(pendingIntent)


        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())
    }
}