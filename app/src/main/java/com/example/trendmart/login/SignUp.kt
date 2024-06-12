package com.example.trendmart.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockPerson
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trendmart.restapi.MainViewModel
import com.example.trendmart.restapi.ResultState
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@Composable
fun SignUp(navController: NavController) {
    var Name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var passwrord by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val viewModel: MainViewModel = koinInject()
    var isLoading by remember {
        mutableStateOf(false)
    }
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Gray, trackColor = Color.Red)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "Let’s Get Started!", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Text(
                text = "Create an account on TrentMart to get all features",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = Name, onValueChange = {
                Name = it
            }, placeholder = { Text(text = "Name") }, leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = ""
                )
            }, textStyle = TextStyle(
                fontSize = 16.sp
            )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = email, onValueChange = {
                email = it
            }, placeholder = { Text(text = "Email") }, leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = ""
                )
            }, textStyle = TextStyle(
                fontSize = 16.sp
            )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = passwrord, onValueChange = {
                passwrord = it
            }, placeholder = { Text(text = "Password") }, leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = ""
                )
            }, textStyle = TextStyle(
                fontSize = 16.sp
            )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = confirmPassword, onValueChange = {
                confirmPassword = it
            }, placeholder = { Text(text = "Confirm Password") }, leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.LockPerson,
                    contentDescription = ""
                )
            }, textStyle = TextStyle(
                fontSize = 16.sp
            )
            )

            Spacer(modifier = Modifier.height(23.dp))
            if (passwrord <= 8.toString() && email.isEmpty() && passwrord != confirmPassword) {
                ElevatedButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Please Enter Email And Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 23.dp)
                        .height(53.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color(0XFF1e6d75)
                    )
                ) {
                    Text(text = "Login")
                }
            } else {
                ElevatedButton(
                    onClick = {
                        scope.launch {
                            viewModel.signUp(
                                User(
                                    email, passwrord
                                )
                            ).collect {
                                when (it) {
                                    is ResultState.Error -> {
                                        isLoading = false
                                        Toast.makeText(context, "${it.error}", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    ResultState.Loading -> {
                                        isLoading = true
                                    }

                                    is ResultState.Success -> {
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            "${it.response}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 23.dp)
                        .height(53.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color(0XFF1e6d75)
                    )
                ) {
                    Text(text = "Login")
                }
            }
            
            
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp), verticalAlignment = Alignment.CenterVertically){
                Text(text = "Already have an account?")
                Text(text = "Login here", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.clickable {  })
            }
        }
    }


}