package com.example.githubuser.ui.detailuser

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.data.dataFav.Favorite
import com.example.githubuser.data.dataFav.FavoriteDao
import com.example.githubuser.data.dataFav.UserDatabase
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUserResponse>()

    private var userDao: FavoriteDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDb(application)
        userDao = userDb?.favUserDao()
    }

    fun setDetailUser(username: String) {
        ApiConfig.getApiService().getDetailUser(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.d("onFailure:", t.message ?: "Unknown error")
                }

            })
    }

    fun getDetailUser(): LiveData<DetailUserResponse> {
        return user
    }

    fun addToFav(username: String, id: Int, avatarURL: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = Favorite(
                username,
                id,
                avatarURL
            )
            userDao?.addToFav(user)
        }
    }

    fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }

}