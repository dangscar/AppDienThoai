package com.nlhd.shortvideo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ShortVideoViewModel(
    private val shortVideoUseCase: ShortVideoUseCase
): ViewModel() {

    private val tokenFlow = MutableStateFlow<String?>(null)

    fun setToken(token: String) {
        // tránh rebuild khi token không đổi
        if (tokenFlow.value != token) tokenFlow.value = token
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val videosFlow: Flow<PagingData<Video>> =
        tokenFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { t ->
                shortVideoUseCase.getVideos.invoke(t) // trả về Flow<PagingData<Video>>
            }
            .cachedIn(viewModelScope)


}

