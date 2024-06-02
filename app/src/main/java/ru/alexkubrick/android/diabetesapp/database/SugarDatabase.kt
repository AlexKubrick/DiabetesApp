package ru.alexkubrick.android.diabetesapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.SugarData

@Database(entities = [SugarData::class], version = 1)
@TypeConverters(SugarDataTypeConverters::class)
abstract class SugarDatabase: RoomDatabase() {
    abstract fun sugarDataDao(): SugarDataDao
}