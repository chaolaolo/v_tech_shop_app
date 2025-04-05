package com.datn.viettech_md_12.data.repository

import com.datn.viettech_md_12.data.interfaces.CheckoutService
import com.datn.viettech_md_12.data.model.AddressModel
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.model.UpdateAddressRequest
import com.datn.viettech_md_12.data.remote.ApiClient.cartService
import retrofit2.Response

class CheckoutReporitory(
    private val checkoutService: CheckoutService,
) {
    //Get Address
    suspend fun getAddress(
        accountId: String,
    ): Response<AddressModel> {
        return checkoutService.getAddress(
            accountId = accountId
        )
    }

    suspend fun updateAddress(
        token: String,
        clientId: String,
        accountId: String,
        request: UpdateAddressRequest,
    ): Response<AddressModel> {
        return checkoutService.updateAddress(
            token = token,
            clientId = clientId,
            accountId = accountId,
            request = request
        )
    }
}