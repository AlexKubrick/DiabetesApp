package ru.alexkubrick.android.diabetesapp.presentation.dateDetail

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.SugarData
import ru.alexkubrick.android.diabetesapp.databinding.FragmentDataDetailBinding
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.MeasurementTime
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

            dataDetailViewModel.updateData {
                //bug with date and time
                //it.date.time
                it.copy(date = newDate)
            }
        }

        setFragmentResultListener(TimePickerFragment.REQUEST_KEY_TIME) { _, bundle ->
            val newDate = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Date

            dataDetailViewModel.updateData {
                //bug with date and time
                //it.date.time
                it.copy(date = newDate)
            }
        }

        binding.sugarLevel.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {
                val str = binding.sugarLevel.text.toString()
                if (str.isEmpty()) return
                val str2 = perfectDecimal(str)

                if (str2 != str) {
                    binding.sugarLevel.setText(str2)
                    binding.sugarLevel.setSelection(str2.length)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val defaultLocale = Locale.getDefault()
    private val formattedDate = SimpleDateFormat("dd MMMM yyyy", defaultLocale)
    private val formattedTime = SimpleDateFormat("HH:mm", defaultLocale)

    private fun updateUi(sugarData: SugarData) {
        binding.apply {
            sugarLevel.setText(sugarData.sugarLevel.toString())
            description.setText(sugarData.desc)

            datePicker.transformIntoDatePicker()
            datePicker.setText(formattedDate.format(sugarData.date).toString())

            timePicker.transformIntoTimePicker()
            timePicker.setText(formattedTime.format(sugarData.date.time))

            measuredPicker.transformIntoMeasurementPicker()
            measuredPicker.setText(sugarData.measurementTime.toString())

            binding.btnDelete.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    dataDetailViewModel.deleteDataById(dataId)
                }
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun updateAndSaveData() {
        binding.btnApply.setOnClickListener {
            dataDetailViewModel.updateData { oldData ->
                var sugarLevel = 0.0F
                val description = binding.description.text.toString()
                val measurementTime = binding.measuredPicker.tag as MeasurementTime
                try {
                    sugarLevel = binding.sugarLevel.text.toString().toFloat()
                } catch (e: Exception) {

                }
                oldData.copy(
                    desc = description,
                    sugarLevel = sugarLevel,
                    measurementTime = measurementTime.ordinal
                )
            }
            dataDetailViewModel.updateDataInRepository()
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun perfectDecimal(str: String): String {
        var string = str
        if (string[0] == '.') string = "0$str"
        val max = str.length

        var rFinal = ""
        var after = false
        var up = 0
        var decimal = 0
        for (i in 0 until max) {
            val t = string[i]
            if (t != '.' && !after) {
                up++
                if (up > MAX_BEFORE_POINT) return rFinal
            } else if (t == '.') {
                after = true
            } else {
                decimal++
                if (decimal > MAX_DECIMAL) return rFinal
            }
            rFinal += t
        }
        return rFinal
    }

    private fun EditText.transformIntoDatePicker() {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(parentFragmentManager, null)

        }
    }

    private fun EditText.transformIntoTimePicker() {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(parentFragmentManager, null)
        }
    }

    private fun EditText.transformIntoMeasurementPicker() {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        setOnClickListener {
            val dialog = MeasurementTimeDialogFragment()
            dialog.setOnMeasurementTimeSelectedListener { selectedTime ->
                tag = selectedTime
               setText(selectedTime.name)
            }
            dialog.show(parentFragmentManager, "MeasurementTimeDialog")
        }
    }


    companion object {
        private const val ARG_DATA_ID = "dataId"
        private const val MAX_BEFORE_POINT = 3
        private const val MAX_DECIMAL = 2

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