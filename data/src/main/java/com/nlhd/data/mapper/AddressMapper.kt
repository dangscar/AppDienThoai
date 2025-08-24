package com.nlhd.data.mapper

import com.nlhd.data.model.address.add.AddAddressRequestDto
import com.nlhd.data.model.address.add.AddAddressResponseDto
import com.nlhd.data.model.address.get.AddressResponseDto
import com.nlhd.data.model.address.get.CustomerInfomation
import com.nlhd.data.model.address.add.CustomerInfomationResponse
import com.nlhd.data.model.address.delete.DeleteAddressRequestDto
import com.nlhd.data.model.address.delete.DeleteAddressResponseDto
import com.nlhd.data.model.address.edit.EditAddressResponseDto
import com.nlhd.data.model.address.get.SelectedAddressResponseDto
import com.nlhd.data.model.address.update.UpdateAddressRequestDto
import com.nlhd.data.model.address.update.UpdateAddressResponseDto
import com.nlhd.domain.entity.address.add.AddAddressRequest
import com.nlhd.domain.entity.address.add.AddAddressResponse
import com.nlhd.domain.entity.address.delete.DeleteAddressRequest
import com.nlhd.domain.entity.address.delete.DeleteAddressResponse
import com.nlhd.domain.entity.address.edit.EditAddressResponse
import com.nlhd.domain.entity.address.get.AddressResponse
import com.nlhd.domain.entity.address.get.SelectedAddressResponse
import com.nlhd.domain.entity.address.update.UpdateAddressRequest
import com.nlhd.domain.entity.address.update.UpdateAddressResponse

//Get
fun AddressResponseDto.toDomain(addressResponseDto: AddressResponseDto): AddressResponse {
    return AddressResponse(
        customerInfomations = addressResponseDto.customerInfomations.map { it.toDomain(it) },
        message = addressResponseDto.message
    )
}

fun CustomerInfomation.toDomain(customerInfomation: CustomerInfomation): com.nlhd.domain.entity.address.get.CustomerInfomation {
    return com.nlhd.domain.entity.address.get.CustomerInfomation(
        address = customerInfomation.address,
        description = customerInfomation.description,
        fullName = customerInfomation.fullName,
        id = customerInfomation.id,
        isSelected = customerInfomation.isSelected,
        phoneNumber = customerInfomation.phoneNumber,
        userId = customerInfomation.user_id
    )

}

//Add

fun AddAddressRequestDto.toDomain(addAddressRequestDto: AddAddressRequestDto): AddAddressRequest {
    return AddAddressRequest(
        address = addAddressRequestDto.address,
        description = addAddressRequestDto.description,
        name = addAddressRequestDto.name,
        phone = addAddressRequestDto.phone
    )
}

fun AddAddressResponseDto.toDomain(addAddressResponseDto: AddAddressResponseDto): AddAddressResponse {
    return AddAddressResponse(
        customerInfomation = addAddressResponseDto.customerInfomation.toDomain(addAddressResponseDto.customerInfomation),
        message = addAddressResponseDto.message
    )
}

fun CustomerInfomationResponse.toDomain(customerInfomationResponse: CustomerInfomationResponse): com.nlhd.domain.entity.address.add.CustomerInfomationResponse {
    return com.nlhd.domain.entity.address.add.CustomerInfomationResponse(
        address = customerInfomationResponse.address,
        description = customerInfomationResponse.description,
        fullName = customerInfomationResponse.fullName,
        id = customerInfomationResponse.id,
        phoneNumber = customerInfomationResponse.phoneNumber,
        user_id = customerInfomationResponse.user_id
    )
}

//Select
fun SelectedAddressResponseDto.toDomain(selectedAddressResponseDto: SelectedAddressResponseDto): SelectedAddressResponse {
    return SelectedAddressResponse(
        message = selectedAddressResponseDto.message
    )

}

//Edit

fun EditAddressResponseDto.toDomain(editAddressResponseDto: EditAddressResponseDto): EditAddressResponse {
    return EditAddressResponse(
        message = editAddressResponseDto.message,
        customerInformation = editAddressResponseDto.customerInformation.toDomain(editAddressResponseDto.customerInformation)
    )
}

fun com.nlhd.data.model.address.edit.CustomerInformation.toDomain(customerInformation: com.nlhd.data.model.address.edit.CustomerInformation): com.nlhd.domain.entity.address.edit.CustomerInformation {
    return com.nlhd.domain.entity.address.edit.CustomerInformation(
        address = customerInformation.address,
        description = customerInformation.description,
        fullName = customerInformation.fullName,
        id = customerInformation.id,
        phoneNumber = customerInformation.phoneNumber,
        isSelected = customerInformation.isSelected
    )
}

//Update

fun UpdateAddressRequestDto.toDomain(updateAddressRequestDto: UpdateAddressRequestDto): UpdateAddressRequest {
    return UpdateAddressRequest(
        address = updateAddressRequestDto.address,
        description = updateAddressRequestDto.description,
        id = updateAddressRequestDto.id,
        name = updateAddressRequestDto.name,
        phone = updateAddressRequestDto.phone
    )
}

fun UpdateAddressResponseDto.toDomain(updateAddressResponseDto: UpdateAddressResponseDto): UpdateAddressResponse {
    return UpdateAddressResponse(
        message = updateAddressResponseDto.message
    )
}

//Delete

fun DeleteAddressRequestDto.toDomain(deleteAddressRequestDto: DeleteAddressRequestDto): DeleteAddressRequest {
    return DeleteAddressRequest(
        id = deleteAddressRequestDto.id
    )
}

fun DeleteAddressResponseDto.toDomain(deleteAddressResponseDto: DeleteAddressResponseDto): DeleteAddressResponse {
    return DeleteAddressResponse(
        message = deleteAddressResponseDto.message
    )
}