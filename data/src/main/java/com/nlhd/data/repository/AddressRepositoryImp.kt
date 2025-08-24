package com.nlhd.data.repository

import android.util.Log
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.address.add.AddAddressRequestDto
import com.nlhd.data.model.address.add.AddAddressResponseDto
import com.nlhd.data.model.address.delete.DeleteAddressRequestDto
import com.nlhd.data.model.address.delete.DeleteAddressResponseDto
import com.nlhd.data.model.address.edit.EditAddressResponseDto
import com.nlhd.data.model.address.get.AddressResponseDto
import com.nlhd.data.model.address.get.SelectedAddressResponseDto
import com.nlhd.data.model.address.update.UpdateAddressRequestDto
import com.nlhd.data.model.address.update.UpdateAddressResponseDto
import com.nlhd.domain.entity.address.add.AddAddressRequest
import com.nlhd.domain.entity.address.add.AddAddressResponse
import com.nlhd.domain.entity.address.delete.DeleteAddressResponse
import com.nlhd.domain.entity.address.edit.EditAddressResponse
import com.nlhd.domain.entity.address.get.AddressResponse
import com.nlhd.domain.entity.address.get.SelectedAddressResponse
import com.nlhd.domain.entity.address.update.UpdateAddressRequest
import com.nlhd.domain.entity.address.update.UpdateAddressResponse
import com.nlhd.domain.repository.AddressRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AddressRepositoryImp(
    private val ktor: HttpClient
): AddressRepository {


    override suspend fun getAddress(token: String): ResultWrapper<AddressResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/dashboard/checkout/checkoutInfoShow") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<AddressResponseDto>()

            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun addAddress(
        token: String,
        addAddressRequest: AddAddressRequest
    ): ResultWrapper<AddAddressResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/dashboard/checkout/checkoutInfoStore") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(AddAddressRequestDto(
                    address = addAddressRequest.address,
                    description = addAddressRequest.description,
                    name = addAddressRequest.name,
                    phone = addAddressRequest.phone
                ))
            }.body<AddAddressResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun selectedAddress(
        token: String,
        id: Int
    ): ResultWrapper<SelectedAddressResponse> {
        return try {
            val responseDto = ktor.post(Utils.BASE_URL+"/api/dashboard/checkout/handleUseInfo/${id}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<SelectedAddressResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }

    }

    override suspend fun editAddress(
        token: String,
        id: Int,
    ): ResultWrapper<EditAddressResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/dashboard/checkout/checkoutInfoEdit/${id}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<EditAddressResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun updateAddress(
        token: String,
        updateAddressRequest: UpdateAddressRequest
    ): ResultWrapper<UpdateAddressResponse> {
        return try {
            val responseDto = ktor.patch(Utils.BASE_URL+"/api/dashboard/checkout/checkoutInfoUpdate") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(UpdateAddressRequestDto(
                    address = updateAddressRequest.address,
                    description = updateAddressRequest.description,
                    id = updateAddressRequest.id,
                    name = updateAddressRequest.name,
                    phone = updateAddressRequest.phone
                ))
            }.body<UpdateAddressResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun deleteAddress(
        token: String,
        id: Int
    ): ResultWrapper<DeleteAddressResponse> {
        return try {
            val responseDto = ktor.delete(Utils.BASE_URL+"/api/dashboard/checkout/checkoutInfoDelete") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(DeleteAddressRequestDto(
                    id = id
                ))
            }.body<DeleteAddressResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

}