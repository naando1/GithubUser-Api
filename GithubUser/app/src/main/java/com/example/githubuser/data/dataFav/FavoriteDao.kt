package com.example.githubuser.data.dataFav

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    fun addToFav(favorite: Favorite)

    @Query("SELECT * FROM fav_user")
    fun getFavorite(): LiveData<List<Favorite>>

    @Query("SELECT count(*) FROM fav_user WHERE fav_user.id = :id")
    fun checkUser(id: Int): Int

    @Query("DELETE FROM fav_user WHERE fav_user.id = :id")
    fun removeFromFavorite(id: Int): Int




}