package com.example.trendmart.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Commit
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    var notification by remember {
        mutableStateOf(false)
    }
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
        CenterAlignedTopAppBar(title = {
            Text(text = "My Favorite", fontWeight = FontWeight.W500, fontSize = 18.sp)
        }, actions = {
            if (notification) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "",
                    modifier = Modifier.clickable { notification = !notification },
                    tint = Color.Red
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "",
                    modifier = Modifier.clickable { notification = !notification },tint = Color.Red
                )
            }
        })
    }) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp),
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
                Icon(imageVector = Icons.Outlined.Commit, contentDescription = "")
            }, leadingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "")
            })

        }

        if (isLoaing) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Red)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 150.dp),
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FavItem(fav: Fav, navController: NavController) {
    var isElevated by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                onClick = {
                    val encodedImage = Uri.encode(fav.image)
                    val encodedDescription = Uri.encode(fav.des)
                    navController.navigate(
                        "${Screen.ProductDetail.route}/${fav.tittle}/$encodedImage/${fav.price}/$encodedDescription/${fav.category}/${fav.rating}"
                    )
                }
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(184.dp)
                .shadow(
                    elevation = if (isElevated) 12.dp else 2.dp,
                    spotColor = Color(0xFF24B363),
                    shape = RoundedCornerShape(16.dp)
                )
                .background(Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = fav.image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp)
                            .background(Color.Gray.copy(alpha = 0.60f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "",
                            tint = Color.Red
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = fav.tittle,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0XFF1E1E1E),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$${fav.price}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(6.dp))
        val rating = fav.rating.toDoubleOrNull() ?: 0.0
        Row {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= rating.toInt()) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Rating Star",
                    tint = if (i <= rating.toInt()) Color(0xFFFFC107) else Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp).size(16.dp)
                )
            }
        }
    }
}
