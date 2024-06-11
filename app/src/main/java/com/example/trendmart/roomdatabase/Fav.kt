package com.example.trendmart.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fav(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    @ColumnInfo("image")
    val image:String,
    @ColumnInfo("tittle")
    val tittle:String,
    @ColumnInfo("price")
    val price:String,
    @ColumnInfo("des")
    val des:String,
    @ColumnInfo("rating")
    val rating:String,
    @ColumnInfo("category")
    val category:String,


)

