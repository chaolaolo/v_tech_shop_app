package com.datn.viettech_md_12.viewmodel

import AccountDetail
import ChangePasswordRequest
import LoginRequest
import RegisterRequest
import UpdateImageToAccountRequest
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.data.remote.ApiClient
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = ApiClient.userRepository
    val changePasswordState = mutableStateOf<String?>(null)

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
                                .putString("userId", userId)  // Lưu userId
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
                Log.d(
                    "dcm_debug_request",
                    "SignIn Request: username=${request.username}, password=${request.password}"
                )
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

                            val sharedPreferences =
                                context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit()
                                .putString("accessToken", token)
                                .putString("clientId", userId)
                                .putString("userId", userId)  // Lưu userId
                                .putString("fullname", body.result?.metadata?.account?.full_name)
                                .putString("email", body.result?.metadata?.account?.email)
                                .putString("profile_image", body.result?.metadata?.profile_image)
                                .apply()
                            // Kiểm tra lại token sau khi lưu
                            val savedToken = sharedPreferences.getString("accessToken", null)
                            Log.d("dcm_debug_signin", "Token đã lưu: $savedToken")
                            val profileImage = body.result?.metadata?.profile_image
                            Log.d("dcm_debug_signin", "Ảnh đại diện nhận được: $profileImage")
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
    fun changePassword(
        context: Context,
        oldPassword: String,
        newPassword: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val clientId = sharedPreferences.getString("clientId", "") ?: ""
                val token = sharedPreferences.getString("accessToken", "") ?: ""

                val request = ChangePasswordRequest(
                    accountId = clientId,
                    oldPassword = oldPassword,
                    newPassword = newPassword
                )

                val response = userRepository.changePassword(clientId, token, request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    Log.d("ChangePassword", "Success: ${response.body()}")
                    onSuccess(response.body()?.message ?: "Đổi mật khẩu thành công!")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ChangePassword", "Failed: code=${response.code()}, errorBody=$errorBody")
                    onError("Đổi mật khẩu thất bại. Mật khẩu mới phải có ít nhất 6 ký tự!")
                }
            } catch (e: Exception) {
                onError("Lỗi kết nối: ${e.message}")
            }
        }
    }
    fun updateProfileImage(
        context: Context,
        imageId: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val accountId = sharedPreferences.getString("clientId", "") ?: ""

                val request = UpdateImageToAccountRequest(
                    accountId = accountId,
                    imageId = imageId
                )

                val response = userRepository.updateProfileImage(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    val newImageUrl = response.body()?.account?.profile_image?.url
                    Log.d("UpdateProfileImage", "Success: New URL = $newImageUrl")

                    // Lưu ảnh đại diện mới vào SharedPreferences (nếu muốn)
                    sharedPreferences.edit().putString("profile_image", newImageUrl).apply()

                    onSuccess("Cập nhật ảnh đại diện thành công!")
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("UpdateProfileImage", "Failed: code=${response.code()}, error=$error")
                    onError("Cập nhật ảnh đại diện thất bại!")
                }
            } catch (e: Exception) {
                Log.e("UpdateProfileImage", "Exception: ${e.message}")
                onError("Lỗi hệ thống: ${e.message}")
            }
        }

    }
    val accountDetail = mutableStateOf<AccountDetail?>(null)
    val accountDetailError = mutableStateOf<String?>(null)

    fun fetchAccountById(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = userRepository.getAccountById(id)
                if (response.isSuccessful && response.body()?.status == "success") {
                    accountDetail.value = response.body()?.data
                    onSuccess()
                } else {
                    onError("Không thể lấy thông tin tài khoản")
                }
            } catch (e: Exception) {
                onError("Lỗi: ${e.message}")
            }
        }
    }


//    fun changePassword(
//        request: ChangePasswordRequest,
//        context: Context
//    ) {
//        viewModelScope.launch {
//            try {
//                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//                val token = sharedPreferences.getString("accessToken", null)
//                val clientId = sharedPreferences.getString("clientId", null)
//
//                if (token.isNullOrEmpty() || clientId.isNullOrEmpty()) {
//                    changePasswordState.value = "Thiếu token hoặc clientId"
//                    return@launch
//                }
//
//                val response = userRepository.changePassword(token, clientId, request)
//                if (response.isSuccessful) {
//                    changePasswordState.value = response.body()?.message ?: "Đổi mật khẩu thành công"
//                } else {
//                    val errorBody = response.errorBody()?.string()
//                    changePasswordState.value = "Thất bại: $errorBody"
//                }
//            } catch (e: Exception) {
//                changePasswordState.value = "Lỗi mạng: ${e.localizedMessage}"
//            }
//        }
//    }
}
