package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmData
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmDataRepository
import ru.alexkubrick.android.diabetesapp.presentation.main.State
import java.util.UUID

class AlarmViewModel(dataId: UUID): ViewModel() {
    private val alarmDataRepository = AlarmDataRepository.get()
    private val _dataList: MutableStateFlow<List<AlarmData>> = MutableStateFlow(emptyList())
    private val _alarmData: MutableStateFlow<AlarmData?> = MutableStateFlow(null)
    val alarmData: StateFlow<AlarmData?> = _alarmData.asStateFlow()

    val state = MutableLiveData<State>(State.Main)
    val dataList: StateFlow<List<AlarmData>>
        get() = _dataList.asStateFlow()


    init {
        viewModelScope.launch {
            alarmDataRepository.getDataList().collect {
                _dataList.value = it
            }
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _alarmData.value = alarmDataRepository.getData(dataId)
            }
        }
    }

    suspend fun addData(alarmData: AlarmData) = withContext(Dispatchers.IO) {
        alarmDataRepository.addData(alarmData)
    }

    fun updateData(onUpdate: (AlarmData) -> AlarmData) {
        _alarmData.update { oldData ->
            oldData?.let { onUpdate(it) }
        }
    }

    fun updateDataInRepository() {
        alarmData.value?.let { alarmDataRepository.updateAlarmData(it) }
    }

    suspend fun deleteDataById(id: UUID) = withContext(Dispatchers.IO) {
        alarmDataRepository.deleteDataById(id)
    }

    suspend fun getOneAlarmData(id: UUID) = withContext(Dispatchers.IO) {
        alarmDataRepository.getData(id)
    }

    override fun onCleared() {
        super.onCleared()
        updateDataInRepository()
    }
}

class AlarmViewModelFactory(
    private val dataId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(dataId) as T
    }
}