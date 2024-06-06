package ru.alexkubrick.android.diabetesapp.presentation.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alexkubrick.android.diabetesapp.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}