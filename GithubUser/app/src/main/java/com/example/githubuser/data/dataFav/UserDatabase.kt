package com.example.githubuser.data.dataFav

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Favorite::class],
    version = 1
)

abstract class UserDatabase: RoomDatabase() {
    companion object{
        var INSTANCE : UserDatabase? = null

        fun getDb(context: Context): UserDatabase?{
            if(INSTANCE==null){
                synchronized((UserDatabase::class)){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "user_db").build()
                }
            }
            return INSTANCE
        }
    }
    abstract fun favUserDao(): FavoriteDao
}