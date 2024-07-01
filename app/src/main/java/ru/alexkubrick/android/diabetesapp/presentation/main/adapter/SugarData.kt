package ru.alexkubrick.android.diabetesapp.presentation.main.adapter

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class SugarData(
    @PrimaryKey val id: UUID,
    val sugarLevel: Float,
    val date: Date,
    val desc: String,
    val measurementTime: Int
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