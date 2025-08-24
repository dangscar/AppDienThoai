package com.nlhd.data.repository

import android.util.Log
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.checkout.CheckoutOrderRequestDto
import com.nlhd.data.model.checkout.CheckoutOrderResponseDto
import com.nlhd.data.model.checkout.CheckoutResponseDto
import com.nlhd.domain.entity.checkout.CheckoutOrderRequest
import com.nlhd.domain.entity.checkout.CheckoutOrderResponse
import com.nlhd.domain.repository.CheckoutRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CheckoutRepositoryImp(
    private val ktor: HttpClient
): CheckoutRepository  {
    override suspend fun checkoutOrder(
        token: String,
        checkoutOrderRequest: CheckoutOrderRequest,
    ): ResultWrapper<CheckoutOrderResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/checkout/store") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(CheckoutOrderRequestDto(
                    customer_information_id = checkoutOrderRequest.customerInfoId,
                    payment_method = checkoutOrderRequest.paymentMethod,
                    selected_products = checkoutOrderRequest.selectedProducts,
                    total_amount = checkoutOrderRequest.totalAmount
                ))
            }.body<CheckoutOrderResponseDto>()

            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            Log.d("AAA", e.message.toString())
            ResultWrapper.Failure(e)

        }
    }
}