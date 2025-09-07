package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.entity.manageProduct.UploadVersionProduct.UploadVersionProduct
import com.nlhd.domain.repository.ManageProductRepository

class AddVersionProduct(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(uploadVersionProduct: UploadVersionProduct, token: String) = repository.addVersionProduct(uploadVersionProduct, token)
}