package com.datn.viettech_md_12.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.datn.viettech_md_12.MyApplication
import com.datn.viettech_md_12.component.CustomNavigationBar
import com.datn.viettech_md_12.component.checkout.AddressScreen
import com.datn.viettech_md_12.screen.CategoriesScreen
import com.datn.viettech_md_12.screen.HomeScreen
import com.datn.viettech_md_12.screen.ProductDetailScreen
import com.datn.viettech_md_12.screen.ProductListScreen
import com.datn.viettech_md_12.screen.ProfileScreen
import com.datn.viettech_md_12.screen.SearchScreen
import com.datn.viettech_md_12.screen.WishlistScreen
import com.datn.viettech_md_12.screen.authentication.LoginUser
import com.datn.viettech_md_12.screen.authentication.OnboardingScreen
import com.datn.viettech_md_12.screen.authentication.SignUpUser
import com.datn.viettech_md_12.screen.cart.CartScreen
import com.datn.viettech_md_12.screen.checkout.OrderSuccessfullyScreen
import com.datn.viettech_md_12.screen.checkout.PaymentUI
import com.datn.viettech_md_12.screen.checkout.checkout_cart.PaymentCartUI
import com.datn.viettech_md_12.screen.checkout.checkout_now.PaymentNowUI
import com.datn.viettech_md_12.screen.contact_us.ContactUsUI
import com.datn.viettech_md_12.screen.post.PostDetailScreen
import com.datn.viettech_md_12.screen.post.PostScreen
import com.datn.viettech_md_12.screen.post.SameTagsPosts
import com.datn.viettech_md_12.screen.profile_detail.ChangePasswordScreen
import com.datn.viettech_md_12.screen.profile_detail.OrderDetailScreen
import com.datn.viettech_md_12.screen.profile_detail.OrderHistoryScreen
import com.datn.viettech_md_12.screen.profile_detail.PaymentScreen
import com.datn.viettech_md_12.screen.profile_detail.ShippingScreen
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.datn.viettech_md_12.viewmodel.UserViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationGraph(startDestination: String = "home") {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    val productViewModel: ProductViewModel =
        (LocalContext.current.applicationContext as MyApplication).productViewModel
    val userViewModel: UserViewModel =
        (LocalContext.current.applicationContext as MyApplication).userViewModel

    val selectedRoute = when {
        currentBackStackEntry.value?.destination?.route == "categories" -> "categories"
        currentBackStackEntry.value?.destination?.route?.startsWith("category/") == true -> "categories"
        else -> currentBackStackEntry.value?.destination?.route ?: "home"
    }

//    val hideBottomBar = currentBackStackEntry.value?.destination?.route == "search"
    val hideBottomBar = when (selectedRoute) {
        "home", "categories", "cart", "wishlist", "profile" -> false
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
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("categories") { CategoriesScreen(navController) }
            composable("cart") { CartScreen(navController) }
            composable("wishlist") { WishlistScreen(viewModel = productViewModel,navController) }
            composable("profile") { ProfileScreen(navController) }
            composable("change_password_screen") { ChangePasswordScreen(navController,userViewModel = userViewModel) }
            composable("order_history_screen") { OrderHistoryScreen(navController, viewModel = productViewModel) }
            composable("shipping_screen") { ShippingScreen(navController) }
            composable("search") { SearchScreen(navController) }
            composable("payment_screen") { PaymentScreen(navController) }
            composable("onb_screen") { OnboardingScreen(navController) }
            composable("order_successfully") { OrderSuccessfullyScreen(navController) }
            composable("address_screen") { AddressScreen(navController) }
            composable("contact_us") { ContactUsUI(navController) }
            composable("post_screen") { PostScreen(navController) }
            composable("post_detail/{postId}") { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId") ?: ""
                PostDetailScreen(navController, postId = postId)
            }
            composable("same_tags_posts/{tag}") { backStackEntry ->
                val tag = backStackEntry.arguments?.getString("tag") ?: ""
                SameTagsPosts(navController, tag = tag)
            }
            //mới nhất(tách 2 màn thanh toán riêng)
            composable("payment_ui/{discount}") { backStackEntry ->
                val discount = backStackEntry.arguments?.getString("discount") ?: ""
                PaymentCartUI(
                    navController = navController,
                    discount = discount,
                )
            }
            composable("payment_now/product/{productId}/{quantity}/{variantId}", arguments = listOf(
                navArgument("productId") { type = NavType.StringType },
                navArgument("quantity") {
                    type = NavType.IntType
                    defaultValue = 1
                },
                navArgument("variantId") {
                    type = NavType.StringType
//                    nullable = true
                }
            )) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                val variantId = backStackEntry.arguments?.getString("variantId") ?: ""
                val quantity = backStackEntry.arguments?.getInt("quantity") ?: 1
                PaymentNowUI(
                    navController = navController,
                    productId = productId,
                    quantity = quantity,
                    variantId = variantId
                )
            }
            // Thêm 2 route riêng biệt cho 2 trường hợp
            composable("payment_ui/cart/{discount}") { backStackEntry ->
                val discount = backStackEntry.arguments?.getString("discount") ?: ""
                PaymentUI(
                    navController = navController,
                    discount = discount,
                    fromCart = true // Thêm flag để phân biệt
                )
            }

            composable("payment_ui/product/{productId}/{quantity}", arguments = listOf(
                navArgument("productId") { type = NavType.StringType },
                navArgument("quantity") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                PaymentUI(
                    navController = navController,
                    productId = productId,
                    quantity = backStackEntry.arguments?.getInt("quantity") ?: 1,
                    fromCart = false // Thêm flag để phân biệt
                )
            }
            composable("product_detail/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(navController, productId)
            }
            composable("order_detail/{orderId}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                OrderDetailScreen( orderId,navController)
            }
            composable("category/{categoryId}") { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                // Gửi categoryId tới ProductListScreen
                ProductListScreen(
                    navController = navController,
                    categoryId = categoryId,
                    productByCategoryViewModel = viewModel()
                )
            }
            composable("login") {
                LoginUser(userViewModel, navController)
            }
            composable("register") {
                SignUpUser(userViewModel, navController)
            }
        }
    }
}