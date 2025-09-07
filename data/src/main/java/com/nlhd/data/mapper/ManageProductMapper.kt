package com.nlhd.data.mapper

import com.nlhd.data.model.manageProduct.AddProduct.ColorProduct
import com.nlhd.data.model.manageProduct.LoadProduct.ProductsResponseDto
import com.nlhd.data.model.manageProduct.AddProduct.ManageProductResponseDto
import com.nlhd.data.model.manageProduct.AddProduct.Product
import com.nlhd.data.model.manageProduct.AddProduct.VersionProduct
import com.nlhd.data.model.manageProduct.EditProduct.EditProductResponseDto
import com.nlhd.data.model.manageProduct.LoadVersionProduct.LoadVersionProductResponseDto
import com.nlhd.data.model.manageProduct.UpdateProduct.UpdateProductRequestDto
import com.nlhd.domain.entity.manageProduct.LoadProduct.ProductsResponse
import com.nlhd.domain.entity.manageProduct.AddProduct.ManageProductResponse
import com.nlhd.domain.entity.manageProduct.EditProduct.EditProductResponse
import com.nlhd.domain.entity.manageProduct.LoadVersionProduct.LoadVersionProductResponse
import com.nlhd.domain.entity.manageProduct.UpdateProduct.UpdateProductRequest

fun ManageProductResponseDto.toDomain(manageProductResponseDto: ManageProductResponseDto): ManageProductResponse {
    return ManageProductResponse(
        colorProduct = color_product.toDomain(color_product),
        message = message,
        product = product.toDomain(product),
        versionProduct = version_product.toDomain(version_product)
    )
}

fun Product.toDomain(product: Product): com.nlhd.domain.entity.manageProduct.AddProduct.Product {
    return com.nlhd.domain.entity.manageProduct.AddProduct.Product(
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

fun ColorProduct.toDomain(colorProduct: ColorProduct): com.nlhd.domain.entity.manageProduct.AddProduct.ColorProduct {
    return com.nlhd.domain.entity.manageProduct.AddProduct.ColorProduct(
        id = id,
        image = image,
        name = name,
        price = price,
        value = value,
        versionProductId = version_product_id
    )
}

fun VersionProduct.toDomain(versionProduct: VersionProduct): com.nlhd.domain.entity.manageProduct.AddProduct.VersionProduct {
    return com.nlhd.domain.entity.manageProduct.AddProduct.VersionProduct(
        id = id,
        productId = product_id,
        ram = ram,
        storage = storage
    )
}

//Load Product

fun ProductsResponseDto.toDomain(productsResponseDto: ProductsResponseDto): ProductsResponse {
    return ProductsResponse(
        message = message,
        products = products.map { it.toDomain(it) }
    )
}

fun com.nlhd.data.model.manageProduct.LoadProduct.Product.toDomain(product: com.nlhd.data.model.manageProduct.LoadProduct.Product): com.nlhd.domain.entity.manageProduct.LoadProduct.Product {
    return com.nlhd.domain.entity.manageProduct.LoadProduct.Product(
        battery = battery,
        camera = camera,
        category_id = category_id,
        cpu = cpu,
        description = description,
        id = id,
        name = name,
        os = os,
        screenSize = screenSize,
        versions = versions.map { it.toDomain(it) },
        colors = colors.map { it.toDomain(it) }
    )
}

fun com.nlhd.data.model.manageProduct.LoadProduct.Version.toDomain(version: com.nlhd.data.model.manageProduct.LoadProduct.Version): com.nlhd.domain.entity.manageProduct.LoadProduct.Version {
    return com.nlhd.domain.entity.manageProduct.LoadProduct.Version(
        id = id,
        product_id = product_id,
        ram = ram,
        storage = storage
    )
}

fun com.nlhd.data.model.manageProduct.LoadProduct.Color.toDomain(color: com.nlhd.data.model.manageProduct.LoadProduct.Color): com.nlhd.domain.entity.manageProduct.LoadProduct.Color {
    return com.nlhd.domain.entity.manageProduct.LoadProduct.Color(
        id = id,
        image = image,
        price = price
    )
}

//EditProduct

fun EditProductResponseDto.toDomain(editProductResponseDto: EditProductResponseDto): EditProductResponse {
    return EditProductResponse(
        message = message,
        product = product.toDomain(product)
    )
}

fun com.nlhd.data.model.manageProduct.EditProduct.Product.toDomain(product: com.nlhd.data.model.manageProduct.EditProduct.Product): com.nlhd.domain.entity.manageProduct.EditProduct.Product {
    return com.nlhd.domain.entity.manageProduct.EditProduct.Product(
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

//UpdateProduct

fun UpdateProductRequestDto.toDomain(updateProductRequestDto: UpdateProductRequestDto): UpdateProductRequest {
    return UpdateProductRequest(
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

//Load Version Product

fun LoadVersionProductResponseDto.toDomain(loadVersionProductResponseDto: LoadVersionProductResponseDto): LoadVersionProductResponse {
    return LoadVersionProductResponse(
        message = message,
        versions = versions.map { it.toDomain(it) }
    )
}

fun com.nlhd.data.model.manageProduct.LoadVersionProduct.Version.toDomain(version: com.nlhd.data.model.manageProduct.LoadVersionProduct.Version): com.nlhd.domain.entity.manageProduct.LoadVersionProduct.Version {
    return com.nlhd.domain.entity.manageProduct.LoadVersionProduct.Version(
        id = id,
        productId = product_id,
        ram = ram,
        storage = storage
    )
}
