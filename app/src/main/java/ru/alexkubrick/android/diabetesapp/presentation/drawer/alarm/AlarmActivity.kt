package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.databinding.ActivityAlarmListBinding
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.adapter.AlarmListAdapter
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmData
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.model.AlarmViewModel
import ru.alexkubrick.android.diabetesapp.presentation.main.State
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.MeasurementTime
import java.util.Date
import java.util.UUID

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmListBinding
    private lateinit var dataId: UUID

    private val viewModel: AlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmListBinding.inflate(layoutInflater)
        binding.alarmRecyclerView.layoutManager = LinearLayoutManager(this)
        setContentView(binding.root)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataList.collect { dataList ->
                    binding.alarmRecyclerView.isVisible = dataList.isNotEmpty()
                    binding.bAlarmAddData.isVisible = dataList.isNotEmpty()
                    binding.layoutNoAlarmData.isVisible = dataList.isEmpty()

                    binding.alarmRecyclerView.adapter =
                        AlarmListAdapter(dataList) { dataId ->
                            viewModel.state.postValue(State.Edit(dataId))
                        }
                }
            }
        }
        binding.bAddAlarmDataEmptyL.setOnClickListener {
            showNewAlarmData()
            openAlarmDetailFragment()
        }
        binding.bAlarmAddData.setOnClickListener {
            showNewAlarmData()
            openAlarmDetailFragment()
        }
    }

    private fun showNewAlarmData() {
        dataId = UUID.randomUUID()
        val newData = AlarmData(
            id = dataId,
            time = Date().time,
            date = Date(),
            desc = "",
            measurementTime = MeasurementTime.OTHER.ordinal,
            enable = false,
            repeat = false
        )

        lifecycleScope.launch {
            viewModel.addData(newData)
        }
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun openAlarmDetailFragment() {
        val alarmDetailFragment = AlarmDetailFragment.getInstance(dataId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, alarmDetailFragment, "alarmDetailFragment + ${UUID.randomUUID()}")
            .addToBackStack(null)
            .commit()
    }
}

