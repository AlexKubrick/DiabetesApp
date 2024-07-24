package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.MeasurementTime
class MeasurementAlarmTimeDialogFragment : DialogFragment() {
    var selectedMeasurementTime: MeasurementTime? = null
    private var onMeasurementTimeSelectedListener: ((MeasurementTime) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Measurement Time")

        // Get the list of measurement time options from resources
        val measurementTimeOptions = resources.getStringArray(R.array.measurementTime)

        // Create a single-choice item list
        val selectedIndex = getMeasurementTimeIndex(selectedMeasurementTime)
        builder.setSingleChoiceItems(measurementTimeOptions, selectedIndex) { _, which ->
            selectedMeasurementTime = getMeasurementTimeByIndex(which)
        }

        builder.setPositiveButton("OK") { _, _ ->
            selectedMeasurementTime?.let { onMeasurementTimeSelectedListener?.invoke(it)
                dismiss()
            }
        }
        builder.setNegativeButton("Cancel") { _, _ -> dismiss() }

        return builder.create()
    }

    fun setOnMeasurementTimeSelectedListener(listener: (MeasurementTime) -> Unit) {
        onMeasurementTimeSelectedListener = listener
    }

    private fun getMeasurementTimeIndex(measurementTime: MeasurementTime?): Int {
        val measurementTimeOptions = MeasurementTime.values()
        return measurementTimeOptions.indexOfFirst { it == measurementTime }
    }

    private fun getMeasurementTimeByIndex(index: Int): MeasurementTime {
        return MeasurementTime.values()[index]
    }
}