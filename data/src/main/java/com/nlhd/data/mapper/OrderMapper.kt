package com.nlhd.data.mapper

import com.nlhd.data.model.order.ColorProduct
import com.nlhd.data.model.order.Data
import com.nlhd.data.model.order.OrderDetail
import com.nlhd.data.model.order.OrderResponseDto
import com.nlhd.data.model.order.Product
import com.nlhd.data.model.order.Version
import com.nlhd.domain.entity.order.OrderResponse

fun OrderResponseDto.toDomain(orderResponseDto: OrderResponseDto): OrderResponse {
    return OrderResponse(
        data = orderResponseDto.data.map { it.toDomain(it) }
    )
}

fun Data.toDomain(data: Data): com.nlhd.domain.entity.order.Data {
    return com.nlhd.domain.entity.order.Data(
        address = data.address,
        description = data.description,
        fullName = data.fullName,
        id = data.id,
        order_details = data.order_details.map { it.toDomain(it) },
        phoneNumber = data.phoneNumber,
        status = data.status,
        total_amount = data.total_amount
    )
}

fun OrderDetail.toDomain(orderDetail: OrderDetail): com.nlhd.domain.entity.order.OrderDetail {
    return com.nlhd.domain.entity.order.OrderDetail(
        color_product = orderDetail.color_product.toDomain(orderDetail.color_product),
        id = orderDetail.id,
        price = orderDetail.price,
        quantity = orderDetail.quantity
    )
}

fun ColorProduct.toDomain(colorProduct: ColorProduct): com.nlhd.domain.entity.order.ColorProduct {
    return com.nlhd.domain.entity.order.ColorProduct(
        id = colorProduct.id,
        image = colorProduct.image,
        name = colorProduct.name,
        price = colorProduct.price,
        status = colorProduct.status,
        value = colorProduct.value,
        version = colorProduct.version.toDomain(colorProduct.version)
    )
}

fun Version.toDomain(version: Version): com.nlhd.domain.entity.order.Version {
    return com.nlhd.domain.entity.order.Version(
        id = version.id,
        product = version.product.toDomain(version.product),
        ram = version.ram,
        storage = version.storage
    )
}

fun Product.toDomain(product: Product): com.nlhd.domain.entity.order.Product {
    return com.nlhd.domain.entity.order.Product(
        battery = product.battery,
        camera = product.camera,
        cpu = product.cpu,
        description = product.description,
        id = product.id,
        name = product.name,
        os = product.os,
        screenSize = product.screenSize
    )
}