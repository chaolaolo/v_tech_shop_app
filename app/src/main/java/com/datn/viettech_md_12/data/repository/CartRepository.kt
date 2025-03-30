package com.datn.viettech_md_12.data.repository

import com.datn.viettech_md_12.data.interfaces.CartService
import com.datn.viettech_md_12.data.model.CartModel
import com.datn.viettech_md_12.data.model.DeleteCartItemRequest
import com.datn.viettech_md_12.data.model.Metadata
import com.datn.viettech_md_12.data.model.UpdateCartRequest
import com.datn.viettech_md_12.data.remote.ApiClient.cartService
import retrofit2.Response


class CartRepository(
    private val apiService: CartService
) {
    suspend fun getCart(
        token: String,
        userId: String, userIdQuery: String,
    ): Response<CartModel> {
        return cartService.getCart(
            token = token,
            userId = userId,
            userIdQuery = userIdQuery
        )
    }

        suspend fun updateCartItem(
            token: String,
            userId: String,
            productId: String,
            variantId: String,
            newQuantity: Int,
        ): Response<CartModel> {
            val request = UpdateCartRequest(
                userId = userId,
                product = UpdateCartRequest.CartProduct(
                    productId = productId,
                    variantId = variantId,
                    quantity = newQuantity,
                )
            )
            return cartService.updateCart(
                token = token,
                clientId = userId,
                request = request
            )
        }

    suspend fun deleteCartItem(
        token: String,
        userId: String,
        productId: String,
        variantId: String
    ): Response<Unit> {
        val request = DeleteCartItemRequest(
            userId = userId,
            productId = productId,
            variantId = variantId
        )
        return cartService.deleteCartItem(
            token = token,
            userId = userId,
            request = request
        )
    }
}