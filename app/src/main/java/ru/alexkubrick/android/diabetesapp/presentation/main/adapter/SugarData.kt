package ru.alexkubrick.android.diabetesapp.presentation.main.adapter

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class SugarData(
    @PrimaryKey val id: UUID,
    val sugarLevel: Long,
    val date: Date,
    val info: String
)