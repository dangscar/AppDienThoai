package com.nlhd.shortvideo

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContentCommonViewModel(
    private val defaultMediaSourceFactory: DefaultMediaSourceFactory,
): ViewModel() {

    private var _isAutoScroll: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAutoScroll = _isAutoScroll.asStateFlow()

    fun setAutoScroll() {
        if (!_isAutoScroll.value) {
            _isAutoScroll.update { true }
        } else {
            _isAutoScroll.update { false }
        }
    }

    private val MAX_PLAYERS = 3 // Giới hạn số ExoPlayer cùng tồn tại
    private val playerMap = mutableMapOf<Int, ExoPlayer>()
    private var pageDefault = 0
    private var isFirst = false

    @OptIn(UnstableApi::class)
    fun getOrCreatePlayer(page: Int, videoUrl: String, context: Context): ExoPlayer {
        // Nếu player đã tồn tại, trả về nó
        if (pageDefault < page) {
            pageDefault = page
            isFirst = true
        } else {
            pageDefault = page
            isFirst = false
        }

        playerMap[page]?.let { return it }

        // Nếu số lượng player vượt quá giới hạn, giải phóng player cũ nhất
        if (playerMap.size >= MAX_PLAYERS) {
            val oldestKey = if (isFirst) playerMap.keys.minOrNull() else playerMap.keys.maxOrNull()
            playerMap.remove(oldestKey)?.release()
        }

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                /* minBufferMs = */ 3_000,
                /* maxBufferMs = */ 10_000,
                /* bufferForPlaybackMs = */ 500,
                /* bufferForPlaybackAfterRebufferMs = */ 1_000
            )
            .build()

        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(
                buildUponParameters()
                    .setMaxVideoSizeSd()   // Giới hạn SD để tiết kiệm RAM
            )
        }

        val exoPlayer = ExoPlayer
            .Builder(context)
            .setMediaSourceFactory(defaultMediaSourceFactory)
            .setLoadControl(loadControl)
            .setTrackSelector(trackSelector)
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(videoUrl))
                prepare()
            }
        playerMap[page] = exoPlayer
        return exoPlayer
    }
    fun releaseAll() {
        playerMap.values.forEach { it.release() }
        playerMap.clear()
    }

    override fun onCleared() {
        super.onCleared()
        releaseAll()
    }
}