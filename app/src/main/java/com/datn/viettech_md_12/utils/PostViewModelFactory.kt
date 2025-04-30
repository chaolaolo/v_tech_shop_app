package com.datn.viettech_md_12.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.datn.viettech_md_12.viewmodel.PostViewModel

class PostViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}