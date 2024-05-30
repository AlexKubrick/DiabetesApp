package ru.alexkubrick.android.diabetesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.alexkubrick.android.diabetesapp.adapter.JournalListAdapter
import ru.alexkubrick.android.diabetesapp.adapter.SugarData
import ru.alexkubrick.android.diabetesapp.databinding.FragmentJournalListBinding
import java.util.Date
import java.util.UUID

class JournalListFragment: Fragment() {
    private val journalListViewModel: JournalListViewModel by viewModels()
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
                journalListViewModel.dataList.collect { dataList ->
                    binding.journalRecyclerView.adapter =
                        JournalListAdapter(dataList) { dataId ->
                            findNavController().navigate(
                                JournalListFragmentDirections.showDataDetailFragment(dataId)
                            )
                        }

                    if (dataList.isEmpty()) {
                        binding.journalRecyclerView.visibility = View.GONE
                        binding.buttonAddData.visibility = View.GONE
                        binding.layoutNoData.visibility = View.VISIBLE
                        binding.buttonAddDataEmptyL.setOnClickListener {
                            showNewSugarData()
                        }
                    } else {
                        binding.journalRecyclerView.visibility = View.VISIBLE
                        binding.layoutNoData.visibility = View.GONE
                        binding.buttonAddData.visibility = View.VISIBLE
                        binding.buttonAddData.setOnClickListener {
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
            sugarLevel = 0,
            date = Date(),
            info = ""
        )
        viewLifecycleOwner.lifecycleScope.launch {
            journalListViewModel.addData(newData)
        }
        findNavController().navigate(
            JournalListFragmentDirections.showDataDetailFragment(newData.id)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}