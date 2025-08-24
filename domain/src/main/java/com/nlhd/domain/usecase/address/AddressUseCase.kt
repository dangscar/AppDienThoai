package com.nlhd.domain.usecase.address

data class AddressUseCase(
    val getAddress: GetAddress,
    val addAddress: AddAddress,
    val selectedAddress: SelectedAddress,
    val editAddress: EditAddress,
    val updateAddress: UpdateAddress,
    val deleteAddress: DeleteAddress
)
