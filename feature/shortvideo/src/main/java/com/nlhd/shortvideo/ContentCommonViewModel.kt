package com.nlhd.shortvideo

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
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

    private val MAX_PLAYERS = 4 // Giới hạn số ExoPlayer cùng tồn tại
    private val playerMap = mutableMapOf<String, ExoPlayer>()

    private var pageDefault = 0
    private var isFirst = false

    @OptIn(UnstableApi::class)
    fun loadControl() = DefaultLoadControl.Builder()
        .setBufferDurationsMs(
            /* minBufferMs = */ 3_000,
            /* maxBufferMs = */ 10_000,
            /* bufferForPlaybackMs = */ 500,
            /* bufferForPlaybackAfterRebufferMs = */ 1_000
        )
        .build()

    @OptIn(UnstableApi::class)
    fun trackSelector(context: Context) = DefaultTrackSelector(context).apply {
        setParameters(
            buildUponParameters()
                .setMaxVideoSizeSd()   // Giới hạn SD để tiết kiệm RAM
        )
    }

    @OptIn(UnstableApi::class)
    private fun renderersFactory(context: Context) = DefaultRenderersFactory(context)
        .setEnableDecoderFallback(true)
        .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
        .forceEnableMediaCodecAsynchronousQueueing()
        .setEnableAudioTrackPlaybackParams(true)

    @OptIn(UnstableApi::class)
    fun getOrCreatePlayer(pageF: String, page: Int, videoUrl: String, context: Context): ExoPlayer {
        val key = "$pageF $page" // Khóa dạng "ForYou 1" hoặc "Following 2"

        // Xác định hướng cuộn
        if (pageDefault < page) {
            pageDefault = page
            isFirst = true
        } else {
            pageDefault = page
            isFirst = false
        }
        //Log.d("AAA", playerMap.toString())
        // Nếu đã tồn tại → trả về luôn
        playerMap[key]?.let { return it }

        // Nếu vượt giới hạn MAX_PLAYERS → tìm player xa nhất để release
        if (playerMap.size >= MAX_PLAYERS) {
            val victim = playerMap.keys
                .filter { it != key }
                .maxByOrNull { existingKey ->
                    // Lấy số page từ key cũ để so sánh khoảng cách
                    val oldPage = existingKey.substringAfterLast(" ").toIntOrNull() ?: 0
                    kotlin.math.abs(oldPage - page)
                }

            victim?.let { victimKey ->
                playerMap.remove(victimKey)?.release()
            }
        }

        // Tạo mới player
        val exoPlayer = ExoPlayer.Builder(context)
            .setMediaSourceFactory(defaultMediaSourceFactory)
            .setLoadControl(loadControl())
            .setTrackSelector(trackSelector(context))
            .build()

        playerMap[key] = exoPlayer

        exoPlayer.setMediaItem(MediaItem.fromUri(videoUrl))
        exoPlayer.prepare()

        return exoPlayer
    }

    fun pauseAll() {
        playerMap.values.forEach { it.pause() }
    }

    fun releaseAll(): Boolean {
        playerMap.values.forEach { it.release() }
        playerMap.clear()
        return true
    }

    fun playVisiblePlayer(pageF: String, visibleIndex: Int): Boolean {
        val targetKey = "$pageF $visibleIndex"

        playerMap.forEach { (key, player) ->
            if (key == targetKey) {
                player.playWhenReady = true
                player.play()
            } else {
                player.playWhenReady = false
                player.pause()
            }
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        releaseAll()
    }
}