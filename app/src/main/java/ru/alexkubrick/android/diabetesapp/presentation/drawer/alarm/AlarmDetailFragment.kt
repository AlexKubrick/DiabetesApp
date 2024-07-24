package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.databinding.FragmentAlarmDetailBinding
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmData
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.model.AlarmViewModel
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.MeasurementTime
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.presentation.DateAlarmPickerDialog
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.presentation.MeasurementAlarmTimeDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AlarmDetailFragment : Fragment() {
    private var _binding: FragmentAlarmDetailBinding?  = null
    private lateinit var dataId: UUID
    private lateinit var alarmData: AlarmData
    private var newDate: Date? = null

//    private lateinit var alarmManager: AlarmManager
//    private lateinit var alarmIntent: PendingIntent
//    private lateinit var calendar: Calendar

    private val alarmViewModel: AlarmViewModel by activityViewModels()

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
        arguments?.let { args ->
            dataId = args.getSerializable(ARG_DATA_ID) as UUID
        }
        viewLifecycleOwner.lifecycleScope.launch {
            alarmData = alarmViewModel.getData(dataId)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateAndSaveData()

        viewLifecycleOwner.lifecycleScope.launch {
            updateUi(alarmViewModel.getData(dataId))
        }

        setFragmentResultListener(DateAlarmPickerDialog.REQUEST_KEY_DATE) { _, bundle ->
            val newDate = bundle.getSerializable(DateAlarmPickerDialog.BUNDLE_KEY_DATE) as Date
            this.newDate = newDate
            binding.datePicker.setText(formattedDate.format(newDate).toString())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val defaultLocale = Locale.getDefault()
    private val formattedDate = SimpleDateFormat("dd MMMM yyyy", defaultLocale)

    private fun updateUi(alarmData: AlarmData) {
        binding.apply {
            timePickerFeature.setIs24HourView(true)
            val timeInMinutes = alarmData.time.toInt()
            val hour = timeInMinutes / 60
            val minute = timeInMinutes % 60
            timePickerFeature.hour = hour
            timePickerFeature.minute = minute

            measuredPicker.transformIntoMeasurementPicker()
            val measurementTime = resources.getStringArray(R.array.measurementTime)
            measuredPicker.setText(measurementTime[alarmData.measurementTime])

            datePicker.transformIntoDatePicker()
            datePicker.setText(formattedDate.format(alarmData.date).toString())

            binding.btnDelete.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    alarmViewModel.deleteDataById(dataId)
                }
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun updateAndSaveData() {
        binding.btnApply.setOnClickListener {
            val description = binding.description.text.toString()
            val measurementTime = if (binding.measuredPicker.tag is MeasurementTime) {
                binding.measuredPicker.tag as MeasurementTime
            } else {
                MeasurementTime.OTHER
            }
            val newDate = newDate ?: alarmData.date

            val timeInMinutes = (binding.timePickerFeature.hour * 60) + binding.timePickerFeature.minute
            alarmViewModel.updateDataByInstanceInRepository(
                alarmData.copy(
                    time = timeInMinutes.toLong(),
                    date = newDate,
                    desc = description,
                    measurementTime = measurementTime.ordinal
                )
            )
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun EditText.transformIntoDatePicker() {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        setOnClickListener {
            val datePickerDialog = DateAlarmPickerDialog()
            datePickerDialog.show(parentFragmentManager, null)
        }
    }

    private fun EditText.transformIntoMeasurementPicker() {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        setOnClickListener {
            val dialog = MeasurementAlarmTimeDialogFragment()
            dialog.setOnMeasurementTimeSelectedListener { selectedTime ->
                tag = selectedTime
                setText(resources.getStringArray(R.array.measurementTime)[selectedTime.ordinal])
            }
            dialog.show(parentFragmentManager, "MeasurementAlarmTimeDialog")
        }
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



    companion object {
        private const val ARG_DATA_ID = "dataId"

        fun getInstance(dataId: UUID): AlarmDetailFragment {
            val fragment = AlarmDetailFragment()
            val arg = Bundle().apply {
                putSerializable(ARG_DATA_ID, dataId)
            }
            fragment.arguments = arg
            return fragment
        }
    }
}