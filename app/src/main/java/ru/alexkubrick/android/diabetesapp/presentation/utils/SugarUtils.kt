package ru.alexkubrick.android.diabetesapp.presentation.utils

import ru.alexkubrick.android.diabetesapp.presentation.main.adapter.MeasurementTime

object SugarUtils {

    fun getMeasurementTimeFromOrdinal(ordinal: Int): MeasurementTime {
        return when (ordinal) {
            0 -> MeasurementTime.BEFORE_BREAKFAST
            1 -> MeasurementTime.BREAKFAST
            2 -> MeasurementTime.AFTER_BREAKFAST
            3 -> MeasurementTime.BEFORE_LUNCH
            4 -> MeasurementTime.LUNCH
            5 -> MeasurementTime.AFTER_LUNCH
            6 -> MeasurementTime.BEFORE_DINNER
            7 -> MeasurementTime.DINNER
            8 -> MeasurementTime.AFTER_DINNER
            9 -> MeasurementTime.OTHER
            else -> MeasurementTime.OTHER
        }
    }
}