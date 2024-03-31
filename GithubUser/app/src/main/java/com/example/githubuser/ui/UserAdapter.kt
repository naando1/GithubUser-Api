package com.example.githubuser.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val list = ArrayList<ItemsItem>()
    private var onItemClickCallback: OnItemClickCallback? = null


    fun setList(newList: List<ItemsItem>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffCallback(list, newList))
        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getList(): List<ItemsItem> {
        return list
    }

    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onClick(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(itemPhoto)
                itemName.text = user.login
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onClick(data: ItemsItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class UserDiffCallback(private val oldList: List<ItemsItem>, private val newList: List<ItemsItem>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}