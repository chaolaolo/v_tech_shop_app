package com.datn.viettech_md_12

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
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
                RequestNotificationPermission()
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
@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("NotificationPermission", "Đã cấp quyền thông báo")
        } else {
            Log.d("NotificationPermission", "Từ chối quyền thông báo")
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewCustomBanner() {
    NavigationGraph(startDestination = "home")
}
