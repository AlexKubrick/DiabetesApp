package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface AlarmDataDao {
    @Query("SELECT * FROM alarmdata")
    fun getDataList(): Flow<List<AlarmData>>

    @Query("SELECT * FROM alarmdata WHERE id=(:id)")
    fun getData(id: UUID): AlarmData

    @Update
    fun updateData(alarmData: AlarmData)

    @Insert
    fun addData(alarmData: AlarmData)

    @Query("DELETE FROM alarmData WHERE id = :id")
    fun deleteDataById (id: UUID)
}