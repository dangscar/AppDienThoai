package com.nlhd.manage_product.AddProductScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.manageCategory.Category
import com.nlhd.domain.entity.manageCategory.CategoryResponse
import com.nlhd.domain.entity.manageProduct.AddProduct.ManageProductResponse
import com.nlhd.domain.entity.manageProduct.AddProduct.UploadProduct
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.manageCategory.ManageCategoryUseCase
import com.nlhd.domain.usecase.manageProduct.ManageProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageProductViewModel(
    private val useCase: ManageProductUseCase,
    private val manageCategoryUseCase: ManageCategoryUseCase
): ViewModel() {


    private var _state: MutableStateFlow<ManageProductState> = MutableStateFlow(ManageProductState.Pending)
    val state: StateFlow<ManageProductState> = _state.asStateFlow()

    private var _manageCategoryState: MutableStateFlow<ManageCategoryState> = MutableStateFlow(ManageCategoryState.Pending)
    val manageCategoryState: StateFlow<ManageCategoryState> = _manageCategoryState.asStateFlow()

    private var _isExpand: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isExpand: StateFlow<Boolean> = _isExpand.asStateFlow()

    private var _isExpandCategory: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isExpandCategory: StateFlow<Boolean> = _isExpandCategory.asStateFlow()

    private var _category: MutableStateFlow<Category> = MutableStateFlow(Category(0, ""))
    val category: StateFlow<Category> = _category.asStateFlow()


    val statusList = listOf(
        "Còn hàng" to "in-stock",
        "Hết hàng" to "out-stock"
    )

    private var _isValidColor: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isValidColor: StateFlow<Boolean> = _isValidColor.asStateFlow()

    private var _manageProductAction: MutableStateFlow<ManageProductAction> = MutableStateFlow(
        ManageProductAction(
            name = "Iphone 15",
            price = "18000000",
            image = null,
            status = Pair(first = statusList.first().first, second = statusList.first().second),
            description = null,
            color = "Black",
            valueColor = "#000000",
            screenSize = "6.2",
            cpu = "Apple A17",
            ram = "6",
            storage = "256",
            camera = "32MP",
            battery = "3300",
            os = "IOS",
            category_id = "2"
        )
    )
    val manageProductAction: StateFlow<ManageProductAction> = _manageProductAction.asStateFlow()

    fun setCategoryAttr(category: Category) {
        _category.update { category }
        setCategory(category.id.toString())
    }

    fun setCategory(category: String) {
        _manageProductAction.update { it.copy(category_id = category) }
    }

    fun setOs(os: String) {
        _manageProductAction.update { it.copy(os = os) }
    }

    fun setBattery(battery: String) {
        _manageProductAction.update { it.copy(battery = battery) }
    }

    fun setCamera(camera: String) {
        _manageProductAction.update { it.copy(camera = camera) }
    }

    fun setStorage(storage: String) {
        _manageProductAction.update { it.copy(storage = storage) }
    }

    fun setRam(ram: String) {
        _manageProductAction.update { it.copy(ram = ram) }
    }

    fun setCpu(cpu: String) {
        _manageProductAction.update { it.copy(cpu = cpu) }
    }

    fun setScreenSize(screenSize: String) {
        _manageProductAction.update { it.copy(screenSize = screenSize) }
    }

    fun setValidColor(isValidColor: Boolean) {
        _isValidColor.update { isValidColor }
    }

    fun setColorValue(valueColor: String) {
        _manageProductAction.update { it.copy(valueColor = valueColor) }
    }

    fun setColor(color: String) {
        _manageProductAction.update { it.copy(color = color) }
    }

    fun setDescription(description: String?) {
        _manageProductAction.update { it.copy(description = description) }
    }

    fun setName(name: String) {
        _manageProductAction.update { it.copy(name = name) }
    }

    fun setPrice(price: String) {
        _manageProductAction.update { it.copy(price = price) }
    }

    fun setStatus(status: Pair<String, String>) {
        _manageProductAction.update {
            it.copy(status = status)
        }
        _isExpand.update { false }
    }

    fun setExpand(isExpand: Boolean) {
        _isExpand.update { isExpand }
    }

    fun setExpandCategory(isExpand: Boolean) {
        _isExpandCategory.update { isExpand }
    }

    fun uploadImage(uri: Uri) {
        _manageProductAction.update { it.copy(image = uri) }
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

    fun addProduct(context: Context,token: String) {
        val uploadProduct = UploadProduct(
            context = context,
            image = manageProductAction.value.image!!,
            name = manageProductAction.value.name,
            price = manageProductAction.value.price,
            status = manageProductAction.value.status.second,
            color = manageProductAction.value.color,
            value = manageProductAction.value.valueColor,
            category_id = manageProductAction.value.category_id,
            screenSize = manageProductAction.value.screenSize,
            cpu = manageProductAction.value.cpu,
            ram = manageProductAction.value.ram,
            storage = manageProductAction.value.storage,
            camera = manageProductAction.value.camera,
            battery = manageProductAction.value.battery,
            os = manageProductAction.value.os
        )
        viewModelScope.launch {
            useCase.addProduct(uploadProduct, token).let { result->
                when (result) {
                    is ResultWrapper.Failure -> {
                        _state.update { ManageProductState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _state.update { ManageProductState.Success(result.value as ManageProductResponse) }
                        _state.update { ManageProductState.Pending }
                    }
                }
            }
        }
    }

}

sealed class ManageCategoryState {
    object Pending: ManageCategoryState()
    object Loading: ManageCategoryState()
    data class Success(val data: CategoryResponse): ManageCategoryState()
    data class Error(val message: String): ManageCategoryState()
}

sealed class ManageProductState {
    object Pending: ManageProductState()
    object Loading: ManageProductState()
    data class Success(val data: ManageProductResponse): ManageProductState()
    data class Error(val message: String): ManageProductState()
}

data class ManageProductAction(
    val name: String,
    val price: String,
    val image: Uri?,
    val status: Pair<String, String>,
    val description: String?,
    val color: String,
    val valueColor: String,
    val screenSize: String,
    val cpu: String,
    val ram: String,
    val storage: String,
    val camera: String,
    val battery: String,
    val os: String,
    val category_id: String
)