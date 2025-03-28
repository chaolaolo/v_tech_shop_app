package com.datn.viettech_md_12.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.ProductModel
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.data.remote.ApiClient.productRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ProductViewModel : ViewModel() {
    private val _repository = ApiClient.productRepository

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products
    private val _product = MutableStateFlow<ProductModel?>(null)
    val product: StateFlow<ProductModel?> = _product
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val myColorHexList = listOf("FF1C1B1B", "FF08E488", "FF21D4B4")

    init {
        loadCategories()
        getAllProduct()
        getProductById("67cdd20838591fcf41a06e47")
        Log.d("ProductViewModel", _product.value.toString())
    }

    private fun loadCategories() {
        viewModelScope.launch {
            delay(2000)

//2000            _products.value = listOf(
//                Product(R.drawable.banner3, false, myColorHexList, "Product 0", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 1", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 2", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 3", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 4", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 5", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 6", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 7", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 8", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 9", 186.00, 126.00),
//                Product(R.drawable.banner3, false, myColorHexList, "Product 10", 186.00, 126.00),
//            )

            _isLoading.value = false
        }
    }

    fun getProductById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = productRepository.getProductById(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _product.value = it.product
                        Log.d("ProductViewModel", "Product loaded: ${it.product.productName}")
                    }
                } else {
                    Log.e("ProductViewModel", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun getAllProduct() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = _repository.getAllProducts()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _products.value = it.products
                        Log.d("dcm", "Danh sách sản phẩm: ${it.products}")
                    }
                } else {
                    Log.e("dcm_error", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: UnknownHostException) {
                Log.e("dcm_error", "Lỗi mạng: Không thể kết nối với máy chủ")
            } catch (e: SocketTimeoutException) {
                Log.e("dcm_error", "Lỗi mạng: Đã hết thời gian chờ")
            } catch (e: HttpException) {
                Log.e("dcm_error", "Lỗi HTTP: ${e.message}")
            } catch (e: JsonSyntaxException) {
                Log.e("dcm_error", "Lỗi dữ liệu: Invalid JSON response")
            } catch (e: Exception) {
                Log.e("dcm_error", "Lỗi chung: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}