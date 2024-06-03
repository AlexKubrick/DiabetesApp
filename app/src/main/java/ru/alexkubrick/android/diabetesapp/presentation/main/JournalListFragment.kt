package ru.alexkubrick.android.diabetesapp.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.JournalListAdapter
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.SugarData
import ru.alexkubrick.android.diabetesapp.databinding.FragmentJournalListBinding
import java.util.Date
import java.util.UUID

class JournalListFragment: Fragment() {
    private val viewModel: JournalListViewModel by activityViewModels()
    private var _binding: FragmentJournalListBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJournalListBinding.inflate(layoutInflater, container, false)
        binding.journalRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataList.collect { dataList ->
                    binding.journalRecyclerView.adapter =
                        JournalListAdapter(dataList) { dataId ->
                            viewModel.state.postValue(State.Edit(dataId))
                        }

                    if (dataList.isEmpty()) {
                        binding.journalRecyclerView.visibility = View.GONE
                        binding.bAddData.visibility = View.GONE
                        binding.layoutNoData.visibility = View.VISIBLE
                        binding.bAddDataEmptyL.setOnClickListener {
                            showNewSugarData()
                        }
                    } else {
                        binding.journalRecyclerView.visibility = View.VISIBLE
                        binding.layoutNoData.visibility = View.GONE
                        binding.bAddData.visibility = View.VISIBLE
                        binding.bAddData.setOnClickListener {
                            showNewSugarData()
                    }
                }
            }
        }
    }
    }

    private fun showNewSugarData() {
        val newData = SugarData(
            id = UUID.randomUUID(),
            sugarLevel = 0.0F,
            date = Date(),
            desc = ""
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addData(newData)
        }
        viewModel.state.postValue(State.Edit(newData.id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}