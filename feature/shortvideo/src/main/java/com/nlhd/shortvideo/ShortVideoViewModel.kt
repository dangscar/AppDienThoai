package com.nlhd.shortvideo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ShortVideoViewModel(
    private val shortVideoUseCase: ShortVideoUseCase
): ViewModel() {

    fun getVideos(token: String): Flow<PagingData<Video>> {
        return shortVideoUseCase.getVideos.invoke(token).cachedIn(viewModelScope)
    }


}

