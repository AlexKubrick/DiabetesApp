package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import ru.alexkubrick.android.diabetesapp.databinding.ActivityAlarmListBinding
import ru.alexkubrick.android.diabetesapp.databinding.FragmentAlarmDetailBinding
import ru.alexkubrick.android.diabetesapp.databinding.FragmentDataDetailBinding
import ru.alexkubrick.android.diabetesapp.presentation.dateDetail.DataDetailFragment
import java.util.Calendar
import java.util.UUID

class AlarmDetailFragment : Fragment() {
    private var _binding: FragmentAlarmDetailBinding?  = null
//    private lateinit var alarmManager: AlarmManager
//    private lateinit var alarmIntent: PendingIntent
//    private lateinit var calendar: Calendar

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAlarmDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
//        //binding.timePicker.setIs24HourView(true)
//
//        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmIntent = Intent(this, AlarmReceiver::class.java).let {
//                intent ->
//            intent.putExtra("key", "Hello!")
//            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//        }
//
////        binding.apply {
////            bStartExact.setOnClickListener {
////                calendar = Calendar.getInstance()
////                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
////                calendar.set(Calendar.MINUTE, timePicker.minute)
////                alarmManager.setExact(
////                    AlarmManager.RTC_WAKEUP,
////                    calendar.timeInMillis,
////                    alarmIntent
////                )
////            }
////            bRepeatAlarm.setOnClickListener {
////                alarmManager.setRepeating(
////                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
////                    SystemClock.elapsedRealtime(),
////                    60 * 1000,
////                    alarmIntent
////                )
////            }
////        }
//    }
}