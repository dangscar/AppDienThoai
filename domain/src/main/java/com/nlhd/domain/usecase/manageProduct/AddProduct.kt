package com.nlhd.domain.usecase.manageProduct

import com.nlhd.domain.entity.manageProduct.ManageProductResponse
import com.nlhd.domain.entity.manageProduct.UploadProduct
import com.nlhd.domain.repository.ManageProductRepository
import com.nlhd.domain.resultWrapper.ResultWrapper

class AddProduct(
    private val repository: ManageProductRepository
) {
    suspend operator fun invoke(uploadProduct: UploadProduct, token: String): ResultWrapper<ManageProductResponse> = repository.addProduct(uploadProduct = uploadProduct, token = token)
}