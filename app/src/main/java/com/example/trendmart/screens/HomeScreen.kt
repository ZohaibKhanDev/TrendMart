package com.example.trendmart.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.trendmart.R
import com.example.trendmart.navigation.Screen
import com.example.trendmart.restapi.MainViewModel
import com.example.trendmart.restapi.ProdectItem
import com.example.trendmart.restapi.ResultState
import com.example.trendmart.roomdatabase.Fav
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchBar by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    val viewModel: MainViewModel = koinInject()
    val state by viewModel.allProdect.collectAsState()
    var filteredProducts by remember { mutableStateOf<List<ProdectItem>?>(null) }
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getAllProduct()
        Log.d("HomeScreen", "Fetching products")
    }

    LaunchedEffect(search) {
        filteredProducts = state.let { resultState ->
            when (resultState) {
                is ResultState.Success -> resultState.response.filter {
                    it.title.contains(search, ignoreCase = true)
                }

                else -> null
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            if (searchBar) {
                TextField(value = search,
                    onValueChange = { search = it },
                    placeholder = {
                        Text(text = "Search", fontSize = 15.sp)
                    },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 5.dp)
                        .width(320.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.60f),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.60f),
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 15.sp
                    ),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Clear,
                            contentDescription = "",
                            modifier = Modifier.clickable { searchBar = false })
                    })
            }
        }, navigationIcon = {
            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(modifier = Modifier.size(45.dp), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.banner3),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))


                Column(
                    modifier = Modifier.wrapContentWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Hi,Johnathon", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "lets go shopping", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }, actions = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "",
                modifier = Modifier.clickable { searchBar = !searchBar })

            Spacer(modifier = Modifier.width(20.dp))

            Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "")


        })
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
            ) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(category.name) }
                    )
                }
            }

            when (state) {
                is ResultState.Error -> {
                    val error = (state as ResultState.Error).error
                    Text(text = error.toString())
                }

                ResultState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(60.dp),
                            color = Color.LightGray,
                            trackColor = Color.Red,
                            strokeCap = StrokeCap.Butt
                        )
                    }
                }

                is ResultState.Success -> {
                    val allProducts = (state as ResultState.Success).response
                    val products = filteredProducts ?: allProducts
                    val filteredByCategory = if (selectedTabIndex == 0) {
                        products
                    } else {
                        products?.filter { it.category == categories[selectedTabIndex].name }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(19.dp))
                        val images = listOf(
                            Banner(R.drawable.banner1),
                            Banner(R.drawable.banner2),
                            Banner(R.drawable.banner3),
                        )
                        val pagerState = rememberPagerState(pageCount = { images.size })


                        HorizontalPager(
                            state = pagerState,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .width(400.dp)
                                    .height(150.dp)
                                    .background(Color.LightGray), contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = images[page].pic),
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(pagerState.pageCount) { iteration ->
                                val color =
                                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(7.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Top Products",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "See All",
                                fontSize = 13.sp,
                                color = Color.Blue.copy(alpha = 0.70f)
                            )
                        }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 60.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            filteredByCategory?.let {
                                items(it) { product ->
                                    ProdectItem(
                                        prodectItem = product,
                                        onSeeMoreClick = { product.title },
                                        navController
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ProdectItem(
    prodectItem: ProdectItem, onSeeMoreClick: () -> Unit, navController: NavController
) {
    val viewModel: MainViewModel = koinInject()

    var fav by remember {
        mutableStateOf(false)
    }
    var showMore by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .width(174.dp)
            .height(224.dp),
        colors = CardDefaults.cardColors(Color.LightGray.copy(alpha = 0.40f)),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box {
                AsyncImage(model = prodectItem.image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(175.dp)
                        .clickable {
                            val encodedImage = Uri.encode(prodectItem.image)
                            val encodedDescription = Uri.encode(prodectItem.description)
                            navController.navigate(
                                "${Screen.ProductDetail.route}/${prodectItem.title}/$encodedImage/${prodectItem.price}/$encodedDescription/${prodectItem.category}/${prodectItem.rating.rate}"
                            )
                        }
                        .height(118.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp)
                        .background(Color.LightGray.copy(alpha = 0.90f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (fav) {
                        Icon(imageVector = Icons.Default.Favorite,
                            contentDescription = "",
                            tint = Color.Red,
                            modifier = Modifier.clickable { fav = !fav })
                    } else {
                        Icon(imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "",
                            tint = Color.Red,
                            modifier = Modifier.clickable {
                                fav = !fav
                                val favourite = Fav(
                                    null,
                                    prodectItem.image,
                                    prodectItem.title,
                                    prodectItem.price.toString(),
                                    prodectItem.description,
                                    prodectItem.rating.rate.toString(),
                                    prodectItem.category
                                )
                                viewModel.Insert(favourite)
                            })
                    }

                }
            }

            Text(
                text = if (showMore) prodectItem.title else prodectItem.title.take(15) + "...",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = if (showMore) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$${prodectItem.price}",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            )

            if (!showMore) {
                TextButton(onClick = { showMore = true }) {
                    Text("See More")
                }
            } else {
                TextButton(onClick = onSeeMoreClick) {
                    Text("See Details")
                }
            }
        }
    }
}

data class Banner(
    val pic: Int
)

data class Category(val name: String)
val categories = listOf(
    Category("All"),
    Category("Categroy"),
)
