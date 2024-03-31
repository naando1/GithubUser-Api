package com.example.githubuser.ui.detailuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    val listFollowing = MutableLiveData<ArrayList<ItemsItem>>()

    fun setFollowingList(username: String) {
        ApiConfig.getApiService().getFollowing(username)
            .enqueue(object : Callback<ArrayList<ItemsItem>> {
                override fun onResponse(
                    call: Call<ArrayList<ItemsItem>>,
                    response: Response<ArrayList<ItemsItem>>
                ) {
                    if (response.isSuccessful) {
                        listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                    Log.d("onFailure:", t.message ?: "Unknown error")
                }

            })
    }

    fun getFollowingList(): LiveData<ArrayList<ItemsItem>> {
        return listFollowing
    }
}