package com.nlhd.domain.usecase.manageProduct

data class ManageProductUseCase(
    val addProduct: AddProduct,
    val loadProducts: LoadProducts,
    val getProduct: GetProduct,
    val updateProduct: UpdateProduct,
    val getVersionProducts: GetVersionProducts,
    val addVersionProduct: AddVersionProduct
)
