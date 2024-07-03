package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.alexkubrick.android.diabetesapp.R

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val REQUEST_CODE_NOTIFICATIONS = 123
    }

    override fun onReceive(context: Context, intent: Intent?) {
        displayNotification(context, intent)
    }

    fun displayNotification(context: Context, intent: Intent?) {
        // Code to display the notification
        val builder = NotificationCompat.Builder(context, "alexKubrick")
            .setSmallIcon(R.drawable.ic_help)
            .setContentTitle("Diabetes Alarm Manager")
            .setContentText("Check the app")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())
    }
}