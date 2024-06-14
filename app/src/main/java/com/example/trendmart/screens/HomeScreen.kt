package com.example.trendmart.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.accompanist.pager.ExperimentalPagerApi
import org.koin.compose.koinInject

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalPagerApi::class
)
@Composable
fun HomeScreen(navController: NavController) {
    var searchBar by remember { mutableStateOf(false) }
    var notification by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    val viewModel: MainViewModel = koinInject()
    val state by viewModel.allProdect.collectAsState()
    var filteredProducts by remember { mutableStateOf<List<ProdectItem>?>(null) }
    var selectedCategoryIndex by remember { mutableStateOf(0) }

    val uniqueCategories = remember(state) {
        when (state) {
            is ResultState.Success -> (state as ResultState.Success<List<ProdectItem>>).response.map { it.category }
                .distinct()

            else -> listOf("All")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllProduct()
        Log.d("HomeScreen", "Fetching products")
    }

    LaunchedEffect(search, selectedCategoryIndex) {
        filteredProducts = state.let { resultState ->
            when (resultState) {
                is ResultState.Success -> {
                    resultState.response.filter { product ->
                        product.title.contains(
                            search,
                            ignoreCase = true
                        ) && (selectedCategoryIndex == 0 || product.category == uniqueCategories[selectedCategoryIndex])
                    }
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
                    placeholder = { Text(text = "Search", fontSize = 15.sp) },
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
                    textStyle = TextStyle(fontSize = 15.sp),
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
                Box(
                    modifier = Modifier.size(45.dp), contentAlignment = Alignment.Center
                ) {
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
                    Text(
                        text = "Hi, Johnathon",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Lets go shopping", fontSize = 12.sp, color = Color.Gray
                    )
                }
            }
        }, actions = {
            Icon(imageVector = Icons.Outlined.Search,
                contentDescription = "",
                modifier = Modifier.clickable { searchBar = !searchBar })

            Spacer(modifier = Modifier.width(20.dp))

            if (notification){
                Icon(imageVector = Icons.Filled.Notifications, contentDescription = "", modifier = Modifier.clickable { notification=!notification }, tint = Color.Red)
            } else{
                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "", modifier = Modifier.clickable { notification=!notification })
            }
        })
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(uniqueCategories) { category ->
                    CategoryTab(categoryName = category,
                        selected = category == uniqueCategories[selectedCategoryIndex],
                        onClick = { selectedCategoryIndex = uniqueCategories.indexOf(category) })
                }
            }

            when (state) {
                is ResultState.Error -> {
                    val error = (state as ResultState.Error).error
                    Text(text = error.toString())
                }

                ResultState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
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
                    val productsToDisplay = filteredProducts ?: allProducts

                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.height(19.dp))

                        val bannerImages = listOf(
                            Banner(R.drawable.banner1),
                            Banner(R.drawable.banner2),
                            Banner(R.drawable.banner3)
                        )
                        val pagerState = rememberPagerState(pageCount = { bannerImages.size })

                        HorizontalPager(
                            state = pagerState,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) { page ->
                            BannerImage(bannerImages[page].pic)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        BannerDotsIndicator(pagerState)

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
                            items(productsToDisplay) { product ->
                                ProdectItem(
                                    prodectItem = product,
                                    onSeeMoreClick = { product.title },
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BannerImage(imageResource: Int) {
    Box(
        modifier = Modifier
            .width(400.dp)
            .height(150.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun BannerDotsIndicator(pagerState: androidx.compose.foundation.pager.PagerState) {
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(8.dp)
            )
        }
    }
}

@Composable
fun CategoryTab(
    categoryName: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(top = 10.dp, bottom = 6.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = categoryName,
            color = if (selected) Color.Blue.copy(alpha = 0.70f) else Color.Black,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        )
        if (selected) {
            Divider(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .wrapContentWidth(),
                color = Color.Blue,
                thickness = 2.dp
            )
        }
    }
}


@Composable
fun ProdectItem(
    prodectItem: ProdectItem, onSeeMoreClick: () -> Unit, navController: NavController
) {
    val viewModel: MainViewModel = koinInject()
    var fav by remember { mutableStateOf(false) }
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
                        .background(Color.DarkGray.copy(alpha = 0.90f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (fav) {
                        Icon(imageVector = Icons.Default.Favorite,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(25.dp)
                                .clickable { fav = !fav })
                    } else {
                        Icon(imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(25.dp)
                                .clickable {
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

data class Banner(val pic: Int)