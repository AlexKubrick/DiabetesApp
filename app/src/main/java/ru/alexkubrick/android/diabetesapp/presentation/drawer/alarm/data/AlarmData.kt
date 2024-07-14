package ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class AlarmData(
    @PrimaryKey val id: UUID,
    val time: Long,
    val date: Date,
    val desc: String,
    val measurementTime: Int,
    val repeat: Boolean,
    val enable: Boolean
)

enum class MeasurementTime {
    BEFORE_BREAKFAST,
    BREAKFAST,
    AFTER_BREAKFAST,
    BEFORE_LUNCH,
    LUNCH,
    AFTER_LUNCH,
    BEFORE_DINNER,
    DINNER,
    AFTER_DINNER,
    OTHER
}