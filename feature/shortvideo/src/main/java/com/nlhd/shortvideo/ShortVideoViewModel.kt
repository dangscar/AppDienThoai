package com.nlhd.shortvideo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import kotlinx.coroutines.flow.Flow

class ShortVideoViewModel(
    private val shortVideoUseCase: ShortVideoUseCase
): ViewModel() {

    private var _videoPagingDataFriends: Flow<PagingData<Video>>? = null
    val videoPagingDataFriends: Flow<PagingData<Video>> = getVideosFriendsPaging()

    fun getVideosFriendsPaging(): Flow<PagingData<Video>> {
        if (_videoPagingDataFriends == null) {
            _videoPagingDataFriends = shortVideoUseCase.getVideos.invoke().cachedIn(viewModelScope)
        }
        return _videoPagingDataFriends!!
    }
}