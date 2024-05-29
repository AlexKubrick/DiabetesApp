package ru.alexkubrick.android.diabetesapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.alexkubrick.android.diabetesapp.adapter.SugarData
import java.util.UUID

@Dao
interface SugarDataDao {
    @Query("SELECT * FROM sugardata")
    fun getDataList(): Flow<List<SugarData>>

    @Query("SELECT * FROM sugardata WHERE id=(:id)")
    fun getData(id: UUID): SugarData

    @Update
    fun updateData(sugarData: SugarData)

    @Insert
    fun addData(sugarData: SugarData)

    @Delete
    fun deleteData (sugarData: SugarData)

    @Query("DELETE FROM sugardata WHERE id = :id")
    fun deleteDataById (id: UUID)
}