package com.datn.viettech_md_12.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.datn.viettech_md_12.MyApplication
import com.datn.viettech_md_12.component.CustomNavigationBar
import com.datn.viettech_md_12.screen.CategoriesScreen
import com.datn.viettech_md_12.screen.HomeScreen
import com.datn.viettech_md_12.screen.ProductDetailScreen
import com.datn.viettech_md_12.screen.ProductListScreen
import com.datn.viettech_md_12.screen.ProfileScreen
import com.datn.viettech_md_12.screen.SearchScreen
import com.datn.viettech_md_12.screen.WishlistScreen
import com.datn.viettech_md_12.screen.cart.CartScreen
import com.datn.viettech_md_12.screen.checkout.CheckoutReviewItemsScreen
import com.datn.viettech_md_12.screen.checkout.CheckoutScreen
import com.datn.viettech_md_12.screen.profile_detail.ChangePasswordScreen
import com.datn.viettech_md_12.screen.profile_detail.OrderHistoryScreen
import com.datn.viettech_md_12.screen.profile_detail.ShippingScreen
import com.datn.viettech_md_12.viewmodel.ProductViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    val productViewModel: ProductViewModel =
        (LocalContext.current.applicationContext as MyApplication).productViewModel

    val selectedRoute = when {
        currentBackStackEntry.value?.destination?.route == "categories" -> "categories"
        currentBackStackEntry.value?.destination?.route?.startsWith("category/") == true -> "categories"
        else -> currentBackStackEntry.value?.destination?.route ?: "home"
    }

//    val hideBottomBar = currentBackStackEntry.value?.destination?.route == "search"
    val hideBottomBar = when (selectedRoute) {
        "home", "categories", "my_cart", "wishlist", "profile" -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (!hideBottomBar) {
                CustomNavigationBar(
                    navController = navController,
                    selectedRoute = selectedRoute
                )
            }
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("categories") { CategoriesScreen(navController) }
            composable("my_cart") { CartScreen(navController) }
            composable("wishlist") { WishlistScreen() }
            composable("profile") { ProfileScreen(navController) }
            composable("change_password_screen") { ChangePasswordScreen(navController) }
            composable("order_history_screen") { OrderHistoryScreen(navController) }
            composable("shipping_screen") { ShippingScreen(navController) }
            composable("search") { SearchScreen(navController) }
            composable("payment") { CheckoutScreen(navController) }
            composable("review_items") { CheckoutReviewItemsScreen(navController) }
            composable("product_detail/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(navController)
            }
            composable("category/{categoryName}") { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                ProductListScreen(
                    navController, categoryName,
                    productViewModel = productViewModel
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewCustomTopAppBar() {
    NavigationGraph()
}
