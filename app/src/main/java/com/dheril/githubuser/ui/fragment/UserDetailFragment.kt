package com.dheril.githubuser.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dheril.githubuser.R
import com.dheril.githubuser.data.local.entity.FavoriteEntity
import com.dheril.githubuser.data.remote.response.UserDetailResponse
import com.dheril.githubuser.databinding.FragmentUserDetailBinding
import com.dheril.githubuser.ui.ViewModelFactory
import com.dheril.githubuser.ui.adapter.SectionPagerAdapter
import com.dheril.githubuser.ui.setting.SettingPreferences
import com.dheril.githubuser.ui.setting.dataStore
import com.dheril.githubuser.ui.viewmodel.UserDetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class UserDetailFragment : Fragment() {
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireActivity().application.dataStore)
        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity().application, pref)
        val viewModel = ViewModelProvider(this, factory)[UserDetailViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            view.findNavController().navigateUp()
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_setting -> {
                    val toThemeModeFragment =
                        UserDetailFragmentDirections.actionUserDetailFragmentToThemeModeFragment()
                    view.findNavController().navigate(toThemeModeFragment)
                    true
                }

                else -> false
            }
        }
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val username = UserDetailFragmentArgs.fromBundle(arguments as Bundle).username

        viewModel.getDetail().observe(viewLifecycleOwner) { user ->
            setUserDetail(user)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
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

        viewModel.userDetail.observe(viewLifecycleOwner) { user ->
            val userLogin = user.login
            val avatarUrl = user.avatarUrl

            binding.tvDetailLogin.text = username
            Glide.with(binding.imgDetailPhoto).load(avatarUrl).into(binding.imgDetailPhoto)

            viewModel.getFavoriteUserByUsername(username)
                .observe(viewLifecycleOwner) { favoriteEntity ->
                    var isFavorite = false
                    binding.btnFavorite.setOnClickListener {
                        if (isFavorite) {
                            viewModel.delete(favoriteEntity)
                        } else {
                            val favorite = FavoriteEntity(userLogin, avatarUrl)
                            viewModel.insert(favorite)
                        }
                    }

                    if (favoriteEntity != null) {
                        isFavorite = true
                        binding.btnFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.btnFavorite.context, R.drawable.ic_favorite_full
                            )
                        )
                    } else {
                        binding.btnFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.btnFavorite.context, R.drawable.ic_favorite_border_blue
                            )
                        )
                    }
                }
        }
        viewModel.getDetail().observe(viewLifecycleOwner) { user ->
            binding.btnShare.setOnClickListener {
                val shareText =
                    "Take a look at ${user.url}'s Github profile, Boasting ${user.followers} Followers and ${user.following} Following"
                val intentToShare = Intent(Intent.ACTION_SEND)
                intentToShare.type = "text/plain"
                intentToShare.putExtra("USER", shareText)
                startActivity(Intent.createChooser(intentToShare, "Share with:"))
            }
        }

        if (savedInstanceState == null) {
            viewModel.showUserDetail(username)
        }
        sectionsPagerAdapter.username = username
    }

    private fun setUserDetail(userDetail: UserDetailResponse) {
        binding.tvDetailName.text = if (userDetail.name == null) {
            "-"
        } else {
            userDetail.name
        }
        binding.tvDetailLogin.text = userDetail.login
        binding.tvDetailFollow.text = userDetail.followers.toString()
        binding.tvDetailFollower.text = userDetail.following.toString()
        Glide.with(requireContext()).load(userDetail.avatarUrl).into(binding.imgDetailPhoto)

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
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1, R.string.tab_text_2
        )
    }
}



