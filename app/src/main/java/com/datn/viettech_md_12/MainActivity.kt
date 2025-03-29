package com.datn.viettech_md_12

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
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
        controller.hide(android.view.WindowInsets.Type.systemBars()) // Ẩn cả thanh trạng thái và thanh điều hướng
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        setContent {
//            VietTech_MD_12Theme {
//                NavigationGraph()
//            }
            val hasSeenOnboarding = remember { mutableStateOf(false) }
            NavigationGraph(startDestination = if (hasSeenOnboarding.value) "home" else "onb_screen")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun PreviewCustomBanner() {
    NavigationGraph()
}