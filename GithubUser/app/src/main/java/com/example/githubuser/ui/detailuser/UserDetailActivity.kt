package com.example.githubuser.ui.detailuser

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.example.githubuser.ui.SectionPagerAdapter
import com.example.githubuser.ui.favorite.FavActivity
import com.example.githubuser.ui.settings.SettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: UserDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
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

        val username = intent.getStringExtra(EXTRA_USERNAME)!!
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarURL = intent.getStringExtra(EXTRA_AVATAR)!!
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(
            this
        ).get(UserDetailViewModel::class.java)
        showLoading(true)
        viewModel.setDetailUser(username)
        viewModel.getDetailUser().observe(this, {
            if (it != null) {
                binding.apply {
                    usrName.text = it.name
                    usrUsername.text = it.login
                    usrFollowers.text = "${it.followers} Followers"
                    usrFollowing.text = "${it.following} Following"
                    Glide.with(this@UserDetailActivity)
                        .load(it.avatarUrl)
                        .into(imgUsr)
                }
                showLoading(false)
            }
        })

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.btnFav.isChecked = true
                        isChecked = true
                    } else {
                        binding.btnFav.isChecked = false
                        isChecked = false
                    }
                }

            }
        }

        binding.btnFav.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addToFav(username, id, avatarURL)
            } else {
                viewModel.removeFromFavorite(id)
            }
            binding.btnFav.isChecked = isChecked
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}