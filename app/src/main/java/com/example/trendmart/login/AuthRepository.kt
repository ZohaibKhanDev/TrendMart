package com.example.trendmart.login

import com.example.trendmart.login.User
import com.example.trendmart.restapi.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signUp(user: User):Flow<ResultState<String>>

    fun login(user: User):Flow<ResultState<String>>
}