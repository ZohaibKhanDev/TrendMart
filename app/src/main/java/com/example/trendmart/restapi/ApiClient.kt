package com.example.trendmart.restapi

import org.koin.core.annotation.Single

@Single
interface ApiClient {
    suspend fun getAllProduct():List<ProdectItem>
}