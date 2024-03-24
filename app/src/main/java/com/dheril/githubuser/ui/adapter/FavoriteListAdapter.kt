package com.dheril.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dheril.githubuser.data.local.entity.FavoriteEntity
import com.dheril.githubuser.databinding.ItemUserBinding
import com.dheril.githubuser.ui.fragment.FavoriteUserFragmentDirections

class FavoriteListAdapter : ListAdapter<FavoriteEntity, FavoriteListAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener { view ->
            val toUserDetailFragment =
                FavoriteUserFragmentDirections.actionFavoriteUserFragmentToUserDetailFragment()
            toUserDetailFragment.username = user.username
            view.findNavController().navigate(toUserDetailFragment)
        }
    }

    class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavoriteEntity) {
            binding.tvItemName.text = user.username
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .into(binding.imgItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoriteEntity,
                newItem: FavoriteEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FavoriteEntity,
                newItem: FavoriteEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}