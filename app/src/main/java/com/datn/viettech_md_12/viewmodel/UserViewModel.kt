package com.datn.viettech_md_12.viewmodel

import RegisterRequest
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.remote.ApiClient
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = ApiClient.userRepository

    fun signUp(
        request: RegisterRequest,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Debug dữ liệu trước khi gửi
                Log.d("dcm_debug_request", "SignUp Request: $request")

                val response = userRepository.signUp(request)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val token = body.metadata?.tokens?.accessToken
                        if (token != null) { // Chỉ lưu token khi nó tồn tại
                            Log.d("dcm_success_signup", "Đăng ký thành công: ${body.message}")
                            val sharedPreferences =
                                context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit().putString("accessToken", token).apply()
                            onSuccess()
                        } else {
                            onError("Token không tồn tại")
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Lỗi không xác định"
                    val errorMessage = "Lỗi: ${response.code()} - $errorBody"
                    Log.e("dcm_error_signup", errorMessage)

                    if (response.code() == 400 && errorBody.contains("Username or Email already exists")) {
                        onError("Email hoặc Username đã tồn tại. Vui lòng chọn thông tin khác!")
                    } else {
                        onError(errorMessage)
                    }
                }
            } catch (e: Exception) {
                Log.e("dcm_error_signup", "Exception: ${e.message}")
                onError("Đã xảy ra lỗi: ${e.message}")
            }
        }
    }
}
