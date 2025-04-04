package com.datn.viettech_md_12.viewmodel

import LoginRequest
import RegisterRequest
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.remote.ApiClient
import kotlinx.coroutines.launch
import java.net.UnknownHostException

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
                Log.d("dcm_debug_request", "SignUp Request: $request")
                val response = userRepository.signUp(request)
                // Log toàn bộ response body
                Log.d("dcm_debug_response", "Response Code: ${response.code()}")
                Log.d("dcm_debug_response", "Response Body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val token = body.metadata?.tokens?.accessToken
                        val userId = body.metadata.account._id  // Lấy userId (_id) từ response
                        Log.d("dcm_id", "signUp: $userId")
                        if (token != null) { // Chỉ lưu token khi nó tồn tại
                            Log.d("dcm_success_signup", "Đăng ký thành công - Token: $token")
                            val sharedPreferences =
                                context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit()
                                .putString("accessToken", token)
                                .putString("clientId", userId)  // Lưu userId
                                .apply()
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

    fun signIn(
        request: LoginRequest,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("dcm_debug_request", "SignIn Request: username=${request.username}, password=${request.password}")
                val response = userRepository.signIn(request)

                // Log thông tin response
                Log.d("dcm_debug_response", "Response Code: ${response.code()}")
                Log.d("dcm_debug_response", "Response Body: ${response.body()}")
                Log.d("dcm_debug_response", "Error Body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val token = body.result?.metadata?.tokens?.accessToken
                        val userId = body.result?.metadata?.account?._id

                        Log.d("dcm_debug_signin", "Token nhận được: $token")
                        Log.d("dcm_id", "UserId nhận được: $userId")

                        if (!token.isNullOrEmpty()) {
                            Log.d("dcm_success_signin", "Đăng nhập thành công!")

                            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit()
                                .putString("accessToken", token)
                                .putString("clientId", userId)
                                .apply()

                            // Kiểm tra lại token sau khi lưu
                            val savedToken = sharedPreferences.getString("accessToken", null)
                            Log.d("dcm_debug_signin", "Token đã lưu: $savedToken")

                            onSuccess()
                        } else {
                            onError("Lỗi hệ thống: Không nhận được token từ server!")
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Lỗi không xác định"
                    Log.e("dcm_error_login", "Lỗi đăng nhập: ${response.code()} - $errorBody")

                    // Xử lý lỗi tùy vào mã phản hồi từ server
                    when {
                        response.code() == 401 && errorBody.contains("Invalid credentials") -> {
                            onError("Sai tên đăng nhập hoặc mật khẩu!")
                        }
                        response.code() == 500 -> {
                            onError("Lỗi máy chủ! Vui lòng thử lại sau.")
                        }
                        else -> {
                            onError("Đăng nhập thất bại")
                        }
                    }
                }
            } catch (e: UnknownHostException) {
                // Lỗi mất mạng, không kết nối được đến server
                Log.e("dcm_error_login", "Không có kết nối mạng: ${e.message}")
                onError("Kiểm tra kết nối mạng và thử lại!")
            } catch (e: Exception) {
                Log.e("dcm_error_login", "Lỗi ngoại lệ: ${e.message}")
                onError("Đã xảy ra lỗi: ${e.message}")
            }
        }
    }

}
