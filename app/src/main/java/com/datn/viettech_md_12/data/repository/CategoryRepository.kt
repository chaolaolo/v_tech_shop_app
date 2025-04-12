package com.datn.viettech_md_12.data.repository

import android.util.Log
import com.datn.viettech_md_12.data.interfaces.CategoryService
import com.datn.viettech_md_12.data.model.CategoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(
    private val categoryService: CategoryService
) {
    suspend fun getCategories(): List<CategoryModel>? {
        return withContext(Dispatchers.IO) {
            val response = categoryService.getCategories()
            Log.d("CategoryRepository", "Response Code: ${response.code()}")
            Log.d("CategoryRepository", "Raw Response: ${response.errorBody()?.string() ?: response.body()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("CategoryRepository", "Parsed Data: ${body?.categories}") // Đổi từ 'data' thành 'categories'
                body?.categories ?: emptyList() // Đổi từ 'data' thành 'categories'
            } else {
                Log.e("CategoryRepository", "Error: ${response.message()}")
                null
            }
        }
    }
}
