package com.datn.viettech_md_12

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.datn.viettech_md_12.navigation.NavigationGraph
import com.datn.viettech_md_12.ui.theme.VietTech_MD_12Theme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
//        controller.hide(android.view.WindowInsets.Type.systemBars()) // Ẩn cả thanh trạng thái và thanh điều hướng
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Lấy thông tin đăng nhập từ SharedPreferences
        val sharedPrefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("IS_LOGGED_IN", false)
        val hasSeenOnboarding = sharedPrefs.getBoolean("HAS_SEEN_ONBOARDING", false)

        setContent {
            VietTech_MD_12Theme {
                // Điều hướng dựa trên trạng thái đăng nhập và đã xem onboarding
                val startDestination = when {
                    isLoggedIn -> "home" // Nếu đã đăng nhập, đi đến màn Home
                    hasSeenOnboarding -> "home" // Nếu đã xem onboarding nhưng chưa đăng nhập, đi đến Home
                    else -> "onb_screen" // Nếu chưa xem onboarding, đi đến Onboarding
                }
                NavigationGraph(startDestination = startDestination)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewCustomBanner() {
    NavigationGraph(startDestination = "home")
}
