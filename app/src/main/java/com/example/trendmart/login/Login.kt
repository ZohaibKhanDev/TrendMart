package com.example.trendmart.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trendmart.R
import com.example.trendmart.restapi.MainViewModel
import org.koin.compose.koinInject

@Composable
fun Login(navController: NavController) {

    val viewModel: MainViewModel = koinInject()
    val scope = rememberCoroutineScope()
    var email by remember {
        mutableStateOf("")
    }

    var pass by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "",
            contentScale = ContentScale.Crop, modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Welcome back!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))

        TextField(value = email, onValueChange = {
            pass = it
        }, placeholder = {
            Text(text = "UserName")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(61.dp), shape = RoundedCornerShape(1.dp), leadingIcon = {
                Icon(imageVector = Icons.Outlined.Person, contentDescription ="" )
        }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = pass, onValueChange = {
            pass = it
        }, placeholder = {
            Text(text = "PassWord")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(61.dp), shape = RoundedCornerShape(1.dp)
        )
    }
}