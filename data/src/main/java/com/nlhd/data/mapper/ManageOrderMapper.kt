package com.nlhd.data.mapper

import com.nlhd.data.model.manageOrder.ColorProduct
import com.nlhd.data.model.manageOrder.Data
import com.nlhd.data.model.manageOrder.GetOrderAdminResponseDto
import com.nlhd.data.model.manageOrder.OrderDetail
import com.nlhd.data.model.manageOrder.Product
import com.nlhd.data.model.manageOrder.Version
import com.nlhd.domain.entity.manageOrder.GetOrderAdminResponse
import com.nlhd.domain.entity.manageOrder.Order


fun GetOrderAdminResponseDto.toDomain(getOrderAdminResponseDto: GetOrderAdminResponseDto): GetOrderAdminResponse {
    return GetOrderAdminResponse(
        currentPage = getOrderAdminResponseDto.current_page,
        orders = getOrderAdminResponseDto.data.map { it.toDomain(it) }
    )
}

fun Data.toDomain(data: Data): Order {
    return Order(
        address = data.address,
        created_at = data.created_at,
        description = data.description,
        fullName = data.fullName,
        id = data.id,
        order_details = data.order_details.map { it.toDomain(it) },
        phoneNumber = data.phoneNumber,
        status = data.status,
        total_amount = data.total_amount,
        updated_at = data.updated_at,
        user_id = data.user_id
    )
}

fun OrderDetail.toDomain(orderDetail: OrderDetail): com.nlhd.domain.entity.manageOrder.OrderDetail {
    return com.nlhd.domain.entity.manageOrder.OrderDetail(
        color_product = orderDetail.color_product.toDomain(orderDetail.color_product),
        color_product_id = orderDetail.color_product_id,
        created_at = orderDetail.created_at,
        id = orderDetail.id,
        order_id = orderDetail.order_id,
        price = orderDetail.price,
        quantity = orderDetail.quantity,
        updated_at = orderDetail.updated_at
    )
}

fun ColorProduct.toDomain(colorProduct: ColorProduct): com.nlhd.domain.entity.manageOrder.ColorProduct {
    return com.nlhd.domain.entity.manageOrder.ColorProduct(
        created_at = colorProduct.created_at,
        id = colorProduct.id,
        image = colorProduct.image,
        name = colorProduct.name,
        price = colorProduct.price,
        status = colorProduct.status,
        updated_at = colorProduct.updated_at,
        value = colorProduct.value,
        version = colorProduct.version.toDomain(colorProduct.version),
        version_product_id = colorProduct.version_product_id
    )
}

fun Version.toDomain(version: Version): com.nlhd.domain.entity.manageOrder.Version {
    return com.nlhd.domain.entity.manageOrder.Version(
        created_at = version.created_at,
        id = version.id,
        product = version.product.toDomain(version.product),
        product_id = version.product_id,
        ram = version.ram,
        storage = version.storage,
        updated_at = version.updated_at
    )
}

fun Product.toDomain(product: Product): com.nlhd.domain.entity.manageOrder.Product {
    return com.nlhd.domain.entity.manageOrder.Product(
        battery = product.battery,
        camera = product.camera,
        category_id = product.category_id,
        cpu = product.cpu,
        created_at = product.created_at,
        description = product.description,
        id = product.id,
        name = product.name,
        os = product.os,
        screenSize = product.screenSize,
        updated_at = product.updated_at
    )
}
