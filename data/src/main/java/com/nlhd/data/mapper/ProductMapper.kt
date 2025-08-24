package com.nlhd.data.mapper

import com.nlhd.data.model.product.Color
import com.nlhd.data.model.product.Product
import com.nlhd.data.model.product.ProductResponseDto
import com.nlhd.data.model.product.Version

fun ProductResponseDto.toDomain(productResponse: ProductResponseDto): com.nlhd.domain.entity.product.ProductResponse {
    return com.nlhd.domain.entity.product.ProductResponse(
        message = productResponse.message,
        products = productResponse.products.map { it.toDomain(it) }
    )
}

fun Product.toDomain(product: Product): com.nlhd.domain.entity.product.Product {
    return com.nlhd.domain.entity.product.Product(
        battery = product.battery,
        camera = product.camera,
        category_id = product.category_id,
        colors = product.colors.map { it.toDomain(it) },
        cpu = product.cpu,
        created_at = product.created_at,
        deleted_at = product.deleted_at,
        description = product.description,
        id = product.id,
        name = product.name,
        os = product.os,
        screenSize = product.screenSize,
        updated_at = product.updated_at,
        versions = product.versions.map { it.toDomain(it) }

    )
}

fun Color.toDomain(color: Color): com.nlhd.domain.entity.product.Color {
    return com.nlhd.domain.entity.product.Color(
        id = color.id,
        image = color.image,
        price = color.price,
        status = color.status
    )
}

fun Version.toDomain(version: Version): com.nlhd.domain.entity.product.Version {
    return com.nlhd.domain.entity.product.Version(
        id = version.id,
        product_id = version.product_id,
        ram = version.ram,
        storage = version.storage
    )
}