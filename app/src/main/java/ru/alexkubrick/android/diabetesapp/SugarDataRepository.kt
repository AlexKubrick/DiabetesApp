package ru.alexkubrick.android.diabetesapp

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alexkubrick.android.diabetesapp.adapter.SugarData
import ru.alexkubrick.android.diabetesapp.database.SugarDataDao
import ru.alexkubrick.android.diabetesapp.database.SugarDatabase
import java.util.UUID

class SugarDataRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope,
) {

    private val database: SugarDatabase = Room.databaseBuilder(
        context.applicationContext,
        SugarDatabase::class.java,
        DATABASE_NAME
    )
//       .createFromAsset(DATABASE_NAME)
        .build()

    fun getDataList(): Flow<List<SugarData>> = database.sugarDataDao().getDataList()

    suspend fun getData(id: UUID): SugarData = withContext(Dispatchers.IO) {
        database.sugarDataDao().getData(id)
    }

    fun updateSugarData(sugarData: SugarData) {
        coroutineScope.launch {
            database.sugarDataDao().updateData(sugarData)
        }
    }

    suspend fun addData(sugarData: SugarData) = withContext(Dispatchers.IO) {
        database.sugarDataDao().addData(sugarData)
    }

    suspend fun deleteData(sugarData: SugarData) = withContext(Dispatchers.IO) {
        database.sugarDataDao().deleteData(sugarData)
    }

    suspend fun deleteDataById(id: UUID) = withContext(Dispatchers.IO) {
        database.sugarDataDao().deleteDataById(id)
    }


    companion object {
        private var INSTANCE: SugarDataRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = SugarDataRepository(context)
            }
        }

        fun get(): SugarDataRepository {
            return INSTANCE ?: throw IllegalStateException("SugarDataRepository must be initialized")
        }

        private const val DATABASE_NAME = "sugar-database"
    }
}