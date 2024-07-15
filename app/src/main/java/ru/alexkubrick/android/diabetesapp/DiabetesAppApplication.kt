package ru.alexkubrick.android.diabetesapp

import android.app.Application
import ru.alexkubrick.android.diabetesapp.presentation.drawer.alarm.data.AlarmDataRepository

class DiabetesAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SugarDataRepository.initialize(this)
        AlarmDataRepository.initialize(this)
    }
}