package com.nlhd.data.repository

import android.content.Context
import android.net.Uri
import com.nlhd.core.utils.Utils
import com.nlhd.data.mapper.toDomain
import com.nlhd.data.model.manageProduct.ManageProductResponseDto
import com.nlhd.domain.entity.manageProduct.ManageProductResponse
import com.nlhd.domain.entity.manageProduct.UploadProduct
import com.nlhd.domain.repository.ManageProductRepository
import com.nlhd.domain.resultWrapper.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
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
}