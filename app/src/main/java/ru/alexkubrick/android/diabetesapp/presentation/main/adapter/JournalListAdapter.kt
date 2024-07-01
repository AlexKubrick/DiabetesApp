package ru.alexkubrick.android.diabetesapp.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            binding.tDate.text = dateFormatter.format(sugarData.date).toString() + " " + timeFormatter.format(sugarData.date.time).toString()
            //getString import??
            binding.tDescription.text = sugarData.desc

            binding.root.setOnClickListener {
                onDataClicked(sugarData.id)
            }
        }
    }

    override fun getItemCount() = dataList.size

}