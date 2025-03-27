package com.datn.viettech_md_12.data.interfaces

import RegisterRequest
import RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {
    @Headers(
        "authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NWU0YTIwMWQ0YTFkNmI4N2U0ZTNmMTEiLCJlbWFpbCI6ImFkbWluMDFAZXhhbXBsZS5jb20iLCJpYXQiOjE3NDI3NDQ1OTYsImV4cCI6MTc0MjkxNzM5Nn0.6p2WK5XZEUgnt04nSvnubUYVzuiZ9hfAjNvkWsE_Edg",
        "x-api-key: c244dcd1532c91ab98a1c028e4f24f81457cdb2ac83e2ca422d36046fec84233589a4b51eda05e24d8871f73653708e3b13cf6dd1415a6330eaf6707217ef683",
        "x-client-id: 65e4a201d4a1d6b87e4e3f11"
    )
    @POST("access/customer/signup")
    suspend fun signUp(@Body request : RegisterRequest) : Response<RegisterResponse>

}