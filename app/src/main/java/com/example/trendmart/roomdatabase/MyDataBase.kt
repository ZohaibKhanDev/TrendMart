package com.example.trendmart.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import org.koin.core.annotation.Single

@Single
@Database(entities = [Fav::class], version = 1)
abstract class MyDataBase : RoomDatabase(){
    abstract fun getfav(): FavDao
}