package ru.alexkubrick.android.diabetesapp.presentation.dateDetail

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.MeasurementTime

class MeasurementTimeDialogFragment : DialogFragment() {
    var selectedMeasurementTime: MeasurementTime? = null
    private var onMeasurementTimeSelectedListener: ((MeasurementTime) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Measurement Time")

        // Get the list of measurement time options
        val measurementTimeOptions = MeasurementTime.values()

        // Create a single-choice item list
        val selectedIndex = measurementTimeOptions.indexOf(selectedMeasurementTime)
        builder.setSingleChoiceItems(
            measurementTimeOptions.map { it.name }.toTypedArray(),
            selectedIndex
        ) { _, which ->
            selectedMeasurementTime = measurementTimeOptions[which]
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

}