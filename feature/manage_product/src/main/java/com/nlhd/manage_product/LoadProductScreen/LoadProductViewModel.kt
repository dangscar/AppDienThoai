package com.nlhd.manage_product.LoadProductScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.manageProduct.LoadProduct.Product
import com.nlhd.domain.usecase.manageProduct.ManageProductUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoadProductViewModel(
    private val manageProductUseCase: ManageProductUseCase
): ViewModel() {

    private val _search = MutableStateFlow("")
    val search = _search.asStateFlow()

    private val trigger = MutableSharedFlow<String>()


    fun setSearch(search: String) = _search.update { search }

    fun onSearchClick() = viewModelScope.launch { trigger.emit(search.value) }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun products(token: String): Flow<PagingData<Product>> = trigger
        .onStart { emit("") }
        .flatMapLatest { keyword -> manageProductUseCase.loadProducts(token, keyword) }
        .cachedIn(viewModelScope)
        .debounce(500)
        .distinctUntilChanged()

}