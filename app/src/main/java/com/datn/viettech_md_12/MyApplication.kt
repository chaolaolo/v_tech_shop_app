package com.datn.viettech_md_12

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.datn.viettech_md_12.viewmodel.UserViewModel
import androidx.lifecycle.ViewModelStore
import com.datn.viettech_md_12.utils.CartViewModelFactory
import com.datn.viettech_md_12.utils.CheckoutViewModelFactory
import com.datn.viettech_md_12.utils.PostViewModelFactory
import com.datn.viettech_md_12.viewmodel.CartViewModel
import com.datn.viettech_md_12.viewmodel.CheckoutViewModel
import com.datn.viettech_md_12.viewmodel.NotificationViewModel
import com.datn.viettech_md_12.viewmodel.PostViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    lateinit var productViewModel: ProductViewModel
    lateinit var cartViewModel: CartViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var notificationViewModel: NotificationViewModel
    lateinit var checkoutViewModel: CheckoutViewModel
    lateinit var postViewModel: PostViewModel

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    categoryModule,
                    productModule
                )
            )
        }

        val networkHelper = NetworkHelper(this)
        // Khởi tạo các ViewModel
        productViewModel = ViewModelProvider(ViewModelStore(), ProductViewModelFactory(networkHelper))[ProductViewModel::class.java]
        cartViewModel = ViewModelProvider(ViewModelStore(), CartViewModelFactory(this,networkHelper))[CartViewModel::class.java]
        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(UserViewModel::class.java)
        notificationViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(NotificationViewModel::class.java)
        checkoutViewModel = ViewModelProvider(ViewModelStore(), CheckoutViewModelFactory(this,networkHelper))[CheckoutViewModel::class.java]
        postViewModel = ViewModelProvider(ViewModelStore(), PostViewModelFactory(this,networkHelper))[PostViewModel::class.java]
    }


}