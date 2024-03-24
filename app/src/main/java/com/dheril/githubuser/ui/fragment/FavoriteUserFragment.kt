package com.dheril.githubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dheril.githubuser.R
import com.dheril.githubuser.data.local.entity.FavoriteEntity
import com.dheril.githubuser.databinding.FragmentFavoriteUserBinding
import com.dheril.githubuser.ui.ViewModelFactory
import com.dheril.githubuser.ui.adapter.FavoriteListAdapter
import com.dheril.githubuser.ui.setting.SettingPreferences
import com.dheril.githubuser.ui.setting.dataStore
import com.dheril.githubuser.ui.viewmodel.FavoriteUserViewModel

class FavoriteUserFragment : Fragment() {

    private var _binding: FragmentFavoriteUserBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteUserBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireActivity().application.dataStore)
        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity().application, pref)
        val viewModel = ViewModelProvider(this, factory)[FavoriteUserViewModel::class.java]

        viewModel.getAllFavorites().observe(viewLifecycleOwner) { user ->
            setFavoriteList(user)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.topAppBar.setNavigationOnClickListener {
            view.findNavController().navigateUp()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_setting -> {
                    val toThemeModeFragment =
                        FavoriteUserFragmentDirections.actionFavoriteUserFragmentToThemeModeFragment()
                    view.findNavController().navigate(toThemeModeFragment)
                    true
                }

                else -> false
            }
        }

    }

    private fun setFavoriteList(favorite: List<FavoriteEntity>) {
        val adapter = FavoriteListAdapter()
        adapter.submitList(favorite)
        binding.rvFavorites.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}