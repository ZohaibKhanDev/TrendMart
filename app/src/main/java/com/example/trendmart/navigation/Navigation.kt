package com.example.trendmart.navigation

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Shop
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trendmart.login.Login
import com.example.trendmart.login.MainScreen
import com.example.trendmart.login.SignUp
import com.example.trendmart.screens.AccountScreen
import com.example.trendmart.screens.FavouriteScreen
import com.example.trendmart.screens.HomeScreen
import com.example.trendmart.screens.MyCard
import com.example.trendmart.screens.ProductDetail
import com.example.trendmart.screens.ShopScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Fav.route) {
            FavouriteScreen(navController = navController)
        }

        composable(Screen.Shop.route) {
            ShopScreen(navController = navController)
        }

        composable(Screen.Account.route) {
            AccountScreen(navController = navController)
        }
        composable(
            "${Screen.ProductDetail.route}/{tittle}/{pic}/{price}/{des}/{category}/{rating}",
            arguments = listOf(
                navArgument("tittle") { type = NavType.StringType },
                navArgument("pic") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType },
                navArgument("des") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType },
                navArgument("rating") { type = NavType.StringType },
            )
        ) {
            val tittle = it.arguments?.getString("tittle")
            val pic = it.arguments?.getString("pic")
            val price = it.arguments?.getString("price")
            val des = it.arguments?.getString("des")
            val category = it.arguments?.getString("category")
            val rating = it.arguments?.getString("rating")
            ProductDetail(navController = navController, tittle, pic, price, des, category, rating)
        }

        composable(Screen.MyCard.route){
            MyCard(navController)
        }
        
       /* composable(Screen.MainScreen.route){
            MainScreen(navController = navController)
        }*/
        
        composable(Screen.SignUp.route){
            SignUp(navController = navController)
        }
        
        composable(Screen.Login.route){
            Login(navController = navController)
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavEntry() {
    val navController= rememberNavController()
    
    Scaffold(bottomBar = { BottomNavigation(navController = navController)}) {
        Navigation(navController = navController)
    }
}


sealed class Screen(
    val tittle: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : Screen(
        "Home",
        "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Fav : Screen(
        "Fav",
        "Fav",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )

    object Shop : Screen(
        "Shop",
        "Shop",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart
    )

    object Account : Screen(
        "Account",
        "Account",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )


    object ProductDetail : Screen(
        "Account",
        "Account",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )


    object MyCard : Screen(
        "MyCard",
        "MyCard",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )


    object MainScreen : Screen(
        "MyCard",
        "MyCard",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )


    object SignUp : Screen(
        "MyCard",
        "MyCard",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )


    object Login : Screen(
        "MyCard",
        "MyCard",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )

}

@Composable
fun BottomNavigation(navController: NavController) {
    val item= listOf(
        Screen.Home,
        Screen.Fav,
        Screen.Shop,
        Screen.Account
    )

    NavigationBar(containerColor = Color.White, contentColor = Color.Red){
        val navStack by navController.currentBackStackEntryAsState()
        val current=navStack?.destination?.route
        item?.forEach {
            NavigationBarItem(selected = current==it.route, onClick = {
                navController.navigate(it.route) {
                navController.graph?.let {
                    it.route?.let { it1 -> popUpTo(it1) }
                    launchSingleTop = true
                    restoreState = true
                }
            } }, icon = {
                if (current==it.route) {
                    Icon(imageVector = it.selectedIcon, contentDescription = "", tint = Color.Red)
                }else{
                    Icon(imageVector = it.unselectedIcon, contentDescription = "", tint = Color.Black)
                }
            }, colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White))
        }

    }


}