package com.nlhd.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.product.Product
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
    private val productUseCase: ProductUseCase
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
        HistorySearchManager.addHistory(context, query.value)
    }

    fun removeHistoryItem(context: Context, item: String) = viewModelScope.launch {
        HistorySearchManager.removeHistoryItem(context, item)
    }

    fun clearHistory(context: Context) = viewModelScope.launch {
        HistorySearchManager.clearHistory(context)
    }
}