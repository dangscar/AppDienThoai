package com.nlhd.data.repository

import android.util.Log
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.cart.AddCartRequestDto
import com.nlhd.data.model.cart.AddCartResponseDto
import com.nlhd.data.model.cart.CartResponseDto
import com.nlhd.data.model.checkout.CheckoutRequestDto
import com.nlhd.data.model.checkout.CheckoutResponseDto
import com.nlhd.domain.entity.cart.AddCartResponse
import com.nlhd.domain.entity.cart.CartResponse
import com.nlhd.domain.entity.checkout.CheckoutRequest
import com.nlhd.domain.entity.checkout.CheckoutResponse
import com.nlhd.domain.repository.CartRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class CartRepositoryImp(
    private val ktor: HttpClient
): CartRepository {
    override suspend fun getCart(token: String): ResultWrapper<CartResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/cart") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<CartResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun addCart(
        token: String,
        colorProductId: Int,
        quantity: Int,
        operator: Int,
    ): ResultWrapper<AddCartResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/cart/store") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(AddCartRequestDto(
                    color_product_id = colorProductId,
                    quantity = quantity,
                    operator = operator
                ))
            }.body<AddCartResponseDto>()
            val addCartResponse = responseDto.toDomain(responseDto)
            ResultWrapper.Success(addCartResponse)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun checkoutResponse(
        token: String,
        checkoutRequest: CheckoutRequest,
    ): ResultWrapper<CheckoutResponse> {
        return try {

            val responseDto = ktor.post(Utils.BASE_URL+"/api/checkout") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(CheckoutRequestDto(
                    customer_info = checkoutRequest.customer_info,
                    selected_products = checkoutRequest.selected_products
                ))
            }.body<CheckoutResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            Log.d("AAA", e.toString())
            ResultWrapper.Failure(e)
        }
    }
}