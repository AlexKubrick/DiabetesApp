package ru.alexkubrick.android.diabetesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alexkubrick.android.diabetesapp.databinding.ListItemJournalBinding
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
            binding.diabetesSugarLevel.text = sugarData.sugarLevel.toString()
            binding.diabetesDate.text = sugarData.date.toString()
            binding.diabetesInfo.text = sugarData.info

            binding.root.setOnClickListener {
                onDataClicked(sugarData.id)
            }
        }
    }

    override fun getItemCount() = dataList.size

}