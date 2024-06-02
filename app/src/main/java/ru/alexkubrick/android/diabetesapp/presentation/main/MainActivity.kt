package ru.alexkubrick.android.diabetesapp.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.alexkubrick.android.diabetesapp.presentation.dateDetail.DataDetailFragment
import ru.alexkubrick.android.diabetesapp.R

class MainActivity : AppCompatActivity() {
    private val viewModel: JournalListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }

// for future feature
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId){
//            R.id.about -> Toast.makeText(this,"About Selected",Toast.LENGTH_SHORT).show()
//            R.id.settings -> Toast.makeText(this,"Settings Selected",Toast.LENGTH_SHORT).show()
//            R.id.exit -> Toast.makeText(this,"Exit Selected",Toast.LENGTH_SHORT).show()
//        }
//        return super.onOptionsItemSelected(item)
//    }
}
