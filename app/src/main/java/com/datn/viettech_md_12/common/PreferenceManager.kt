package com.datn.viettech_md_12.common

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("accessToken", null)
    }

    fun getClientId(): String? {
        return sharedPreferences.getString("clientId", null)
    }

    fun saveFavoriteStatus(productId: String, isFavorite: Boolean) {
        sharedPreferences.edit().putBoolean(productId, isFavorite).apply()
    }
}
