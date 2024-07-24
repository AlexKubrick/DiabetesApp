package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmData
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmDataRepository
import ru.alexkubrick.android.diabetesapp.presentation.main.State
import java.util.UUID

class AlarmViewModel: ViewModel() {
    private val alarmDataRepository = AlarmDataRepository.get()
    private val _dataList: MutableStateFlow<List<AlarmData>> = MutableStateFlow(emptyList())

    val state = MutableLiveData<State>(State.Main)
    val dataList: StateFlow<List<AlarmData>>
        get() = _dataList.asStateFlow()


    init {
        viewModelScope.launch {
            alarmDataRepository.getDataList().collect {
                _dataList.value = it
            }
        }
    }

    suspend fun addData(alarmData: AlarmData) = withContext(Dispatchers.IO) {
        alarmDataRepository.addData(alarmData)
    }

    suspend fun getData(dataId: UUID) = withContext(Dispatchers.IO) {
        alarmDataRepository.getData(dataId)
    }

    fun updateDataByInstanceInRepository(alarmData: AlarmData) {
        alarmDataRepository.updateAlarmData(alarmData)
    }

    suspend fun deleteDataById(id: UUID) = withContext(Dispatchers.IO) {
        alarmDataRepository.deleteDataById(id)
    }

    suspend fun getOneAlarmData(id: UUID) = withContext(Dispatchers.IO) {
        alarmDataRepository.getData(id)
    }
}