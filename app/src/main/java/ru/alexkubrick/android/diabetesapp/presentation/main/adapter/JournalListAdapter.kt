package ru.alexkubrick.android.diabetesapp.presentation.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.databinding.ListItemJournalBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class JournalListAdapter(
    private val dataList: List<SugarData>,
    private val onDataClicked: (crimeId: UUID) -> Unit,
) : RecyclerView.Adapter<JournalListAdapter.JournalHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = JournalHolder(ListItemJournalBinding.inflate(inflater, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: JournalHolder, position: Int) {
        val dataList = dataList[position]
        holder.bind(dataList, onDataClicked)
    }

    class JournalHolder(private val binding: ListItemJournalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sugarData: SugarData, onDataClicked: (crimeId: UUID) -> Unit) {
            binding.tSugarLevel.text = sugarData.sugarLevel.toString()
            val defaultLocale = Locale.getDefault()
            val dateFormatter = SimpleDateFormat("dd MMMM yyyy", defaultLocale)
            val timeFormatter = SimpleDateFormat("HH:mm", defaultLocale)
            binding.tDate.text = dateFormatter.format(sugarData.date).toString() + " " + timeFormatter.format(sugarData.time).toString()
            binding.tDescription.text = sugarData.desc
            val measurementTime = getMeasurementTimeFromOrdinal(sugarData.measurementTime)
            binding.tMeasurementTime.text = getMeasurementTimeString(measurementTime, binding.root.context)

            binding.root.setOnClickListener {
                onDataClicked(sugarData.id)
            }
        }

        private fun getMeasurementTimeString(measurementTime: MeasurementTime, context: Context): String {
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

