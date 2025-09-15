package com.nlhd.shortvideo.Search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.product.Product
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import com.nlhd.keystore.HistorySearchManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchShortVideoSuccessViewModel(
    private val shortVideoUseCase: ShortVideoUseCase
): ViewModel() {

    private var _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val trigger = MutableSharedFlow<String>(replay = 1,extraBufferCapacity = 1)
    private val tokenFlow = MutableStateFlow<String?>(null)
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val videos: Flow<PagingData<Video>> =
        combine(tokenFlow.filterNotNull(), trigger.debounce(500).distinctUntilChanged()) { token, keyword ->
            token to keyword
        }.flatMapLatest { (token, keyword) ->
            shortVideoUseCase.getVideosSearch(token, keyword) // repo trả về Pager.flow
        }.cachedIn(viewModelScope)

    fun onSearchClick() {
        viewModelScope.launch {
            trigger.emit(_query.value)
        }
    }

    fun setQuery(query: String) {
        _query.update { query }
    }

    fun setToken(token: String) {
        tokenFlow.value = token
    }
    fun addSearch(context: Context) = viewModelScope.launch {
        if (query.value.isNotEmpty()) {
            HistorySearchManager.addHistory(context, query.value)
        }

    }

}