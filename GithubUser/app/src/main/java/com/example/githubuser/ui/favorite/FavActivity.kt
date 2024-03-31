package com.example.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.dataFav.Favorite
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.ActivityFavBinding
import com.example.githubuser.ui.UserAdapter
import com.example.githubuser.ui.detailuser.UserDetailActivity
import com.example.githubuser.ui.settings.SettingsActivity

class FavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: FavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu -> {
                    val intent = Intent(this, FavActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        userAdapter = UserAdapter()

        viewModel = ViewModelProvider(this).get(FavViewModel::class.java)

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onClick(data: ItemsItem) {
                Intent(this@FavActivity, UserDetailActivity::class.java).also { intent ->
                    intent.putExtra(UserDetailActivity.EXTRA_USERNAME, data.login)
                    intent.putExtra(UserDetailActivity.EXTRA_ID, data.id)
                    intent.putExtra(UserDetailActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(intent)
                }
            }


        })

        binding.apply {
            rvFav.adapter = userAdapter
            rvFav.setHasFixedSize(true)
            rvFav.layoutManager = LinearLayoutManager(this@FavActivity)
        }
        viewModel.getFavorite()?.observe(this, { favUsers ->
            favUsers?.let {
                val list = mapList(it)
                updateList(list)
                showLoading(false)
            }
        })
        showLoading(true)

    }

    private fun mapList(users: List<Favorite>): List<ItemsItem> {
        return users.map { user ->
            ItemsItem(
                user.login,
                user.id,
                user.avatarUrl
            )
        }
    }

    private fun updateList(newList: List<ItemsItem>) {
        val diffResult =
            DiffUtil.calculateDiff(UserAdapter.UserDiffCallback(userAdapter.getList(), newList))
        userAdapter.setList(newList)
        diffResult.dispatchUpdatesTo(userAdapter)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}