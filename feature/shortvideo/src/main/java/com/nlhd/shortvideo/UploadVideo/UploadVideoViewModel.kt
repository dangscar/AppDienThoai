package com.nlhd.shortvideo.UploadVideo

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.shortVideo.UploadVideo.UploadVideo
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UploadVideoViewModel(
    private val shortVideoUseCase: ShortVideoUseCase,
    private val exoPlayer: ExoPlayer
): ViewModel() {

    private var _state = MutableStateFlow(UploadVideoAction(
        video = Uri.EMPTY,
        image = null,
        caption = "",
        productId = null
    ))
    val state = _state.asStateFlow()

    private var _size = MutableStateFlow(0.0)
    val size = _size.asStateFlow()

    private var _addVideoState = MutableStateFlow<UploadVideoState>(UploadVideoState.Idle)
    val addVideoState = _addVideoState.asStateFlow()

    private var _isPlaying = MutableStateFlow(false)

    fun setSize(size: Double) {
        _size.update { size }
    }

    fun getExoPlayer() = exoPlayer

    fun setUpExoPlayer() {
        exoPlayer.apply {
            setMediaItem(MediaItem.fromUri(_state.value.video))
            prepare()
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    fun setVideo(video: Uri) {
        _state.update { it.copy(video = video) }
        setUpExoPlayer()
    }

    fun actionVideo() {
        _isPlaying.update { !_isPlaying.value }
        if (!_isPlaying.value) {
            exoPlayer.pause()
            exoPlayer.playWhenReady = false
            return
        } else {
            exoPlayer.play()
            exoPlayer.playWhenReady = true
        }
    }

    fun pause() {
        _isPlaying.update { false }
        exoPlayer.pause()
        exoPlayer.playWhenReady = false
    }


    fun setImage(image: Uri) {
        _state.update { it.copy(image = image) }
    }

    fun setCaption(caption: String) {
        _state.update { it.copy(caption = caption) }
    }

    fun setProductId(productId: Int) {
        _state.update { it.copy(productId = productId) }
    }

    fun addVideo(token: String, context: Context) = viewModelScope.launch {
        _addVideoState.update { UploadVideoState.Loading }
        val uploadVideo = UploadVideo(
            context = context,
            video = state.value.video,
            image = state.value.image,
            caption = state.value.caption,
            productId = state.value.productId
        )

        shortVideoUseCase.addVideo(token, uploadVideo).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _addVideoState.update {
                        UploadVideoState.Error(result.exception.message.toString())
                    }
                }
                is ResultWrapper.Success<*> -> {
                    _addVideoState.update {
                        UploadVideoState.Success(result.value as MessageResponse)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.pause()
        exoPlayer.playWhenReady = false
        exoPlayer.removeMediaItem(0)
    }
}
sealed class UploadVideoState {
    object Idle: UploadVideoState()
    object Loading: UploadVideoState()
    data class Success(val data: MessageResponse): UploadVideoState()
    data class Error(val message: String): UploadVideoState()
}

data class UploadVideoAction(
    val video: Uri,
    val image: Uri? = null,
    val caption: String? = null,
    val productId: Int? = null
)