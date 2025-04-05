data class RegisterRequest(
    val username: String,
    val full_name: String,
    val phone: String,
    val email: String,
    val password: String
)
// Request login
data class LoginRequest(
    val username: String,
    val password: String
)
// Data class cho response đăng ký
data class RegisterResponse(
    val code: Int,
    val message: String,
    val status: String,
    val metadata: Metadata
)

// Metadata chứa thông tin user và token
data class Metadata(
    val account: Account,
    val tokens: Tokens
)

// Thông tin tài khoản user
data class Account(
    val _id: String,
    val username: String,
    val full_name: String,
    val email: String,
    val phone: String,
    val status: String
)

// Token đăng nhập
data class Tokens(
    val accessToken: String,
    val refreshToken: String
)
data class LoginResponse(
    val result: LoginResult
)
data class LoginResult(
    val code: Int,
    val message: String,
    val status: String,
    val metadata: Metadata
)
//Change Password
data class ChangePasswordRequest(
    val accountId: String,
    val oldPassword: String,
    val newPassword: String
)

data class ChangePasswordResponse(
    val code: Int,
    val message: String,
    val status: String
)
