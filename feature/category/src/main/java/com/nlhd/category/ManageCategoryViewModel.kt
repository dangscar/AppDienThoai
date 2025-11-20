package com.nlhd.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.manageCategory.CategoryResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.manageCategory.ManageCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageCategoryViewModel(
    private val manageCategoryUseCase: ManageCategoryUseCase
): ViewModel() {

    init {
        getCategory()
    }
    private var _manageCategoryState: MutableStateFlow<ManageCategoryState> = MutableStateFlow(ManageCategoryState.Loading)
    val manageCategoryState: StateFlow<ManageCategoryState> = _manageCategoryState.asStateFlow()

    private var _addCategoryState: MutableStateFlow<AddCategoryState> = MutableStateFlow(AddCategoryState.Idle)
    val addCategoryState: StateFlow<AddCategoryState> = _addCategoryState.asStateFlow()

    private var _name: MutableStateFlow<String> = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    fun setName(name: String) {
        _name.update { name }
    }

    fun getCategory() = viewModelScope.launch {
        manageCategoryUseCase.getCategory().let { result->
            when (result) {
                is ResultWrapper.Failure -> {
                    _manageCategoryState.update { ManageCategoryState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _manageCategoryState.update { ManageCategoryState.Success(result.value as CategoryResponse) }
                }
            }
        }
    }

    fun addCategory(token: String) = viewModelScope.launch {
        manageCategoryUseCase.addCategory(token, _name.value).let { result->
            when (result) {
                is ResultWrapper.Failure -> {
                    _addCategoryState.update { AddCategoryState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _addCategoryState.update { AddCategoryState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

    fun updateAddCategoryState() {
        _addCategoryState.update { AddCategoryState.Idle }
    }


}

sealed class ManageCategoryState {
    object Loading: ManageCategoryState()
    data class Success(val data: CategoryResponse): ManageCategoryState()
    data class Error(val message: String): ManageCategoryState()
}

sealed class AddCategoryState {
    object Idle: AddCategoryState()
    object Loading: AddCategoryState()
    data class Success(val data: MessageResponse): AddCategoryState()
    data class Error(val message: String): AddCategoryState()
}
