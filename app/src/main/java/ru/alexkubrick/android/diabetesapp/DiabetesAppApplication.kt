package ru.alexkubrick.android.diabetesapp

import android.app.Application

class DiabetesAppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
       SugarDataRepository.initialize(this)
    }
}