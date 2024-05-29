package ru.alexkubrick.android.diabetesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val fragment = JournalListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment, JOURNAL_FRAGMENT_TAG)
                .commit()
        }
    }

    companion object {
        private const val JOURNAL_FRAGMENT_TAG = "JournalFragment"
    }
}
