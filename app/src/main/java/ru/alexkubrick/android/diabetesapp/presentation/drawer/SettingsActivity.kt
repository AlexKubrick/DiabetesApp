package ru.alexkubrick.android.diabetesapp.presentation.drawer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.alexkubrick.android.diabetesapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}