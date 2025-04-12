package com.datn.viettech_md_12

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.datn.viettech_md_12.viewmodel.CategoryViewModel
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.datn.viettech_md_12.viewmodel.SearchViewModel
import com.datn.viettech_md_12.viewmodel.UserViewModel

class MyApplication : Application() {
    lateinit var productViewModel: ProductViewModel
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var searchViewModel: SearchViewModel
    lateinit var userViewModel: UserViewModel

    override fun onCreate() {
        super.onCreate()
        productViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(
            ProductViewModel::class.java
        )

        categoryViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(
            CategoryViewModel::class.java
        )

        searchViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(
            SearchViewModel::class.java
        )
        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(
            UserViewModel::class.java
        )
    }
}