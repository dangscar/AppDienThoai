package com.nlhd.composestore.navigate

import com.nlhd.domain.entity.manageProduct.LoadProduct.Product
import kotlinx.serialization.Serializable

@Serializable
object LoadProduct

@Serializable
object AddProduct

@Serializable
data class EditProduct(
    val id: Int
)

@Serializable
data class LoadVersionProduct(
    val productId: Int,
    val productName: String
)

@Serializable
data class AddVersionProduct(
    val productId: Int
)

@Serializable
data class LoadColorProduct(
    val id: Int
)