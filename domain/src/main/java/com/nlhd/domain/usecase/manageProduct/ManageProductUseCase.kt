package com.nlhd.domain.usecase.manageProduct

data class ManageProductUseCase(
    val addProduct: AddProduct,
    val loadProducts: LoadProducts,
    val getProduct: GetProduct,
    val updateProduct: UpdateProduct,
    val getVersionProducts: GetVersionProducts,
    val addVersionProduct: AddVersionProduct,
    val updateVersionProduct: UpdateVersionProduct,
    val getColorProducts: GetColorProducts,
    val addColorProducts: AddColorProduct,
    val editColorProduct: EditColorProduct,
    val updateColorProduct: UpdateColorProduct
)
