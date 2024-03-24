package com.dheril.githubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dheril.githubuser.R
import com.dheril.githubuser.data.remote.response.ItemsItem
import com.dheril.githubuser.databinding.FragmentHomeBinding
import com.dheril.githubuser.ui.ViewModelFactory
import com.dheril.githubuser.ui.adapter.UserListAdapter
import com.dheril.githubuser.ui.setting.SettingPreferences
import com.dheril.githubuser.ui.setting.dataStore
import com.dheril.githubuser.ui.viewmodel.MainViewModel
import com.dheril.githubuser.ui.viewmodel.ThemeModeViewModel
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    private val adapter = UserListAdapter()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var query = "snasset"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireActivity().application.dataStore)
        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity().application, pref)
        val themeModeViewModel =
            ViewModelProvider(this, factory)[ThemeModeViewModel::class.java]
        val mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        themeModeViewModel.getThemeSetting()
            .observe(viewLifecycleOwner) { isThemeDarkMode: Boolean ->
                if (isThemeDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

        mainViewModel.userList.observe(viewLifecycleOwner) { users ->
            setUserList(users)
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        mainViewModel.snackbarText.observe(
            requireActivity()
        ) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    requireActivity().window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        findUserList(mainViewModel)


        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_favorite -> {
                    val toFavoriteUserFragment =
                        HomeFragmentDirections.actionHomeFragmentToFavoriteUserFragment()
                    view.findNavController().navigate(toFavoriteUserFragment)
                    true

                }

                R.id.menu_setting -> {
                    val toThemeModeFragment =
                        HomeFragmentDirections.actionHomeFragmentToThemeModeFragment()
                    view.findNavController().navigate(toThemeModeFragment)
                    true
                }

                else -> false
            }
        }

    }

    private fun findUserList(mainViewModel: MainViewModel) {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    query = searchView.text.toString()
                    if (query.isEmpty()) {
                        Snackbar.make(textView, "Field cannot be blank.", Snackbar.LENGTH_SHORT)
                            .show()
                        true
                    } else {
                        mainViewModel.showUserList(query)
                        searchBar.setText(searchView.text)
                        searchView.hide()
                        false
                    }
                }
        }

    }

    private fun setUserList(userList: List<ItemsItem>) {
        adapter.submitList(userList)
        binding.rvUsers.adapter = adapter
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