package com.dheril.githubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dheril.githubuser.data.remote.response.ItemsItem
import com.dheril.githubuser.databinding.FragmentFollowBinding
import com.dheril.githubuser.ui.ViewModelFactory
import com.dheril.githubuser.ui.adapter.FollowListAdapter
import com.dheril.githubuser.ui.setting.SettingPreferences
import com.dheril.githubuser.ui.setting.dataStore
import com.dheril.githubuser.ui.viewmodel.FollowViewModel
import com.google.android.material.snackbar.Snackbar

class FollowFragment : Fragment() {

    private val adapter = FollowListAdapter()
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.layoutManager = layoutManager

        val pref = SettingPreferences.getInstance(requireActivity().application.dataStore)
        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity().application, pref)
        val viewModel = ViewModelProvider(this, factory)[FollowViewModel::class.java]

        viewModel.userFollow.observe(viewLifecycleOwner) { follow ->
            setFollowList(follow)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.snackbarText.observe(
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

        if (savedInstanceState == null) {
            viewModel.showUserFollow(username!!, index!!)
        }
    }

    private fun setFollowList(followList: List<ItemsItem>) {

        adapter.submitList(followList)
        binding.rvFollow.adapter = adapter
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


    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "username"
    }
}