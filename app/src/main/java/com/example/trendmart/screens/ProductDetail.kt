package com.example.trendmart.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.trendmart.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetail(
    navController: NavController,
    tittle: String?,
    pic: String?,
    price: String?,
    des: String?,
    category: String?,
    rating: String?,
) {
    val sizes = listOf(35, 36, 37, 38, 39, 40)
    var selectedSize by remember { mutableStateOf(38) }
    var count by remember { mutableStateOf(0) }
    val scroll = rememberScrollState()
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(top = 20.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { navController.popBackStack() }
                    .size(40.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "")
            }


            Text(
                text = "Product Detail",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }

        AsyncImage(
            model = pic,
            contentDescription = "",
            modifier = Modifier
                .size(500.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "$tittle",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    repeat(5) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFFFC107)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "$rating",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                }

                Text(
                    text = "$category",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Text(
                    text = "$$price",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF007bff)
                )

                Text(
                    text = "$des",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.clickable { count++ }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "$count", fontSize = 18.sp, color = Color.Black)

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.Minimize,
                            contentDescription = "Subtract",
                            modifier = Modifier
                                .padding(bottom = 14.dp)
                                .size(23.dp)
                                .align(Alignment.CenterVertically)
                                .combinedClickable(
                                    onClick = {
                                        if (count > 0) {
                                            count--
                                        }
                                    },
                                    onLongClick = {
                                        count = 0
                                    }
                                )
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        items(sizes) { size ->
                            Box(
                                modifier = Modifier
                                    .width(44.dp)
                                    .height(43.dp)
                                    .background(if (size == selectedSize) Color(0XFF007bff) else Color.White)
                                    .clickable { selectedSize = size },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "$size")
                            }
                        }
                    }
                }


                Button(
                    onClick = {
                        Toast.makeText(context, "Add To Cart SuccessFully", Toast.LENGTH_SHORT)
                            .show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007bff)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = "Add To Card", color = Color.White)
                }
            }
        }
    }
}