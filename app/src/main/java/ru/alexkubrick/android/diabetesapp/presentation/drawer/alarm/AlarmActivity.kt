package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import ru.alexkubrick.android.diabetesapp.databinding.ActivityAlarmBinding
import java.util.Calendar

class AlarmActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.timePicker.setIs24HourView(true)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, AlarmReceiver::class.java).let {
            intent ->
            intent.putExtra("key", "Hello!")
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        binding.apply {
            bStartExact.setOnClickListener {
                calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmIntent
                )
            }
            bRepeatAlarm.setOnClickListener {
                alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    60 * 1000,
                    alarmIntent
                )
            }
        }
    }
}

