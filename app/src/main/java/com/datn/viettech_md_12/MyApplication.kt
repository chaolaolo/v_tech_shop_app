package com.datn.viettech_md_12

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.datn.viettech_md_12.viewmodel.CategoryViewModel
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.datn.viettech_md_12.viewmodel.SearchViewModel
import com.datn.viettech_md_12.viewmodel.UserViewModel
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelStore
import com.datn.viettech_md_12.utils.CartViewModelFactory
import com.datn.viettech_md_12.utils.CheckoutViewModelFactory
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.CheckoutViewModel
import com.datn.viettech_md_12.viewmodel.NotificationViewModel
import com.onesignal.OneSignal
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
class MyApplication : Application() {
    lateinit var productViewModel: ProductViewModel
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var cartViewModel: CartViewModel
    lateinit var searchViewModel: SearchViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var notificationViewModel: NotificationViewModel
    lateinit var checkoutViewModel: CheckoutViewModel

    override fun onCreate() {
        super.onCreate()
        // Khởi tạo OneSignal
//        OneSignal.initWithContext(this)
//        OneSignal.setAppId("29ae4e65-bafd-49fb-8c3b-cde54d2bf2bb")
//
//        // Đăng ký Observer để theo dõi trạng thái đăng ký
//        OneSignal.addSubscriptionObserver { stateChanges ->
//            if (stateChanges.to.isSubscribed) {
//                val playerId = stateChanges.to.userId
//                val userId = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("userId", null)
//
//                if (!playerId.isNullOrEmpty() && !userId.isNullOrEmpty()) {
//                    Log.d("dcm_onesignal", "OneSignal playerId READY: $playerId")
//                    setExternalUserId(userId)  // Gọi setExternalUserId thay vì gửi lên server
//                }
//            }
//        }
//
//        // Lấy playerId và userId sau khi OneSignal được khởi tạo
//        val playerId = OneSignal.getDeviceState()?.userId
//        val userId = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("userId", null)
//
//        if (!playerId.isNullOrEmpty() && !userId.isNullOrEmpty()) {
//            Log.d("dcm_onesignal", "Player ID: $playerId")
//            setExternalUserId(userId)  // Gọi setExternalUserId thay vì gửi lên server
//            OneSignal.sendTag("externalUserId", userId)
//        } else {
//            Log.w("dcm_onesignal", "Player ID hoặc userId là null. OneSignal chưa khởi tạo xong?")
//        }
//
//        // Cấu hình xử lý khi nhận thông báo trong foreground
//        OneSignal.setNotificationWillShowInForegroundHandler { event ->
//            val notification = event.notification
//            val title = notification.title
//            val body = notification.body
//            Log.d("dcm_onesignal", "Thông báo nhận: $title - $body")
//            event.complete(notification)
//        }
//
//        // Cấu hình xử lý khi người dùng nhấn vào thông báo
//        OneSignal.setNotificationOpenedHandler { result ->
//            val notification = result.notification
//            val actionId = result.action?.actionId
//            Log.d("dcm_onesignal", "Thông báo được mở: ${notification.title}, actionId: $actionId")
//        }

        val networkHelper = NetworkHelper(this)
        // Khởi tạo các ViewModel
        productViewModel = ViewModelProvider(ViewModelStore(), ProductViewModelFactory(networkHelper))[ProductViewModel::class.java]
        categoryViewModel = ViewModelProvider(ViewModelStore(), CategoryViewModelFactory(networkHelper))[CategoryViewModel::class.java]
        cartViewModel = ViewModelProvider(ViewModelStore(), CartViewModelFactory(this,networkHelper))[CartViewModel::class.java]
        searchViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(SearchViewModel::class.java)
        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(UserViewModel::class.java)
        notificationViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(NotificationViewModel::class.java)
        checkoutViewModel = ViewModelProvider(ViewModelStore(), CheckoutViewModelFactory(this,networkHelper))[CheckoutViewModel::class.java]
    }

//    private fun setExternalUserId(userId: String) {
//        OneSignal.setExternalUserId(userId)
//        Log.d("dcm_onesignal", "External user ID đã được gán: $userId")
//    }

}