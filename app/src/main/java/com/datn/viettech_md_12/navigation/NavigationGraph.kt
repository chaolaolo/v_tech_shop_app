package com.datn.viettech_md_12.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.component.CustomNavigationBar
import com.datn.viettech_md_12.screen.CategoriesScreen
import com.datn.viettech_md_12.screen.HomeScreen
import com.datn.viettech_md_12.screen.MyCartScreen
import com.datn.viettech_md_12.screen.ProfileScreen
import com.datn.viettech_md_12.screen.WishlistScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry.value?.destination?.route) {
    }

    Scaffold(
        bottomBar = {
            CustomNavigationBar(
                navController = navController,
                selectedRoute = currentBackStackEntry.value?.destination?.route ?: "home",
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
        ) {
            composable("home") { HomeScreen() }
            composable("categories") { CategoriesScreen() }
            composable("my_cart") { MyCartScreen() }
            composable("wishlist") { WishlistScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewCustomTopAppBar() {
    NavigationGraph()
}