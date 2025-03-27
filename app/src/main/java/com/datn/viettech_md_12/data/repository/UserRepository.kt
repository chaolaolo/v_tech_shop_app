import com.datn.viettech_md_12.data.interfaces.UserService
import retrofit2.Response

class UserRepository (
    private val apiService : UserService
){
    suspend fun signUp(request: RegisterRequest) : Response<RegisterResponse>{
        return apiService.signUp(request)
    }
    suspend fun signIn(request: LoginRequest) : Response<LoginResponse>{
        return apiService.signIn(request)
    }
}