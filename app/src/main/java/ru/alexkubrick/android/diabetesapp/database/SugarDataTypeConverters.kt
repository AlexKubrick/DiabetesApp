package ru.alexkubrick.android.diabetesapp.database

import androidx.room.TypeConverter
import java.util.Date

class SugarDataTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}