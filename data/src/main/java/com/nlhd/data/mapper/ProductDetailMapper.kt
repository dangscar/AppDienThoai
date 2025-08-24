package com.nlhd.data.mapper

import com.nlhd.data.model.productDetail.Color
import com.nlhd.data.model.productDetail.Product
import com.nlhd.data.model.productDetail.ProductDetailResponseDto
import com.nlhd.data.model.productDetail.Version

fun ProductDetailResponseDto.toDomain(productDetailResponseDto: ProductDetailResponseDto): com.nlhd.domain.entity.productDetail.ProductDetailResponse {
    return com.nlhd.domain.entity.productDetail.ProductDetailResponse(
        message = productDetailResponseDto.message,
        product = productDetailResponseDto.product.toDomain(productDetailResponseDto.product)
    )
}

fun Product.toDomain(product: Product): com.nlhd.domain.entity.productDetail.Product {
    return com.nlhd.domain.entity.productDetail.Product(
        battery = product.battery,
        camera = product.camera,
        categoryId = product.category_id,
        cpu = product.cpu,
        description = product.description,
        id = product.id,
        name = product.name,
        os = product.os,
        screenSize = product.screenSize,
        versions = product.versions.map { it.toDomain(it) }
    )
}

fun Version.toDomain(version: Version): com.nlhd.domain.entity.productDetail.Version {
    return com.nlhd.domain.entity.productDetail.Version(
        colors = version.colors.map { it.toDomain(it) },
        id = version.id,
        productId = version.product_id,
        ram = version.ram,
        storage = version.storage
    )
}

fun Color.toDomain(color: Color): com.nlhd.domain.entity.productDetail.Color {
    return com.nlhd.domain.entity.productDetail.Color(
        id = color.id,
        image = color.image,
        name = color.name,
        price = color.price,
        value = color.value,
        status = color.status,
        versionId = color.version_product_id
    )
}

