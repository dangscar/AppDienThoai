package com.nlhd.domain.repository

import android.content.Context
import android.net.Uri
import com.nlhd.domain.entity.manageProduct.ManageProductResponse
import com.nlhd.domain.entity.manageProduct.UploadProduct
import com.nlhd.domain.resultWrapper.ResultWrapper

interface ManageProductRepository {
    suspend fun uploadImage(context: Context, uri: Uri): String?
    suspend fun addProduct(uploadProduct: UploadProduct, token: String): ResultWrapper<ManageProductResponse>
}