package com.example.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuser.data.dataFav.Favorite
import com.example.githubuser.data.dataFav.FavoriteDao
import com.example.githubuser.data.dataFav.UserDatabase

class FavViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: FavoriteDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDb(application)
        userDao = userDb?.favUserDao()
    }

    fun getFavorite(): LiveData<List<Favorite>>? {
        return userDao?.getFavorite()
    }
}