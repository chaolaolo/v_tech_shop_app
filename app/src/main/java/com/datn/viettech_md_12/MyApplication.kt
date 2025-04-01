package com.datn.viettech_md_12

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.datn.viettech_md_12.viewmodel.CategoryViewModel
import com.datn.viettech_md_12.viewmodel.ProductViewModel

class MyApplication : Application() {
    lateinit var productViewModel: ProductViewModel
    lateinit var categoryViewModel: CategoryViewModel

    override fun onCreate() {
        super.onCreate()
        productViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(
            ProductViewModel::class.java
        )

        categoryViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(
            CategoryViewModel::class.java
        )
    }
}