package com.example.trendmart.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trendmart.R
import com.example.trendmart.navigation.Screen
import com.example.trendmart.restapi.MainViewModel
import com.example.trendmart.restapi.ResultState
import kotlinx.coroutines.launch
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
    var isLoading by remember {
        mutableStateOf(false)
    }
    var visibility by remember {
        mutableStateOf(false)
    }
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White, trackColor = Color.Red)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.50f))
                .padding(start = 20.dp, end = 20.dp, top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Welcome back!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,

            )
            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                },
                placeholder = {
                    Text(text = "UserName")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(61.dp),
                shape = RoundedCornerShape(1.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = "")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    focusedContainerColor = Color(0XFFF2F2F2),
                    unfocusedContainerColor = Color(0XFFF2F2F2)
                )
            )


            TextField(
                value = pass,
                onValueChange = {
                    pass = it
                },
                placeholder = {
                    Text(text = "PassWord")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(61.dp),
                shape = RoundedCornerShape(1.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Lock, contentDescription = "")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    focusedContainerColor = Color(0XFFF2F2F2),
                    unfocusedContainerColor = Color(0XFFF2F2F2)
                ),
                trailingIcon = {
                    if (pass >= 1.toString()) {
                        Icon(
                            imageVector = if (visibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "",
                            modifier = Modifier.clickable { visibility = !visibility })
                    }
                },
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))
            if (email.isEmpty() && pass <= 8.toString()) {
                Button(
                    onClick = {

                        Toast.makeText(
                            context,
                            "Please Enter Email And Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    shape = RoundedCornerShape(11.dp),
                    modifier = Modifier
                        .width(199.dp)
                        .padding(20.dp)
                        .height(53.dp), colors = ButtonDefaults.buttonColors(Color(0XFF769DAD))
                ) {
                    Text(text = "Login")
                }
            } else {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.login(
                                User(email, pass)
                            ).collect {
                                when (it) {
                                    is ResultState.Error -> {
                                        isLoading = false
                                        Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG)
                                            .show()
                                    }

                                    ResultState.Loading -> {
                                        isLoading = true

                                    }

                                    is ResultState.Success -> {
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            "Login SuccessFully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(11.dp),
                    modifier = Modifier
                        .width(199.dp)
                        .padding(20.dp)
                        .height(53.dp), colors = ButtonDefaults.buttonColors(Color(0XFF769DAD))
                ) {
                    Text(text = "Login")
                }
            }

        }
    }


}