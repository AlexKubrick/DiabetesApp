package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.databinding.ActivityAlarmListBinding
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.adapter.AlarmListAdapter
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmData
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.model.AlarmViewModel
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.model.AlarmViewModelFactory
import ru.alexkubrick.android.diabetesapp.presentation.main.State
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.MeasurementTime
import java.util.Date
import java.util.UUID

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmListBinding
    private lateinit var dataId: UUID

    private val viewModel: AlarmViewModel by viewModels {
        AlarmViewModelFactory(dataId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmListBinding.inflate(layoutInflater)
        binding.alarmRecyclerView.layoutManager = LinearLayoutManager(this)
        setContentView(binding.root)

        dataId = UUID.randomUUID()

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
        val newData = AlarmData(
            id = UUID.randomUUID(),
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
        super.onBackPressed()
        binding.fragmentContainer.isVisible = false
    }

    private fun openAlarmDetailFragment() {
        val alarmDetailFragment = AlarmDetailFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, alarmDetailFragment, "alarmDetailFragment")
            .addToBackStack(null)
            .commit()
        binding.fragmentContainer.isVisible = true
    }
}

