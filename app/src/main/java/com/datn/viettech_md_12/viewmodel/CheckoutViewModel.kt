package com.datn.viettech_md_12.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.model.AddressModel
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.model.CheckoutModel
import com.datn.viettech_md_12.data.model.UpdateAddressRequest
import com.datn.viettech_md_12.data.remote.ApiClient
import com.datn.viettech_md_12.data.remote.ApiClient.cartRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CheckoutViewModel(application: Application) : ViewModel(){
    private val checkoutRepository = ApiClient.checkoutRepository
    private val _addressState = MutableStateFlow<Response<AddressModel>?>(null)
    val addressState: StateFlow<Response<AddressModel>?> get() = _addressState
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val sharedPreferences = application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val token: String? = sharedPreferences.getString("accessToken", null)
    private val userId: String? = sharedPreferences.getString("clientId", null)

    init {
        getAddress()
    }

    fun getAddress() {
        viewModelScope.launch {
            Log.d("CheckoutViewModel", "userId: $userId")
            Log.d("CheckoutViewModel", "token: $token")
            _isLoading.value = true
            try {
                val response = checkoutRepository.getAddress(
                    accountId = userId?:"",
                )
                if (response.isSuccessful) {
                    _addressState.value = response
                    response.body()?.let {
                        Log.d("dm", "Fetch Address Success: ${it}")
                    }
                } else {
                    Log.e("dm", "Fetch Address Failed: ${response.code()} - ${response.message()}")
                }
            } catch (e: UnknownHostException) {
                Log.e("dm_error", "Lỗi mạng: Không thể kết nối với máy chủ")
            } catch (e: SocketTimeoutException) {
                Log.e("dm_error", "Lỗi mạng: Đã hết thời gian chờ")
            } catch (e: HttpException) {
                Log.e("dm_error", "Lỗi HTTP: ${e.message()}")
            } catch (e: JsonSyntaxException) {
                Log.e("dm_error", "Lỗi dữ liệu: Invalid JSON response")
            } catch (e: Exception) {
                Log.e("dm_error", "Lỗi chung: ${e.message}", e)
            } finally {
                _isLoading.value = false // Kết thúc trạng thái loading
            }
        }
    }

    fun updateAddress(fullName: String, phone: String, address: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = UpdateAddressRequest(
                    full_name = fullName,
                    phone = phone,
                    address = address
                )

                val response = checkoutRepository.updateAddress(
                    token = token ?: "",
                    clientId = userId ?: "",
                    accountId = userId ?: "",
                    request = request
                )

                if (response.isSuccessful) {
                    _addressState.value = response
                    response.body()?.let {
                        Log.d("CheckoutViewModel", "Update Address Success: $it")
                        // Cập nhật lại thông tin địa chỉ sau khi update thành công
                        getAddress()
                    }
                } else {
                    Log.e("CheckoutViewModel", "Update Address Failed: ${response.code()} - ${response.message()}")
                    _addressState.value = response
                }
            } catch (e: UnknownHostException) {
                Log.e("CheckoutViewModel", "Network error: Cannot connect to server")
                _addressState.value = null
            } catch (e: SocketTimeoutException) {
                Log.e("CheckoutViewModel", "Network error: Timeout")
                _addressState.value = null
            } catch (e: HttpException) {
                Log.e("CheckoutViewModel", "HTTP error: ${e.message()}")
                _addressState.value = null
            } catch (e: JsonSyntaxException) {
                Log.e("CheckoutViewModel", "Data error: Invalid JSON response")
                _addressState.value = null
            } catch (e: Exception) {
                Log.e("CheckoutViewModel", "General error: ${e.message}", e)
                _addressState.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }


}


class CheckoutViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            return CheckoutViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}