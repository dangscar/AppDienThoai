package com.nlhd.manage_product.EditProductScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.manageCategory.Category
import com.nlhd.domain.entity.manageCategory.CategoryResponse
import com.nlhd.domain.entity.manageProduct.EditProduct.EditProductResponse
import com.nlhd.domain.entity.manageProduct.UpdateProduct.UpdateProductRequest
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.manageCategory.ManageCategoryUseCase
import com.nlhd.domain.usecase.manageProduct.ManageProductUseCase
import com.nlhd.manage_product.AddProductScreen.ManageCategoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProductViewModel(
    private val manageCategoryUseCase: ManageCategoryUseCase,
    private val manageProductUseCase: ManageProductUseCase
): ViewModel() {

    private var _manageCategoryState: MutableStateFlow<ManageCategoryState> = MutableStateFlow(
        ManageCategoryState.Pending)
    val manageCategoryState: StateFlow<ManageCategoryState> = _manageCategoryState.asStateFlow()

    private var _isExpandCategory: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isExpandCategory: StateFlow<Boolean> = _isExpandCategory.asStateFlow()

    private var _category: MutableStateFlow<Category> = MutableStateFlow(Category(0, ""))
    val category: StateFlow<Category> = _category.asStateFlow()

    private var _editProductAction: MutableStateFlow<EditProductAction> = MutableStateFlow(
        EditProductAction(
            name = "Iphone 15",
            description = null,
            screenSize = "6.2",
            cpu = "Apple A17",
            camera = "32MP",
            battery = "3300",
            os = "IOS",
            categoryId = "0",
        )
    )
    val editProductAction = _editProductAction.asStateFlow()

    private var _updateProductState: MutableStateFlow<UpdateProductState> = MutableStateFlow(UpdateProductState.Pending)
    val updateProductState = _updateProductState.asStateFlow()

    fun setName(name: String) = _editProductAction.update { it.copy(name = name) }
    fun setDescription(description: String) = _editProductAction.update { it.copy(description = description) }
    fun setScreenSize(screenSize: String) = _editProductAction.update { it.copy(screenSize = screenSize) }
    fun setCpu(cpu: String) = _editProductAction.update { it.copy(cpu = cpu) }
    fun setCamera(camera: String) = _editProductAction.update { it.copy(camera = camera) }
    fun setBattery(battery: String) = _editProductAction.update { it.copy(battery = battery) }
    fun setOs(os: String) = _editProductAction.update { it.copy(os = os) }
    fun setExpandCategory(isExpand: Boolean) {
        _isExpandCategory.update { isExpand }
    }

    fun setCategoryAttr(category: Category) {
        _category.update { category }
        setCategory(category.id.toString())
    }

    fun setCategory(category: String) {
        _editProductAction.update { it.copy(categoryId = category) }
    }

    fun updateProduct(token: String) = viewModelScope.launch {
        val updateProductRequest = UpdateProductRequest(
            id = editProductAction.value.categoryId.toInt(),
            name = editProductAction.value.name,
            description = editProductAction.value.description ?: "",
            screenSize = editProductAction.value.screenSize.toDouble(),
            cpu = editProductAction.value.cpu,
            camera = editProductAction.value.camera,
            battery = editProductAction.value.battery.toInt(),
            os = editProductAction.value.os,
            categoryId = editProductAction.value.categoryId.toInt()
        )
        manageProductUseCase.updateProduct(token, updateProductRequest).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _updateProductState.update { UpdateProductState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _updateProductState.update { UpdateProductState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

    fun getProduct(token: String, id: Int) = viewModelScope.launch {
        manageProductUseCase.getProduct(id, token).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {

                }
                is ResultWrapper.Success<*> -> {
                    val data = (result.value as EditProductResponse).product
                    _editProductAction.update {
                        it.copy(
                            name = data.name,
                            description = data.description,
                            screenSize = data.screenSize.toString(),
                            cpu = data.cpu,
                            camera = data.camera,
                            battery = data.battery.toString(),
                            os = data.os,
                            categoryId = data.categoryId.toString()
                        )
                    }
                }
            }

        }
    }
    fun getCategory(token: String) {
        viewModelScope.launch {
            manageCategoryUseCase.getCategory(token).let { result ->
                when (result) {
                    is ResultWrapper.Failure -> {
                        _manageCategoryState.update { ManageCategoryState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        val categories = (result.value as CategoryResponse).categories
                        _manageCategoryState.update { ManageCategoryState.Success(result.value as CategoryResponse) }
                        if (categories.isNotEmpty()) {
                            setCategoryAttr(categories[0])
                        }

                    }
                }
            }
        }
    }
}

data class EditProductAction(
    val name: String,
    val description: String?,
    val screenSize: String,
    val cpu: String,
    val camera: String,
    val battery: String,
    val os: String,
    val categoryId: String
)

sealed class UpdateProductState {
    object Pending: UpdateProductState()
    data class Success(val data: MessageResponse): UpdateProductState()
    data class Error(val message: String): UpdateProductState()

}