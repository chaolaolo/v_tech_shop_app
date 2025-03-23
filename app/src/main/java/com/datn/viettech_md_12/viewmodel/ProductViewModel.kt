package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.Product
import com.datn.viettech_md_12.data.remote.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _repository = ApiClient.productRepository

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products
    private val _product = MutableStateFlow<Product?>(null)
    val  product: StateFlow<Product?> = _product
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val myColorHexList = listOf("FF1C1B1B", "FF08E488", "FF21D4B4")

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            delay(2000)

            _products.value = listOf(
                Product(R.drawable.banner3, false, myColorHexList, "Product 0", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 1", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 2", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 3", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 4", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 5", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 6", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 7", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 8", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 9", 186.00, 126.00),
                Product(R.drawable.banner3, false, myColorHexList, "Product 10", 186.00, 126.00),
            )

            _isLoading.value = false
        }
    }

    fun getProductById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = _repository.getProductById(id)
                if (response.isSuccessful) {
                    _product.value = response.body()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", e.message.toString())
            }
            _isLoading.value = false
        }
    }

}