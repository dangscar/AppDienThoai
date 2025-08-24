package com.nlhd.domain.repository

import com.nlhd.domain.entity.address.add.AddAddressRequest
import com.nlhd.domain.entity.address.add.AddAddressResponse
import com.nlhd.domain.entity.address.delete.DeleteAddressResponse
import com.nlhd.domain.entity.address.edit.EditAddressResponse
import com.nlhd.domain.entity.address.get.AddressResponse
import com.nlhd.domain.entity.address.get.SelectedAddressResponse
import com.nlhd.domain.entity.address.update.UpdateAddressRequest
import com.nlhd.domain.entity.address.update.UpdateAddressResponse
import com.nlhd.domain.resultWrapper.ResultWrapper

interface AddressRepository {
    suspend fun getAddress(token: String): ResultWrapper<AddressResponse>
    suspend fun addAddress(token: String, addAddressRequest: AddAddressRequest): ResultWrapper<AddAddressResponse>
    suspend fun selectedAddress(token: String, id: Int): ResultWrapper<SelectedAddressResponse>
    suspend fun editAddress(token: String, id: Int): ResultWrapper<EditAddressResponse>
    suspend fun updateAddress(token: String, updateAddressRequest: UpdateAddressRequest): ResultWrapper<UpdateAddressResponse>
    suspend fun deleteAddress(token: String, id: Int): ResultWrapper<DeleteAddressResponse>
}