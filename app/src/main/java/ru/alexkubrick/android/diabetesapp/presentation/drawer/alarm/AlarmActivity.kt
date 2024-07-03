package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import ru.alexkubrick.android.diabetesapp.databinding.ActivityAlarmBinding
import java.util.Calendar

class AlarmActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        binding.apply {
            bSelectTime.setOnClickListener {
                showTimePicker()
            }

            bSetAlarm.setOnClickListener {
                checkAndRequestNotificationPermission()
                setAlarm()
            }
            bCancelAlarm.setOnClickListener {
                cancelAlarm()
            }
        }

    }

    private fun checkAndRequestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(POST_NOTIFICATIONS),
                REQUEST_CODE_NOTIFICATIONS
            )
        } else {
            // Permission already granted, proceed with displaying the notification
            val alarmReceiver = AlarmReceiver()
            alarmReceiver.displayNotification(this, null)
        }
    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm was cancelled", Toast.LENGTH_LONG).show()
    }

    private fun setAlarm() {
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1)
        }
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_LONG).show()
    }

    private fun showTimePicker() {
       picker = MaterialTimePicker.Builder()
           .setTimeFormat(TimeFormat.CLOCK_12H)
           .setHour(12)
           .setMinute(0)
           .setTitleText("Select alarm time")
           .build()

        picker.show(supportFragmentManager, "alexKubrickTimeFragment")

        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12) {
                binding.tvSelectedTime.text =
                    String.format("%02d", picker.hour - 12) + ":" +
                            String.format("%02d", picker.minute) + "PM"
            } else {
                String.format("%02d", picker.hour - 12) + ":" +
                        String.format("%02d", picker.minute) + "AM"
            }
            calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, picker.hour)
            calendar.set(Calendar.MINUTE, picker.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "alexKubrickRemainderChannel"
            val description = "Channel for AlarmManager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alexKubrick", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with displaying the notification
                // You can call a method to display the notification here
                val alarmReceiver = AlarmReceiver()
                alarmReceiver.displayNotification(this, null)
            } else {
                // Permission denied, handle accordingly
                // For example, you can show a message to the user or disable the notification feature
                Toast.makeText(this, "Notification feature is disabled", Toast.LENGTH_LONG).show()
            }
        }
    }


    companion object {
        private const val REQUEST_CODE_NOTIFICATIONS = 123
    }
}

