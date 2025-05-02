package com.datn.viettech_md_12

import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.data.repository.ProductRepository
import com.datn.viettech_md_12.viewmodel.CategoryViewModel
import com.datn.viettech_md_12.viewmodel.ProductViewModel
import com.datn.viettech_md_12.viewmodel.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val categoryModule = module {
    single { NetworkHelper(androidContext()) }

    // Truyền trực tiếp ApiClient.categoryRepository nếu bạn đang dùng singleton
    single { ApiClient.categoryRepository }

    viewModel {
        CategoryViewModel(
            networkHelper = get(),
            repository = get()
        )
    }
}

val productModule = module {
    // Inject NetworkHelper
    single { NetworkHelper(androidContext()) }

    // Inject ProductRepository
    single { ProductRepository(ApiClient.productService) }

    // Inject ProductViewModel
    viewModel { ProductViewModel(networkHelper = get(), repository = get()) }
}

val searchModule = module {
    // Inject ProductRepository
    single { ProductRepository(ApiClient.productService) }

    // Inject SearchViewModel
    viewModel { SearchViewModel(repository = get()) }
}