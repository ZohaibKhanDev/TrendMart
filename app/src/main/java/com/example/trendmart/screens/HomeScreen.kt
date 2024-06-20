package com.example.trendmart.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
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
                            search, ignoreCase = true
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "",
                        modifier = Modifier.clickable { searchBar = false })
                    TextField(value = search,
                        onValueChange = { search = it },
                        placeholder = { Text(text = "Search", fontSize = 15.sp) },
                        modifier = Modifier
                            .padding()
                            .fillMaxWidth()
                            .height(50.dp),
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
                            if (search >= 0.toString()) {
                                Icon(imageVector = Icons.Default.Clear,
                                    contentDescription = "",
                                    modifier = Modifier.clickable { search = "" })
                            }

                        })
                }
            } else {

            }
        }, navigationIcon = {
            if (!searchBar) {
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
            }
        }, actions = {
            if (searchBar) {

            } else {
                Icon(imageVector = Icons.Outlined.Search,
                    contentDescription = "",
                    modifier = Modifier.clickable { searchBar = true })
            }

            Spacer(modifier = Modifier.width(20.dp))
            if (searchBar) {

            } else {
                if (notification) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "",
                        modifier = Modifier.clickable { notification = !notification },
                        tint = Color.Red
                    )
                } else {
                    Icon(imageVector = Icons.Outlined.Notifications,
                        contentDescription = "",
                        modifier = Modifier.clickable { notification = !notification })
                }
            }

        })
    }) { paddingValues ->
        Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {


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
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            items(uniqueCategories) { category ->
                                CategoryTab(categoryName = category,
                                    selected = category == uniqueCategories[selectedCategoryIndex],
                                    onClick = {
                                        selectedCategoryIndex = uniqueCategories.indexOf(category)
                                    })
                            }
                        }

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
                                color = Color.Blue.copy(alpha = 0.70f),
                                )
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 85.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(productsToDisplay) { product ->
                                ProductItem(
                                    productItem = product,
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
    categoryName: String, selected: Boolean, onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(top = 10.dp, bottom = 6.dp)
            .clickable(onClick = onClick), horizontalAlignment = Alignment.CenterHorizontally
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
fun ProductItem(
    productItem: ProdectItem, onSeeMoreClick: () -> Unit, navController: NavController
) {
    val viewModel: MainViewModel = koinInject()
    var fav by remember { mutableStateOf(false) }
    var isFavorited by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable {
                val encodedImage = Uri.encode(productItem.image)
                val encodedDescription = Uri.encode(productItem.description)
                navController.navigate(
                    "${Screen.ProductDetail.route}/${productItem.title}/$encodedImage/${productItem.price}/$encodedDescription/${productItem.category}/${productItem.rating.rate}"
                )
            },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = productItem.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )


                IconButton(
                    onClick = {
                        isFavorited = !isFavorited
                    }, modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorited) "Remove from Favorites" else "Add to Favorites",
                        tint = Color.White,
                        modifier = Modifier
                            .background(
                                color = if (isFavorited) Color(0xFFF44336) else Color.Gray.copy(
                                    alpha = 0.7f
                                ), shape = CircleShape
                            )
                            .padding(4.dp)
                    )
                }
            }

            Text(
                text = productItem.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$${productItem.price}", color = Color.Gray, fontSize = 14.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add to Cart", color = Color.Gray, fontSize = 12.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Button(
                    onClick = {
                        val encodedImage = Uri.encode(productItem.image)
                        val encodedDescription = Uri.encode(productItem.description)
                        navController.navigate(
                            "${Screen.ProductDetail.route}/${productItem.title}/$encodedImage/${productItem.price}/$encodedDescription/${productItem.category}/${productItem.rating}"
                        )
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )
                ) {
                    Text("See Details", color = Color.White)
                }
            }

        }
    }
}

@Composable
fun ProductList(
    products: List<ProdectItem>, navController: NavController
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(
                productItem = product, onSeeMoreClick = {
                    navController.navigate("productDetails/${product.id}")
                }, navController = navController
            )
        }
    }
}

data class Banner(val pic: Int)