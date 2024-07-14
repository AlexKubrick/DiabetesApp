package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AlarmData::class], version = 1)
@TypeConverters(AlarmDataTypeConverters::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDataDao(): AlarmDataDao
}