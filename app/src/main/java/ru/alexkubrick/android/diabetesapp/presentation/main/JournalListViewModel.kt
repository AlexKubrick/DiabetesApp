package ru.alexkubrick.android.diabetesapp.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alexkubrick.android.diabetesapp.SugarDataRepository
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.SugarData

class JournalListViewModel: ViewModel() {
    private val sugarDataRepository = SugarDataRepository.get()
    private val _dataList: MutableStateFlow<List<SugarData>> = MutableStateFlow(emptyList())

    val state = MutableLiveData<State>(State.Main)
    val dataList: StateFlow<List<SugarData>>
        get() = _dataList.asStateFlow()


    init {
        viewModelScope.launch {
            sugarDataRepository.getDataList().collect {
                _dataList.value = it
            }
        }
    }

    suspend fun addData(sugarData: SugarData) = withContext(Dispatchers.IO) {
        sugarDataRepository.addData(sugarData)
    }

}