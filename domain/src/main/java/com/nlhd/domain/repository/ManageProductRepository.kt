package com.nlhd.domain.repository

import android.content.Context
import android.net.Uri
import androidx.paging.PagingData
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.manageProduct.LoadProduct.Product
import com.nlhd.domain.entity.manageProduct.AddProduct.ManageProductResponse
import com.nlhd.domain.entity.manageProduct.AddProduct.UploadProduct
import com.nlhd.domain.entity.manageProduct.EditProduct.EditProductResponse
import com.nlhd.domain.entity.manageProduct.LoadVersionProduct.LoadVersionProductResponse
import com.nlhd.domain.entity.manageProduct.UpdateProduct.UpdateProductRequest
import com.nlhd.domain.entity.manageProduct.UploadVersionProduct.UploadVersionProduct
import com.nlhd.domain.resultWrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ManageProductRepository {
    suspend fun uploadImage(context: Context, uri: Uri): String?
    suspend fun addProduct(uploadProduct: UploadProduct, token: String): ResultWrapper<ManageProductResponse>
    fun getProducts(token: String, search: String): Flow<PagingData<Product>>
    suspend fun getProduct(id: Int, token: String): ResultWrapper<EditProductResponse>
    suspend fun updateProduct(token: String, updateProductRequest: UpdateProductRequest): ResultWrapper<MessageResponse>
    suspend fun getVersionProducts(token: String, productId: Int): ResultWrapper<LoadVersionProductResponse>
    suspend fun addVersionProduct(uploadVersionProduct: UploadVersionProduct, token: String): ResultWrapper<MessageResponse>
}