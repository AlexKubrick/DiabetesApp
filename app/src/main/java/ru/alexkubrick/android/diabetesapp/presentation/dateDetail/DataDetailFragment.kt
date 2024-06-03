package ru.alexkubrick.android.diabetesapp.presentation.dateDetail

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.SugarData
import ru.alexkubrick.android.diabetesapp.databinding.FragmentDataDetailBinding
import java.lang.Exception
import java.util.Date
import java.util.UUID

class DataDetailFragment : Fragment() {
    private var _binding: FragmentDataDetailBinding? = null
    private lateinit var dataId: UUID
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val dataDetailViewModel: DataDetailViewModel by viewModels {
        DataDetailViewModelFactory(dataId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDataDetailBinding.inflate(layoutInflater, container, false)
        arguments?.run {
            dataId = getSerializable(ARG_DATA_ID) as UUID
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateAndSaveData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataDetailViewModel.sugarData.collect { sugarData ->
                    sugarData?.let { updateUi(it) }
                }
            }
        }

        setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) { _, bundle ->
            val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            dataDetailViewModel.updateData { it.copy(date = newDate) }
        }

        setFragmentResultListener(TimePickerFragment.REQUEST_KEY_TIME) { _, bundle ->
            val newDate = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Date
            dataDetailViewModel.updateData { it.copy(date = newDate) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(sugarData: SugarData) {
        binding.apply {
            edSugarLevel.setText(sugarData.sugarLevel.toString())
            edDescription.setText(sugarData.desc)

            bPickData.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(parentFragmentManager, "DatePicker")
            }

            bPickTime.setOnClickListener {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(parentFragmentManager, "TimePicker")
            }

            binding.bDelete.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    dataDetailViewModel.deleteDataById(dataId)
                }
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun updateAndSaveData() {
        binding.bApply.setOnClickListener {
            dataDetailViewModel.updateData { oldData ->
                var sugarLevel: Float = 0.0F
                val description = binding.edDescription.text.toString()
                try {
                    sugarLevel = binding.edSugarLevel.text.toString().toFloat()
                } catch (e: Exception) {

                }
                oldData.copy(
                    desc = description,
                    sugarLevel = sugarLevel
                )
            }
            dataDetailViewModel.updateDataInRepository()
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    companion object {
        private const val ARG_DATA_ID = "dataId"

        fun getInstance(dataId: UUID): DataDetailFragment {
            val fragment = DataDetailFragment()
            val arg = Bundle().apply {
                putSerializable(ARG_DATA_ID, dataId)
            }
            fragment.arguments = arg
            return fragment
        }
    }
}