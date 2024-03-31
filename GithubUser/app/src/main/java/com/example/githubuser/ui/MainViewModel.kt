package com.example.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.GithubResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<ItemsItem>>(

    )

    fun setUsers(query: String){
        ApiConfig.getApiService().getGithub(query)
            .enqueue(object : Callback<GithubResponse>{
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    if (response.isSuccessful){
                        listUsers.postValue(response.body()?.items)

                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.d("onFailure:", t.message ?: "Unknown error")
                }

            })
    }

    fun getUsers(): LiveData<ArrayList<ItemsItem>>{
        return listUsers
    }
}