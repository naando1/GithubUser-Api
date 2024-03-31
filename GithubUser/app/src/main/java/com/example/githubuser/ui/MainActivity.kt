package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.response.GithubResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.detailuser.UserDetailActivity
import com.example.githubuser.ui.favorite.FavActivity
import com.example.githubuser.ui.settings.SettingPreferences
import com.example.githubuser.ui.settings.SettingsActivity
import com.example.githubuser.ui.settings.SettingsViewModel
import com.example.githubuser.ui.settings.ViewModelFactory
import com.example.githubuser.ui.settings.dataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var userAdapter: UserAdapter

    companion object {
        private const val TAG = "MainActivity"
        var GITHUB_ID = "arif"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Users()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingsViewModel::class.java
        )

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

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
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onClick(data: ItemsItem) {
                Intent(this@MainActivity, UserDetailActivity::class.java).also { intent ->
                    intent.putExtra(UserDetailActivity.EXTRA_USERNAME, data.login)
                    intent.putExtra(UserDetailActivity.EXTRA_ID, data.id)
                    intent.putExtra(UserDetailActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(intent)
                }
            }
        })


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)


        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = userAdapter

            btnCari.setOnClickListener {
                searchUser()
            }

            cariEdit.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
        viewModel.getUsers().observe(this) {
            if (it != null) {
                userAdapter.setList(it)
                showLoading(false)
            }
        }
    }

    private fun searchUser() {
        binding.apply {
            val search = cariEdit.text.toString()
            if (search.isEmpty()) return
            showLoading(true)
            viewModel.setUsers(search)
        }
    }

    private fun Users() {
        showLoading(true)
        val client = ApiConfig.getApiService().getGithub(GITHUB_ID)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                showLoading(false)
                Log.e("Response", "${response.message()}")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.e(TAG, "onSuccess: ${responseBody}")
                    if (responseBody != null) {
                        setGithubUser(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    private fun setGithubUser(users: List<ItemsItem>) {
        userAdapter.setList(users)

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onClick(data: ItemsItem) {
                Intent(this@MainActivity, UserDetailActivity::class.java).also { intent ->
                    intent.putExtra(UserDetailActivity.EXTRA_USERNAME, data.login)
                    intent.putExtra(UserDetailActivity.EXTRA_ID, data.id)
                    intent.putExtra(UserDetailActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(intent)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}