package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.databinding.ListItemAlarmBinding
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmData
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.MeasurementTime
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class AlarmListAdapter(
    private val dataList: List<AlarmData>,
    private val onDataClicked: (alarmId: UUID) -> Unit,
) : RecyclerView.Adapter<AlarmListAdapter.AlarmHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = AlarmHolder(ListItemAlarmBinding.inflate(inflater, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        val dataList = dataList[position]
        holder.bind(dataList, onDataClicked)
    }

    class AlarmHolder(private val binding: ListItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarmData: AlarmData, onDataClicked: (alarmId: UUID) -> Unit) {
            val defaultLocale = Locale.getDefault()
            val dateFormatter = SimpleDateFormat("dd MMMM yyyy", defaultLocale)
            val timeFormatter = SimpleDateFormat("HH:mm", defaultLocale)
            binding.tAlarmTime.text = timeFormatter.format(alarmData.time).toString()
            binding.tAlarmDate.text = dateFormatter.format(alarmData.date).toString()
            binding.tAlarmDescription.text = alarmData.desc
            val measurementTime = getMeasurementTimeFromOrdinal(alarmData.measurementTime)
            binding.tAlarmMeasurementTime.text =
                getMeasurementTimeString(measurementTime, binding.root.context)

            binding.root.setOnClickListener {
                onDataClicked(alarmData.id)
            }
        }

        private fun getMeasurementTimeString(
            measurementTime: MeasurementTime,
            context: Context,
        ): String {
            val measurementTimeArray = context.resources.getStringArray(R.array.measurementTime)
            return measurementTimeArray[measurementTime.ordinal]
        }

        private fun getMeasurementTimeFromOrdinal(ordinal: Int): MeasurementTime {
            return when (ordinal) {
                0 -> MeasurementTime.BEFORE_BREAKFAST
                1 -> MeasurementTime.BREAKFAST
                2 -> MeasurementTime.AFTER_BREAKFAST
                3 -> MeasurementTime.BEFORE_LUNCH
                4 -> MeasurementTime.LUNCH
                5 -> MeasurementTime.AFTER_LUNCH
                6 -> MeasurementTime.BEFORE_DINNER
                7 -> MeasurementTime.DINNER
                8 -> MeasurementTime.AFTER_DINNER
                9 -> MeasurementTime.OTHER
                else -> MeasurementTime.OTHER
            }
        }

    }

    override fun getItemCount() = dataList.size
}