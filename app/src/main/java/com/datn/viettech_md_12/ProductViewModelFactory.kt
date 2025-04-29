package com.datn.viettech_md_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.datn.viettech_md_12.viewmodel.ProductViewModel

class ProductViewModelFactory (private val networkHelper: NetworkHelper) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(networkHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}