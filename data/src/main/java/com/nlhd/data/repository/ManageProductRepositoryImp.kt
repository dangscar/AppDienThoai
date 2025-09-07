package com.nlhd.data.repository

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.Message.MessageResponseDto
import com.nlhd.data.model.manageProduct.AddProduct.ManageProductResponseDto
import com.nlhd.data.model.manageProduct.EditProduct.EditProductResponseDto
import com.nlhd.data.model.manageProduct.LoadVersionProduct.LoadVersionProductResponseDto
import com.nlhd.data.model.manageProduct.UpdateProduct.UpdateProductRequestDto
import com.nlhd.data.remote.LoadProductPagingSource
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.manageProduct.AddProduct.ManageProductResponse
import com.nlhd.domain.entity.manageProduct.AddProduct.UploadProduct
import com.nlhd.domain.entity.manageProduct.EditProduct.EditProductResponse
import com.nlhd.domain.entity.manageProduct.LoadProduct.Product
import com.nlhd.domain.entity.manageProduct.LoadVersionProduct.LoadVersionProductResponse
import com.nlhd.domain.entity.manageProduct.UpdateProduct.UpdateProductRequest
import com.nlhd.domain.entity.manageProduct.UploadVersionProduct.UploadVersionProduct
import com.nlhd.domain.repository.ManageProductRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

class ManageProductRepositoryImp(
    private val ktor: HttpClient
): ManageProductRepository  {
    override suspend fun uploadImage(
        context: Context,
        uri: Uri,
    ): String? {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val byteArray = inputStream.readBytes()
        inputStream.close()

        val headers = headersOf(
            HttpHeaders.ContentDisposition to listOf("form-data; name=\"image\"; filename=\"upload.jpg\""),
            HttpHeaders.ContentType to listOf("image/jpeg")
        )

        val multipartData = MultiPartFormDataContent(
            formData {
                append("image", byteArray, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"upload.jpg\"")
                })
            }
        )

        val response = ktor.post(Utils.BASE_URL+"/api/image") {
            setBody(multipartData)
        }
        return if (response.status.isSuccess()) {
            val body = response.bodyAsText()
            JSONObject(body).getString("imageUrl")
        } else {
            null
        }

    }

    override suspend fun addProduct(uploadProduct: UploadProduct, token: String): ResultWrapper<ManageProductResponse> {
        try {
            val contentResolver = uploadProduct.context.contentResolver
            val inputStream = contentResolver.openInputStream(uploadProduct.image) ?: return ResultWrapper.Failure(Exception("Không thể mở tệp hình ảnh"))
            val byteArray = inputStream.readBytes()
            inputStream.close()


            val multipartData = MultiPartFormDataContent(
                formData {
                    append("image", byteArray, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"upload.jpg\"")
                    })
                    append("name", uploadProduct.name)
                    append("price", uploadProduct.price)
                    append("status", uploadProduct.status)
                    append("color", uploadProduct.color)
                    append("value", uploadProduct.value)
                    append("category_id", uploadProduct.category_id)
                    append("screenSize", uploadProduct.screenSize)
                    append("cpu", uploadProduct.cpu)
                    append("ram", uploadProduct.ram)
                    append("storage", uploadProduct.storage)
                    append("battery", uploadProduct.battery)
                    append("camera", uploadProduct.camera)
                    append("os", uploadProduct.os)
                }
            )

            val responseDto = ktor.post(Utils.BASE_URL+"/api/product/store") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(multipartData)
            }.body<ManageProductResponseDto>()

            val response = responseDto.toDomain(responseDto)
            return ResultWrapper.Success(response)
        } catch (e: Exception) {
            return ResultWrapper.Failure(e)
        }

    }

    override fun getProducts(
        token: String,
        search: String
    ): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                LoadProductPagingSource(
                    ktor = ktor,
                    search = search,
                    token = token
                )
            }
        ).flow
    }

    override suspend fun getProduct(
        id: Int,
        token: String
    ): ResultWrapper<EditProductResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/product/edit/${id}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<EditProductResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun updateProduct(
        token: String,
        updateProductRequest: UpdateProductRequest
    ): ResultWrapper<MessageResponse> {
        return try {
            val responseDto = ktor.patch(Utils.BASE_URL+"/api/product/update") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateProductRequestDto(
                        id = updateProductRequest.id,
                        name = updateProductRequest.name,
                        battery = updateProductRequest.battery,
                        camera = updateProductRequest.camera,
                        category_id = updateProductRequest.categoryId,
                        cpu = updateProductRequest.cpu,
                        description = updateProductRequest.description,
                        os = updateProductRequest.os,
                        screenSize = updateProductRequest.screenSize
                    )
                )
            }.body<MessageResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun getVersionProducts(
        token: String,
        productId: Int
    ): ResultWrapper<LoadVersionProductResponse> {
        return try {
            val responseDto = ktor.get(Utils.BASE_URL+"/api/versionProduct/${productId}") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<LoadVersionProductResponseDto>()
            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun addVersionProduct(
        uploadVersionProduct: UploadVersionProduct,
        token: String
    ): ResultWrapper<MessageResponse> {
        return try {
            val contentResolver = uploadVersionProduct.context.contentResolver
            val inputStream = contentResolver.openInputStream(uploadVersionProduct.image) ?: return ResultWrapper.Failure(Exception("Không thể mở tệp hình ảnh"))
            val byteArray = inputStream.readBytes()
            inputStream.close()

            val multipartData = MultiPartFormDataContent(
                formData {
                    append("image", byteArray, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"upload.jpg\"")
                    })
                    append("price", uploadVersionProduct.price)
                    append("status", uploadVersionProduct.status)
                    append("color", uploadVersionProduct.color)
                    append("value", uploadVersionProduct.value)
                    append("ram", uploadVersionProduct.ram)
                    append("storage", uploadVersionProduct.storage)
                    append("product_id", uploadVersionProduct.productId)
                }
            )

            val responseDto = ktor.post(Utils.BASE_URL+"/api/versionProduct/store") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(multipartData)
            }.body<MessageResponseDto>()

            val response = responseDto.toDomain(responseDto)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }

}