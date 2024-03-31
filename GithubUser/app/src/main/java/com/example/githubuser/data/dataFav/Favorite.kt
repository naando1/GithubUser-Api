package com.example.githubuser.data.dataFav

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "fav_user")

data class Favorite(
    val login: String,
    @PrimaryKey
    val id: Int,
    val avatarUrl : String
):Serializable