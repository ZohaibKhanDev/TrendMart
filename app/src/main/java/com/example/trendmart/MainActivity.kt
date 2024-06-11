package com.example.trendmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.trendmart.login.Login
import com.example.trendmart.login.MainScreen
import com.example.trendmart.navigation.NavEntry
import com.example.trendmart.restapi.appModule
import com.example.trendmart.ui.theme.TrendMartTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            androidLogger()
            modules(appModule)
        }
        setContent {
            TrendMartTheme {
                val navController= rememberNavController()
              Login(navController)
            }
        }
    }
}

