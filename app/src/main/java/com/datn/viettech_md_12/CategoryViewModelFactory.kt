package com.datn.viettech_md_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.datn.viettech_md_12.viewmodel.CategoryViewModel

class CategoryViewModelFactory(private val networkHelper: NetworkHelper) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(networkHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}