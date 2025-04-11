package com.datn.viettech_md_12.viewmodel

import FavoriteItem
import FavoriteRequest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.OrderModel
import com.datn.viettech_md_12.data.model.ProductModel
import com.datn.viettech_md_12.data.model.ProductResponse
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.data.remote.ApiClient.cartRepository
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
    val productResponse = MutableStateFlow<ProductResponse?>(null)
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val myColorHexList = listOf("FF1C1B1B", "FF08E488", "FF21D4B4")


    private val _favoriteProducts = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favoriteProducts: StateFlow<List<FavoriteItem>> = _favoriteProducts

    //hien thi don hang
    private val _orders = MutableStateFlow<List<OrderModel>>(emptyList())
    val orders: StateFlow<List<OrderModel>> = _orders

    // lưu variantId đã match
    private val _matchedVariantId = MutableStateFlow<String?>(null)
    val matchedVariantId: StateFlow<String?> = _matchedVariantId
    private val _matchedVariantPrice = MutableStateFlow<Double?>(null)
    val matchedVariantPrice: StateFlow<Double?> = _matchedVariantPrice

    private val _bottomSheetType = MutableStateFlow("")
    val bottomSheetType: StateFlow<String> = _bottomSheetType
    fun setBottomSheetType(type: String) {
        _bottomSheetType.value = type
    }
    init {
        loadCategories()
        getAllProduct()
//        getProductById("67cdd20838591fcf41a06e47")
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
                        productResponse.value = response.body()
                        Log.d("ProductViewModel", "Product loaded: ${it.product.productName}")
                        Log.d("ProductViewModel", "product: ${it.product}")
                        Log.d("ProductViewModel", "attributes: ${it.attributes}")
                        Log.d("ProductViewModel", "variants: ${it.variants}")
                        Log.d("ProductViewModel", "productResponse: $productResponse")
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
                        Log.d("lol", "Danh sách sản phẩm: ${it.products}")
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

    fun addToFavorites(productId: String, context: Context) {
        viewModelScope.launch {
            //lay token
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("accessToken", "")
            val clientId = sharedPreferences.getString("clientId", "")
            val editor = sharedPreferences.edit() // luu trang thai cua favorite khi load lai trang
            Log.d("dcm_debug", "Token: Bearer $token")
            Log.d("dcm_debug", "ClientId: $clientId")
            if (!token.isNullOrEmpty() && !clientId.isNullOrEmpty()) {
                val favoriteRequest = FavoriteRequest(productId = productId)
                Log.d(
                    "dcm_request",
                    "Sending request: $favoriteRequest"
                )
                try {
                    val response = _repository.addToFavorites(
                        favoriteRequest, token, clientId
                    )
                    Log.d("dcm_token_id", "Token used: Bearer $token")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            editor.putBoolean(productId, true) // Lưu trạng thái yêu thích của sản phẩm
                            editor.apply()
                            Log.d("dcm_success_fav", "Thêm vào danh sách yêu thích thành công: ${it.favorite}")
                            Toast.makeText(context, "Đã thêm vào danh sách yêu thích!", Toast.LENGTH_SHORT).show()
                            getFavoriteProducts(context)
                        }
                    } else {
                        Log.e(
                            "dcm_error_fav",
                            "Lỗi thêm vào danh sách yêu thích: ${response.code()} - ${response.message()}"
                        )
                        Log.e("dcm_error_fav", "Chi tiết lỗi: ${response.errorBody()?.string()}")

                        Toast.makeText(
                            context,
                            "Sản phẩm này đã có trong danh sách yêu thích!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Log.e("dcm_error_fav", "Lỗi khi thêm yêu thích: ${e.message}")
                }

            } else {
                Log.e("dcm_error_fav", "Token hoặc UserId không tồn tại trong SharedPreferences")
            }
        }
    }

    fun addProductToCart(
        productId: String,
        variantId: String,
        quantity: Int,
        context:Context,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("accessToken", "")
            val userId = sharedPreferences.getString("clientId", "")
            Log.d("CartViewModel", "token $token")
            Log.d("CartViewModel", "token $userId")
//            _isLoading.value = true
            try {
                val response = cartRepository.addToCart(
                    token = token ?: "",
                    userId = userId ?: "",
                    productId = productId,
                    detailsVariantId = variantId,
                    quantity = quantity
                )

                if (response.isSuccessful) {
                    // Cập nhật lại giỏ hàng sau khi thêm sản phẩm thành công
                    onSuccess()
                    Log.d("CartViewModel", "Add to cart success")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("CartViewModel", "Add to cart failed: $errorMsg")
                    onError(errorMsg)
                }
            } catch (e: UnknownHostException) {
                val errorMsg = "Lỗi mạng: Không thể kết nối với máy chủ"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } catch (e: SocketTimeoutException) {
                val errorMsg = "Lỗi mạng: Đã hết thời gian chờ"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } catch (e: HttpException) {
                val errorMsg = "Lỗi HTTP: ${e.message()}"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } catch (e: JsonSyntaxException) {
                val errorMsg = "Lỗi dữ liệu: Invalid JSON response"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Lỗi không xác định"
                Log.e("CartViewModel", errorMsg, e)
                onError(errorMsg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getFavoriteProducts(context: Context) {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("accessToken", "")
            val clientId = sharedPreferences.getString("clientId", "")

            if (!token.isNullOrEmpty() && !clientId.isNullOrEmpty()) {
                try {
                    _isLoading.value = true
                    val response = _repository.getFavoriteProducts(token, clientId)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _favoriteProducts.value = it.favorites
                            Log.d("dcm_success_fav_list", "Danh sách sản phẩm yêu thích: ${it.favorites}")
                        }
                    } else {
                        Log.e("dcm_error_fav", "Lỗi lấy danh sách yêu thích: ${response.code()} - ${response.message()}")
                    }
                } catch (e: UnknownHostException) {
                    Log.e("dcm_error_fav", "Lỗi mạng: Không thể kết nối với máy chủ")
                } catch (e: SocketTimeoutException) {
                    Log.e("dcm_error_fav", "Lỗi mạng: Đã hết thời gian chờ")
                } catch (e: HttpException) {
                    Log.e("dcm_error_fav", "Lỗi HTTP: ${e.message}")
                } catch (e: Exception) {
                    Log.e("dcm_error_fav", "Lỗi chung: ${e.message}")
                } finally {
                    _isLoading.value = false
                }
            } else {
                Log.e("dcm_error_fav", "Token hoặc ClientId không tồn tại")
            }
        }
    }
    fun removeFromFavorites(productId: String, context: Context) {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("accessToken", "")
            val clientId = sharedPreferences.getString("clientId", "")
            val apiKey = "c244dcd1532c91ab98a1c028e4f24f81457cdb2ac83e2ca422d36046fec84233589a4b51eda05e24d8871f73653708e3b13cf6dd1415a6330eaf6707217ef683" // Đảm bảo apiKey đúng
            val editor = sharedPreferences.edit()// luu trang thai cua favorite khi load lai trang

            if (!token.isNullOrEmpty() && !clientId.isNullOrEmpty()) {
                try {
                    val response = _repository.removeFromFavorites(productId, token, clientId, apiKey)
                    if (response.isSuccessful) {
                        getFavoriteProducts(context)

                        editor.putBoolean(productId, false) // Cập nhật trạng thái yêu thích của sản phẩm
                        editor.apply()
                    } else {
                        Log.e("dcm_error_remove", "Lỗi xóa sản phẩm yêu thích: ${response.code()} - ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("dcm_error_remove", "Lỗi khi xóa yêu thích: ${e.message}")
                }
            } else {
                Log.e("dcm_error_remove", "Token hoặc ClientId không tồn tại trong SharedPreferences")
            }
        }
    }
    //hien thi don hang
    fun getUserOrders(context: Context) {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("accessToken", "")
            val clientId = sharedPreferences.getString("clientId", "")
            val userId = sharedPreferences.getString("userId", "")  // bạn cần lưu userId sau khi login
            Log.d("dcm_debug_order", "Token:$token")
            Log.d("dcm_debug_order", "ClientId: $clientId")
            if (!token.isNullOrEmpty() && !clientId.isNullOrEmpty()) {
                try {
                    val response = _repository.getUserOrders(userId.toString(),token, clientId)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _orders.value = it.metadata.bills
                            Log.d("dcm_order", "Đơn hàng: ${it.metadata.bills}")
                        }
                    } else {
                        // Log chi tiết lỗi từ response.errorBody()
                        Log.e("dcm_order", "Lỗi lấy danh sách đơn hàng: ${response.code()} - ${response.message()}")
                        response.errorBody()?.let {
                            Log.e("dcm_order", "Chi tiết lỗi: ${it.string()}")
                        }
                    }
                } catch (e: UnknownHostException) {
                    Log.e("dcm_order", "Lỗi mạng: Không thể kết nối với máy chủ")
                } catch (e: SocketTimeoutException) {
                    Log.e("dcm_order", "Lỗi mạng: Đã hết thời gian chờ")
                } catch (e: HttpException) {
                    Log.e("dcm_order", "Lỗi HTTP: ${e.message}")
                    e.response()?.errorBody()?.let {
                        Log.e("dcm_order", "Chi tiết lỗi HTTP: ${it.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("dcm_order", "Lỗi chung: ${e.message}")
                }
            } else {
                Log.e("dcm_order", "Token hoặc ClientId không tồn tại")
            }
        }
    }

    fun matchVariant(productId: String, selectedAttributes: Map<String, String>) {
        viewModelScope.launch {
            try {
                // 1. Chuyển đổi attribute names sang ids
                val attributes = productResponse.value?.attributes ?: emptyList()
                val convertedAttrs = selectedAttributes.mapNotNull { (name, value) ->
                    attributes.find { it.name == name }?._id?.let { id -> id to value }
                }.toMap()

                Log.d("MatchVariant", "Converted attributes: $convertedAttrs")

                // 2. Thêm fallback nếu không có variant khớp
                val localVariantId = findMatchingVariantLocal(selectedAttributes)
                Log.d("MatchVariant", "Product ID: $productId")
                Log.d("MatchVariant", "Attributes: ${productResponse.value?.attributes}")
                Log.d("MatchVariant", "Selected Attributes (names): $selectedAttributes")
                Log.d("MatchVariant", "Converted Attributes (ids): $convertedAttrs")
                // 3. Gọi API
                val response = _repository.matchVariant(productId, convertedAttrs)
                _matchedVariantId.value = response.variant.id
                _matchedVariantPrice.value = response.variant.price
                Log.d("MatchVariant", "Matched Variant ID: ${response.variant.id}")
            } catch (e: Exception) {
                Log.e("MatchVariant", "Error matching variant", e)
                // Fallback: Tự match local nếu API fail
                _matchedVariantId.value = findMatchingVariantLocal(selectedAttributes)
                _matchedVariantPrice.value = findMatchingVariantPriceLocal(selectedAttributes)
                if (_matchedVariantId.value == null) {
                    Log.e("MatchVariant", "No matching variant found")
                }
            }
        }
    }

    private fun findMatchingVariantLocal(selectedAttributes: Map<String, String>): String? {
        val variants = productResponse.value?.variants ?: return null
        val attributes = productResponse.value?.attributes ?: return null

        return variants.firstOrNull { variant ->
            selectedAttributes.all { (name, value) ->
                variant.variantDetails.any { detail ->
                    val attributeId = attributes.find { it.name == name }?._id
                    detail.variantId == attributeId && detail.value == value
                }
            }
        }?.id
    }

    private fun findMatchingVariantPriceLocal(selectedAttributes: Map<String, String>): Double? {
        val variants = productResponse.value?.variants ?: return null
        val attributes = productResponse.value?.attributes ?: return null

        return variants.firstOrNull { variant ->
            selectedAttributes.all { (name, value) ->
                variant.variantDetails.any { detail ->
                    val attributeId = attributes.find { it.name == name }?._id
                    detail.variantId == attributeId && detail.value == value
                }
            }
        }?.price
    }


    fun filterValidOptions(
        selectedAttributes: Map<String, String>
    ): Map<String, Set<String>> {
        val variants = productResponse.value?.variants ?: return emptyMap()
        val attributes = productResponse.value?.attributes ?: return emptyMap()

        val attributeNameToIdMap = attributes.associate { it.name to it._id }
        val selectedAttrWithIds = selectedAttributes.mapKeys { (name, _) ->
            attributeNameToIdMap[name] ?: ""
        }.filterKeys { it.isNotEmpty() }

        val validOptions = mutableMapOf<String, MutableSet<String>>()

        attributes.forEach { attribute ->
            val attrId = attribute._id
            val attrName = attribute.name
            val validValues = mutableSetOf<String>()

            val partialSelection = selectedAttrWithIds.filterKeys { it != attrId }

            val possibleVariants = variants.filter { variant ->
                partialSelection.all { (id, value) ->
                    variant.variantDetails.any { it.variantId == id && it.value == value }
                } && variant.stock > 0
            }

            possibleVariants.forEach { variant ->
                variant.variantDetails.find { it.variantId == attrId }?.let { detail ->
                    validValues.add(detail.value)
                }
            }

            validOptions[attrName] = validValues
        }

        return validOptions
    }
}