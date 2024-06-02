package ru.alexkubrick.android.diabetesapp

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
import ru.alexkubrick.android.diabetesapp.adapter.SugarData
import java.util.UUID

class DataDetailViewModel(dataId: UUID): ViewModel() {
    private val sugarDataRepository = SugarDataRepository.get()

    private val _sugarData: MutableStateFlow<SugarData?> = MutableStateFlow(null)
    val sugarData: StateFlow<SugarData?> = _sugarData.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _sugarData.value = sugarDataRepository.getData(dataId)
            }
        }
    }

    fun updateData(onUpdate: (SugarData) -> SugarData) {
        _sugarData.update { oldData ->
            oldData?.let { onUpdate(it) }
        }
    }

    fun updateDataInRepository() {
        sugarData.value?.let { sugarDataRepository.updateSugarData(it) }
    }

    suspend fun deleteDataById(id: UUID) = withContext(Dispatchers.IO) {
        sugarDataRepository.deleteDataById(id)
    }

    suspend fun getOneSugarData(id: UUID) = withContext(Dispatchers.IO) {
        sugarDataRepository.getData(id)
    }

    override fun onCleared() {
        super.onCleared()
        updateDataInRepository()
    }
}

class DataDetailViewModelFactory(
    private val dataId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DataDetailViewModel(dataId) as T
    }
}