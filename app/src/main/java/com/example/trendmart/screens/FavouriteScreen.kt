package com.example.trendmart.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.trendmart.navigation.Screen
import com.example.trendmart.restapi.MainViewModel
import com.example.trendmart.restapi.ResultState
import com.example.trendmart.roomdatabase.Fav
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavouriteScreen(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    var isLoaing by remember {
        mutableStateOf(false)
    }
    var favData by remember {
        mutableStateOf<List<Fav>?>(null)
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllFav()
    }
    var search by remember {
        mutableStateOf("")
    }
    val state by viewModel.allFav.collectAsState()
    when (state) {
        is ResultState.Error -> {
            isLoaing = false
            val error = (state as ResultState.Error).error
            Text(text = error.toString())
        }

        ResultState.Loading -> {
            isLoaing = true
        }

        is ResultState.Success -> {
            isLoaing = false
            val success = (state as ResultState.Success).response
            favData = success
        }
    }
    var filteredProducts by remember { mutableStateOf<List<Fav>?>(null) }

    LaunchedEffect(search) {
        filteredProducts = state.let { resultState ->
            when (resultState) {
                is ResultState.Success -> resultState.response.filter {
                    it.tittle.contains(search, ignoreCase = true)
                }

                else -> null
            }
        }
    }

    Scaffold(topBar = {
        LargeTopAppBar(title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = "Favourite")

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 1.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(value = search, onValueChange = {
                        search = it
                    }, placeholder = {
                        Text(text = "Search", fontSize = 14.sp)
                    }, modifier = Modifier
                        .height(50.dp)
                        .width(300.dp), textStyle = TextStyle(
                        fontSize = 15.sp
                    ), colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.60f),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.60f),
                    ), trailingIcon = {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "")
                    })

                    Icon(
                        imageVector = Icons.Outlined.FilterAlt,
                        contentDescription = "",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        })
    }) {
        if (isLoaing) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Red)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                filteredProducts?.let {
                    items(it) { fav ->
                        FavItem(fav = fav, navController)
                    }
                }
            }
        }


    }
}


@Composable
fun FavItem(fav: Fav, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Card(
            modifier = Modifier
                .width(142.dp)
                .clickable {
                    val encodedImage = Uri.encode(fav.image)
                    val encodedDescription = Uri.encode(fav.des)
                    navController.navigate(
                        "${Screen.ProductDetail.route}/${fav.tittle}/$encodedImage/${fav.price}/$encodedDescription/${fav.category}/${fav.rating}"
                    )
                }
                .height(184.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                Color(0XFFDDD5D5)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = fav.image, contentDescription = "", contentScale = ContentScale.Crop
                )
            }
        }

        Text(
            text = "${fav.tittle}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0XFF1E1E1E).copy(alpha = 0.85f)
        )

        Text(text = "$${fav.price}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}