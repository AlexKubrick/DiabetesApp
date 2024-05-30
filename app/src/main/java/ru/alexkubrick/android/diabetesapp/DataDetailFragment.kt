package ru.alexkubrick.android.diabetesapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import ru.alexkubrick.android.diabetesapp.adapter.SugarData
import ru.alexkubrick.android.diabetesapp.databinding.FragmentDataDetailBinding
import java.util.Date

class DataDetailFragment : Fragment() {
    private var _binding: FragmentDataDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: DataDetailFragmentArgs by navArgs()

    private val dataDetailViewModel:  DataDetailViewModel by viewModels {
        DataDetailViewModelFactory(args.dataId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDataDetailBinding.inflate(layoutInflater, container, false)
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

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate =
                bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            dataDetailViewModel.updateData { it.copy(date = newDate) }
        }

        setFragmentResultListener(
            TimePickerFragment.REQUEST_KEY_TIME
        ) { _, bundle ->
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
            // костыль
            if (editSugarLevelText.text.isEmpty()) {
                editSugarLevelText.setText("0")
            }
            if (editSugarLevelText.text.toString().toLong() != sugarData.sugarLevel) {
                editSugarLevelText.setText(sugarData.sugarLevel.toString())
            }
            if(editAdInfoText.text.toString() != sugarData.info) {
                editAdInfoText.setText(sugarData.info)
            }

            buttonPickData.setOnClickListener {
                findNavController().navigate(
                    DataDetailFragmentDirections.selectDate(sugarData.date)
                )
            }
            buttonPickTime.setOnClickListener {
                findNavController().navigate(
                    DataDetailFragmentDirections.selectTime(sugarData.date)
                )
            }
        }
    }

    private fun updateAndSaveData() {
        binding.editSugarLevelText.doOnTextChanged { text, _, _, _ ->
            dataDetailViewModel.updateData { oldData ->
                oldData.copy(sugarLevel = text.toString().toLong())
            }
        }

        binding.editAdInfoText.doOnTextChanged { text, _, _, _ ->
            dataDetailViewModel.updateData { oldData ->
                oldData.copy(info = text.toString())
            }
        }

        binding.buttonApply.setOnClickListener {
            dataDetailViewModel.updateDataInRepository()
        }
    }
}