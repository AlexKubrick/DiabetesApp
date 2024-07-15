package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class AlarmDataRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope,
) {

    private val database: AlarmDatabase = Room.databaseBuilder(
        context.applicationContext,
       AlarmDatabase::class.java,
        DATABASE_NAME
    )
        .build()

    fun getDataList(): Flow<List<AlarmData>> = database.alarmDataDao().getDataList()

    suspend fun getData(id: UUID): AlarmData = withContext(Dispatchers.IO) {
        database.alarmDataDao().getData(id)
    }

    fun updateAlarmData(alarmData: AlarmData) {
        coroutineScope.launch {
            database.alarmDataDao().updateData(alarmData)
        }
    }

    suspend fun addData(alarmData: AlarmData) = withContext(Dispatchers.IO) {
        database.alarmDataDao().addData(alarmData)
    }

    suspend fun deleteDataById(id: UUID) = withContext(Dispatchers.IO) {
        database.alarmDataDao().deleteDataById(id)
    }


    companion object {
        private var INSTANCE: AlarmDataRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = AlarmDataRepository(context)
            }
        }

        fun get(): AlarmDataRepository {
            return INSTANCE ?: throw IllegalStateException("AlarmDataRepository must be initialized")
        }

        private const val DATABASE_NAME = "alarm-database"
    }
}