package com.example.trendmart.restapi

import com.example.trendmart.roomdatabase.Fav
import com.example.trendmart.roomdatabase.MyDataBase
import org.koin.core.annotation.Single

@Single
class Repository(private val myDataBase: MyDataBase): ApiClient {
    override suspend fun getAllProduct(): List<ProdectItem> {
        return ProdectApiClient.getAllProduct()
    }

    fun getAllFav(): List<Fav> {
        return myDataBase.getfav().getDao()
    }

    fun Insert(fav: Fav) {
        myDataBase.getfav().Insert(fav)
    }
}

