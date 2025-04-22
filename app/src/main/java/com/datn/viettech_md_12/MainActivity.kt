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
import com.onesignal.OneSignal
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Khởi tạo OneSignal
        OneSignal.initWithContext(this)
        OneSignal.setAppId("29ae4e65-bafd-49fb-8c3b-cde54d2bf2bb")
        // Đăng ký Observer để theo dõi trạng thái đăng ký
        OneSignal.addSubscriptionObserver { stateChanges ->
            if (stateChanges.to.isSubscribed) {
                val playerId = stateChanges.to.userId
                val userId = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("userId", null)

                if (!playerId.isNullOrEmpty() && !userId.isNullOrEmpty()) {
                    Log.d("dcm_onesignal", "OneSignal playerId READY: $playerId")
                    setExternalUserId(userId)  // Gọi setExternalUserId thay vì gửi lên server
                }
            }
        }
        // Lấy playerId và userId sau khi OneSignal được khởi tạo
        val playerId = OneSignal.getDeviceState()?.userId
        val userId = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("userId", null)

        if (!playerId.isNullOrEmpty() && !userId.isNullOrEmpty()) {
            Log.d("dcm_onesignal", "Player ID: $playerId")
            setExternalUserId(userId)  // Gọi setExternalUserId thay vì gửi lên server
            OneSignal.sendTag("externalUserId", userId)
        } else {
            Log.w("dcm_onesignal", "Player ID hoặc userId là null. OneSignal chưa khởi tạo xong?")
        }

        // Cấu hình xử lý khi nhận thông báo trong foreground
        OneSignal.setNotificationWillShowInForegroundHandler { event ->
            val notification = event.notification
            val title = notification.title
            val body = notification.body
            Log.d("dcm_onesignal", "Thông báo nhận: $title - $body")
            event.complete(notification)
        }

        // Cấu hình xử lý khi người dùng nhấn vào thông báo
        OneSignal.setNotificationOpenedHandler { result ->
            val notification = result.notification
            val actionId = result.action?.actionId
            Log.d("dcm_onesignal", "Thông báo được mở: ${notification.title}, actionId: $actionId")
        }

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
    private fun setExternalUserId(userId: String) {
        OneSignal.setExternalUserId(userId)
        Log.d("dcm_onesignal", "External user ID đã được gán: $userId")
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
