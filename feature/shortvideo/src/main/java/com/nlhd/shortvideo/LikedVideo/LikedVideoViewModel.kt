package com.nlhd.shortvideo.LikedVideo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

class LikedVideoViewModel(
    private val shortVideoUseCase: ShortVideoUseCase
): ViewModel() {

    private val tokenFlow = MutableStateFlow<String?>(null)

    fun setToken(token: String) {
        // tránh rebuild khi token không đổi
        if (tokenFlow.value != token) tokenFlow.value = token
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val likedVideosFlow: Flow<PagingData<Video>> =
        tokenFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { t ->
                shortVideoUseCase.getVideosLiked(t) // trả về Flow<PagingData<Video>>
            }
            .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteVideosFlow: Flow<PagingData<Video>> =
        tokenFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { t ->
                shortVideoUseCase.getVideosFavorite(t) // trả về Flow<PagingData<Video>>
            }
            .cachedIn(viewModelScope)



}