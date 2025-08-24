package com.nlhd.data.mapper

import com.nlhd.data.model.cart.AddCartRequestDto
import com.nlhd.data.model.cart.AddCartResponseDto
import com.nlhd.data.model.cart.CartItem
import com.nlhd.data.model.cart.CartResponseDto
import com.nlhd.data.model.cart.ColorProduct
import com.nlhd.data.model.cart.CustomerInformation
import com.nlhd.data.model.cart.Product
import com.nlhd.data.model.cart.Version
import com.nlhd.domain.entity.cart.CartResponse

fun CartResponseDto.toDomain(cartResponseDto: CartResponseDto): CartResponse {
    return CartResponse(
        cartItems = cartResponseDto.cartItems.map { it.toDomain(it) },
        message = cartResponseDto.message,
        totalCost = cartResponseDto.totalCost,
        customerInformation = cartResponseDto.customerInformation?.toDomain(customerInformation!!)

    )
}

fun CartItem.toDomain(cartItem: CartItem): com.nlhd.domain.entity.cart.CartItem {
    return com.nlhd.domain.entity.cart.CartItem(
        cart_id = cartItem.cart_id,
        color_product = cartItem.color_product.toDomain(color_product),
        color_product_id = cartItem.color_product_id,
        id = cartItem.id,
        price = cartItem.price,
        quantity = cartItem.quantity
    )
}

fun ColorProduct.toDomain(colorProduct: ColorProduct): com.nlhd.domain.entity.cart.ColorProduct {
    return com.nlhd.domain.entity.cart.ColorProduct(
        id = colorProduct.id,
        image = colorProduct.image,
        name = colorProduct.name,
        price = colorProduct.price,
        product = colorProduct.product.toDomain(product),
        version = colorProduct.version.toDomain(version),
        version_product_id = colorProduct.version_product_id
    )
}

fun Product.toDomain(product: Product): com.nlhd.domain.entity.cart.Product {
    return com.nlhd.domain.entity.cart.Product(
        battery = product.battery,
        camera = product.camera,
        category_id = product.category_id,
        cpu = product.cpu,
        description = product.description,
        id = product.id,
        name = product.name,
        os = product.os,
        screenSize = product.screenSize
    )
}

fun Version.toDomain(version: Version): com.nlhd.domain.entity.cart.Version {
    return com.nlhd.domain.entity.cart.Version(
        created_at = version.created_at,
        deleted_at = version.deleted_at,
        id = version.id,
        product_id = version.product_id,
        ram = version.ram,
        storage = version.storage,
        updated_at = version.updated_at
    )

}

fun CustomerInformation.toDomain(customerInformation: CustomerInformation): com.nlhd.domain.entity.cart.CustomerInformation {
    return com.nlhd.domain.entity.cart.CustomerInformation(
        id = customerInformation.id,
        fullName = customerInformation.fullName,
        phoneNumber = customerInformation.phoneNumber,
        address = customerInformation.address,
        description = customerInformation.description,
        isSelected = customerInformation.isSelected,
        user_id = customerInformation.user_id,
        deleted_at = customerInformation.deleted_at,
        created_at = customerInformation.created_at,
        updated_at = customerInformation.updated_at
    )

}


//CART
fun AddCartRequestDto.toDomain(addCartDto: AddCartRequestDto): com.nlhd.domain.entity.cart.AddCartRequest {
    return com.nlhd.domain.entity.cart.AddCartRequest(
        colorProductId = addCartDto.color_product_id,
        operator = addCartDto.operator,
        quantity = addCartDto.quantity
    )
}

fun AddCartResponseDto.toDomain(addCartDto: AddCartResponseDto): com.nlhd.domain.entity.cart.AddCartResponse {
    return com.nlhd.domain.entity.cart.AddCartResponse(
        message = addCartDto.message
    )
}
