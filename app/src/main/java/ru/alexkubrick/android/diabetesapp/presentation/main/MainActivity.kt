package ru.alexkubrick.android.diabetesapp.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import ru.alexkubrick.android.diabetesapp.presentation.dateDetail.DataDetailFragment
import ru.alexkubrick.android.diabetesapp.R
import ru.alexkubrick.android.diabetesapp.databinding.ActivityMainBinding
import ru.alexkubrick.android.diabetesapp.databinding.FragmentDataDetailBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: JournalListViewModel by viewModels()
//    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open_menu, R.string.apply)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewModel.state.observe(this) { state ->
            when (state) {
                is State.Main -> {
                    val mainFragment = JournalListFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mainFragment)
                        .commit()
                }

                is State.Edit -> {
                    val detailFragment = DataDetailFragment.getInstance(dataId = state.dataId)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
//        binding.navView.setCheckedItem()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
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
//            R.id.
        }
        return true
    }
}
