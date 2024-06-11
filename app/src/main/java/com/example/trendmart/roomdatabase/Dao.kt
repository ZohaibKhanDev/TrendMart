package com.example.trendmart.roomdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavDao {
    @Query("SELECT * FROM Fav")
    fun getDao():List<Fav>

    @Insert
    fun Insert(fav: Fav)
}