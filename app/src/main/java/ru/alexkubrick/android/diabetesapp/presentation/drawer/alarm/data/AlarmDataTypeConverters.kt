package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data

import androidx.room.TypeConverter
import java.util.Date

class AlarmDataTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}