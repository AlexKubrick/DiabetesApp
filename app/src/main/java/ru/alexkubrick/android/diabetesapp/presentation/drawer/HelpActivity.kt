package ru.alexkubrick.android.diabetesapp.presentation.drawer

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import ru.alexkubrick.android.diabetesapp.databinding.ActivityHelpBinding

class HelpActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webView.loadUrl("file:///android_asset/Diabetes_Eng.html")
        @SuppressLint("SetJavaScriptEnabled")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
    }
}