package com.nlhd.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.manageCategory.CategoryResponse
import com.nlhd.domain.entity.product.Product
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.manageCategory.ManageCategoryUseCase
import com.nlhd.domain.usecase.product.ProductUseCase
import com.nlhd.keystore.HistorySearchManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val productUseCase: ProductUseCase,
    private val manageCategoryUseCase: ManageCategoryUseCase
): ViewModel() {

    private var _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val trigger = MutableSharedFlow<String>(replay = 1,extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val products: Flow<PagingData<Product>> = trigger
        .flatMapLatest { keyword -> productUseCase.searchProducts(keyword) }
        .cachedIn(viewModelScope)
        .debounce(500)
        .distinctUntilChanged()

    private var _categorySearchState = MutableStateFlow<CategorySearchState>(CategorySearchState.Loading)
    val categorySearchState = _categorySearchState.asStateFlow()

    fun getCategory() = viewModelScope.launch {
        _categorySearchState.value = CategorySearchState.Loading
        manageCategoryUseCase.getCategory().let { result ->
            when(result) {
                is ResultWrapper.Failure -> {
                    _categorySearchState.update { CategorySearchState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _categorySearchState.update { CategorySearchState.Success(result.value as CategoryResponse) }
                }
            }
        }

    }

    init {
        getCategory()
    }

    fun onSearchClick() {
        viewModelScope.launch {
            trigger.emit(_query.value)
        }
    }

    fun setQuery(query: String) {
        _query.update { query }
    }

    fun getHistory(context: Context): Flow<List<String>> = HistorySearchManager.getHistory(context)

    fun addSearch(context: Context) = viewModelScope.launch {
        if (query.value.isNotEmpty()) {
            HistorySearchManager.addHistory(context, query.value)
        }

    }

    fun removeHistoryItem(context: Context, item: String) = viewModelScope.launch {
        HistorySearchManager.removeHistoryItem(context, item)
    }

    fun clearHistory(context: Context) = viewModelScope.launch {
        HistorySearchManager.clearHistory(context)
    }
}

sealed class CategorySearchState {
    object Loading: CategorySearchState()
    data class Success(val data: CategoryResponse): CategorySearchState()
    data class Error(val message: String): CategorySearchState()
}