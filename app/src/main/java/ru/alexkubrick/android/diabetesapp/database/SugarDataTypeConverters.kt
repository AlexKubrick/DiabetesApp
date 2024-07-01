package ru.alexkubrick.android.diabetesapp.database

import androidx.room.TypeConverter
import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.MeasurementTime
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