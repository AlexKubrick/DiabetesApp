package ru.alexkubrick.android.diabetesapp.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import ru.alexkubrick.android.diabetesapp.presentation.dateDetail.DataDetailFragment
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.databinding.ActivityMainBinding
import ru.alexkubrick.android.diabetesapp.presentation.drawer.SettingsActivity

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: JournalListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar.toolbar, R.string.open_menu, R.string.apply)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewModel.state.observe(this) { state ->
            when (state) {
                is State.Main -> replaceFragment(fragment = JournalListFragment(), addToBackStack = false)
                is State.Edit -> replaceFragment(fragment = DataDetailFragment.getInstance(dataId = state.dataId))
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, fragmentName: String? = null, addToBackStack: Boolean = true) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragmentName)
        }
        transaction.commit()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            //R.id.navSettings -> SettingsActivity()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
