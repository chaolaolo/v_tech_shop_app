package com.datn.viettech_md_12.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.Category
import com.datn.viettech_md_12.data.model.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // Fake API Data - StateFlow
    private val _banners = MutableStateFlow<List<Int>>(emptyList())
    val banners: StateFlow<List<Int>> = _banners

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _latestProducts = MutableStateFlow<List<Product>>(emptyList())
    val latestProducts: StateFlow<List<Product>> = _latestProducts

    // Màu giả lập
    private val myColorHexList = listOf("FF1C1B1B", "FF08E488", "FF21D4B4")

    init {
        loadFakeData()
    }

    private fun loadFakeData() {
        viewModelScope.launch {
            // Giả lập thời gian tải API
            delay(2000)

            // Fake danh sách banners
            _banners.value = listOf(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3,
                R.drawable.banner4
            )

            // Fake danh mục
            _categories.value = listOf(
                Category("Electronics", R.drawable.ic_category1),
                Category("Fashion", R.drawable.ic_category2),
                Category("Furniture", R.drawable.ic_category3),
                Category("Industrial", R.drawable.ic_category4),
            )

            // Fake danh sách sản phẩm mới nhất
            _latestProducts.value = listOf(
                Product(R.drawable.banner3, false, myColorHexList, "Product 0", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 1", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 2", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 3", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 4", 186.00, 126.00),
            )
        }
    }
}
