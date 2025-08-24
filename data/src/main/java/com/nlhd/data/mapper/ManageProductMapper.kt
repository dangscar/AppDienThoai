package com.nlhd.data.mapper

import com.nlhd.data.model.manageProduct.ColorProduct
import com.nlhd.data.model.manageProduct.ManageProductResponseDto
import com.nlhd.data.model.manageProduct.Product
import com.nlhd.data.model.manageProduct.VersionProduct
import com.nlhd.domain.entity.manageProduct.ManageProductResponse

fun ManageProductResponseDto.toDomain(manageProductResponseDto: ManageProductResponseDto): ManageProductResponse {
    return ManageProductResponse(
        colorProduct = color_product.toDomain(color_product),
        message = message,
        product = product.toDomain(product),
        versionProduct = version_product.toDomain(version_product)
    )
}

fun Product.toDomain(product: Product): com.nlhd.domain.entity.manageProduct.Product {
    return com.nlhd.domain.entity.manageProduct.Product(
        battery = battery,
        camera = camera,
        categoryId = category_id,
        cpu = cpu,
        description = description,
        id = id,
        name = name,
        os = os,
        screenSize = screenSize
    )
}

fun ColorProduct.toDomain(colorProduct: ColorProduct): com.nlhd.domain.entity.manageProduct.ColorProduct {
    return com.nlhd.domain.entity.manageProduct.ColorProduct(
        id = id,
        image = image,
        name = name,
        price = price,
        value = value,
        versionProductId = version_product_id
    )
}

fun VersionProduct.toDomain(versionProduct: VersionProduct): com.nlhd.domain.entity.manageProduct.VersionProduct {
    return com.nlhd.domain.entity.manageProduct.VersionProduct(
        id = id,
        productId = product_id,
        ram = ram,
        storage = storage
    )
}