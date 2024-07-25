package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.databinding.ListItemAlarmBinding
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmData
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.MeasurementTime
import ru.alexkubrick.android.diabetesapp.presentation.utils.SugarUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class AlarmListAdapter(
    private val dataList: List<AlarmData>,
    private val onDataClicked: (alarmId: UUID) -> Unit,
) : RecyclerView.Adapter<AlarmListAdapter.AlarmHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlarmHolder(ListItemAlarmBinding.inflate(inflater, parent, false))
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
            val hours = alarmData.time / (1000 * 60 * 60)
            val minutes = (alarmData.time % (1000 * 60 * 60)) / (1000 * 60)
            val formattedTime = String.format("%02d:%02d", hours, minutes)
            binding.tAlarmTime.text = formattedTime
            binding.tAlarmDate.text = dateFormatter.format(alarmData.date).toString()
            binding.tAlarmDescription.text = alarmData.desc
            val measurementTime = SugarUtils.getMeasurementTimeFromOrdinal(alarmData.measurementTime)
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
    }

    override fun getItemCount() = dataList.size
}