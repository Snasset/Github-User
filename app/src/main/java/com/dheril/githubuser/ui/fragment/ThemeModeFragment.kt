package com.dheril.githubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dheril.githubuser.databinding.FragmentThemeModeBinding
import com.dheril.githubuser.ui.ViewModelFactory
import com.dheril.githubuser.ui.setting.SettingPreferences
import com.dheril.githubuser.ui.setting.dataStore
import com.dheril.githubuser.ui.viewmodel.ThemeModeViewModel

class ThemeModeFragment : Fragment() {

    private var _binding: FragmentThemeModeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireActivity().application.dataStore)
        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity().application, pref)
        val viewModel = ViewModelProvider(this, factory)[ThemeModeViewModel::class.java]

        viewModel.getThemeSetting().observe(viewLifecycleOwner) { isThemeDarkMode: Boolean ->
            if (isThemeDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}