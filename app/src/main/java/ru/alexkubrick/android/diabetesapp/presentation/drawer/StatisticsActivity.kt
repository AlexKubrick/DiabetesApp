package ru.alexkubrick.android.diabetesapp.presentation.drawer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.alexkubrick.android.diabetesapp.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}