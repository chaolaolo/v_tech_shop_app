package com.datn.viettech_md_12

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.datn.viettech_md_12.viewmodel.ProductViewModel

class MyApplication : Application() {
    lateinit var productViewModel: ProductViewModel

    override fun onCreate() {
        super.onCreate()
        productViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(
            ProductViewModel::class.java
        )
    }
}