package com.example.trendmart.restapi

import androidx.room.Room
import com.example.trendmart.login.AuthRepository
import com.example.trendmart.login.RepositoryImpl
import com.example.trendmart.roomdatabase.MyDataBase
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            MyDataBase::class.java,
            "demo.db"
        ).allowMainThreadQueries()
            .build()
    }

    single {
        FirebaseAuth.getInstance()
    }

    single<AuthRepository> {
        RepositoryImpl(get(), get())
    }

    single { Repository(get()) }
    viewModelOf(::MainViewModel)
}
