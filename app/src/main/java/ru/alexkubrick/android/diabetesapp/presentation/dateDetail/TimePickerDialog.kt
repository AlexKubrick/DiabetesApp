package ru.alexkubrick.android.diabetesapp.presentation.dateDetail

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.Calendar
import java.util.GregorianCalendar

class TimePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val timeListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
                val resultDate = GregorianCalendar(initialYear, initialMonth, initialDay, hour, minute).time

                setFragmentResult(
                    REQUEST_KEY_TIME,
                    bundleOf(BUNDLE_KEY_TIME to resultDate)
                )
            }


        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            DateFormat.is24HourFormat(activity)
        )
    }

    companion object {
        const val REQUEST_KEY_TIME = "REQUEST_KEY_TIME"
        const val BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME"
    }
}