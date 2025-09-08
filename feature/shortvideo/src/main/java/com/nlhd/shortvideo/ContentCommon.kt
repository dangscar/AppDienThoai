package com.nlhd.shortvideo

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.shortvideo.components.ActionItem
import com.nlhd.shortvideo.components.AvatarUser
import com.nlhd.shortvideo.components.TimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.get

@Composable
fun Circle(
    selected: Boolean = false,
) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color = if (selected) Color.White else Color.LightGray, shape = CircleShape)
    )
    Spacer(modifier = Modifier.width(5.dp))
}

@Composable
fun CountText(settledPage: Int, pageCount: Int) {
    Box(
        modifier = Modifier.background(color = Color.Gray, shape = RoundedCornerShape(AppTheme.dimens.small2)),
        contentAlignment = Alignment.Center,

        ) {
        Text(
            "${settledPage}/${pageCount}",
            style = AppTheme.typography.titleSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(5.dp)
        )
    }
}
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "ConfigurationScreenWidthHeight")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentCommon(
    pageF: String,
    isPlaying: Boolean,
    pagerState: PagerState,
    paddingValues: PaddingValues,
    videos: LazyPagingItems<Video>,
    onSearch: ((String)-> Unit)? = null,
    onHiddenText: ((Boolean) -> Unit)? = null,
) {
    val context = LocalContext.current
    val contentCommonViewModel: ContentCommonViewModel = koinViewModel(key = pageF)
    DisposableEffect(key1 = pageF) {
        onDispose {
            if (!isPlaying) {
                contentCommonViewModel.releaseAll()
            }
        }
    }
    val isAutoScroll by contentCommonViewModel.isAutoScroll.collectAsStateWithLifecycle()
    val widthScreen = LocalConfiguration.current.screenWidthDp.dp/2
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        label = "",
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val lifeCycleOwner = LocalLifecycleOwner.current

    val isScrolling by remember {
        derivedStateOf {
            pagerState.isScrollInProgress
        }
    }

    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        VerticalPager(
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapAnimationSpec = spring(
                    stiffness = Spring.StiffnessMedium,
                    dampingRatio =Spring.DampingRatioMediumBouncy,
                ),
                decayAnimationSpec = exponentialDecay(0.9f)
            )
        ) { page->
            val videoViewModel: VideoViewModel = koinViewModel(key = "$pageF $page")

            val state by videoViewModel.videoState.collectAsStateWithLifecycle()
            val displayText by videoViewModel.displayText.collectAsStateWithLifecycle()

            val video = videos[page]!!

            val actionButton by videoViewModel.actionButton.collectAsStateWithLifecycle()
            val imageStatus by videoViewModel.imageStatus.collectAsStateWithLifecycle()
            val currentPosition by videoViewModel.currentPosition.collectAsStateWithLifecycle()
            val duration by videoViewModel.duration.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = actionButton.comment) {
                if (actionButton.comment == ShowHide.Show) {
                    onHiddenText?.invoke(true)
                } else {
                    onHiddenText?.invoke(false)
                }
            }

            val exoPlayer by remember {
                mutableStateOf(
                    contentCommonViewModel.getOrCreatePlayer(page, video.url, context)
                )
            }

            if (isAutoScroll) {
                exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
            } else {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            }

            DisposableEffect(key1 = Unit, isPlaying) {
                onDispose {
                    exoPlayer.pause()

                }
            }

            DisposableEffect(key1 = lifeCycleOwner) {
                val observer = LifecycleEventObserver {_, event->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> {
                            if (pagerState.settledPage == page && isPlaying) {
                                exoPlayer.playWhenReady = true
                            }
                        }
                        Lifecycle.Event.ON_PAUSE -> {
                            exoPlayer.playWhenReady = false
                        }
                        else -> {

                        }
                    }
                }
                lifeCycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifeCycleOwner.lifecycle.removeObserver(observer)
                }
            }

            exoPlayer.addListener(object : Player.Listener {
                @SuppressLint("SwitchIntDef")
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when(playbackState) {
                        Player.STATE_IDLE -> {
                            videoViewModel.onEvent(VideoState.IDLE)
                        }
                        Player.STATE_BUFFERING -> {
                            videoViewModel.onEvent(VideoState.IDLE)
                        }
                        Player.STATE_ENDED -> {
                            if (page <= pagerState.pageCount - 1) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (!isPlaying && exoPlayer.playbackState == Player.STATE_READY) {
                        videoViewModel.onEvent(VideoState.PAUSE)
                    } else if (isPlaying && exoPlayer.playbackState == Player.STATE_READY) {
                        videoViewModel.onEvent(VideoState.PLAY)
                    }
                }
            })

            LaunchedEffect(key1 = pagerState.settledPage, key2 = isPlaying) {
                if (pagerState.settledPage == page && isPlaying) {
                    exoPlayer.playWhenReady = true
                    if (onSearch != null) {
                        onSearch(video.search)
                    }
                } else {
                    exoPlayer.seekTo(0)
                    exoPlayer.playWhenReady = false
                }
            }

            //val alphaAnimation by animateFloatAsState(if (isScrolling && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) 0.4f else 1f, label = "alphaA")

            val paddingBottom = paddingValues.calculateBottomPadding()

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (!isScrolling && isPlaying) {
                                    videoViewModel.onActionButton(Perform.Like())
                                }
                            },
                            onTap = {
                                if (!exoPlayer.isPlaying && isPlaying && pagerState.settledPage == page) {
                                    exoPlayer.play()
                                } else {
                                    exoPlayer.pause()
                                }
                            },
                            onLongPress = {
                                if (isPlaying && pagerState.settledPage == page) {
                                    videoViewModel.onActionButton(Perform.General())
                                }
                            }
                        )
                    }
            ) {
                val (action, description) = createRefs()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingBottom)
                        .zIndex(2f),
                    contentAlignment = Alignment.Center
                ) {
                    if (state == VideoState.PAUSE) {
                        Icon(
                            painter = painterResource(R.drawable.play),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(AppTheme.dimens.large)
                                .graphicsLayer {
                                    this.alpha = 0.5f
                                }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .zIndex(1f),
                    contentAlignment = Alignment.BottomStart
                ) {
                    val colorInfiniteTransition = rememberInfiniteTransition()
                    val colorInfinite by colorInfiniteTransition.animateColor(
                        initialValue = Color.Gray,
                        targetValue = Color.Transparent,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 500), // Thời gian nhấp nháy
                            repeatMode = RepeatMode.Reverse
                        ), label = ""
                    )
                    val color = if (state == VideoState.IDLE && pagerState.settledPage == page && isPlaying) colorInfinite else Color.Transparent
                    Column {

                        val alpha: Float by animateFloatAsState(if (actionButton.change == ChangeSlider.CHANGE) 1f else 0f, label = "alpha")
                        videoViewModel.handleDuration(exoPlayer)
                        LaunchedEffect(key1 = exoPlayer) {
                            videoViewModel.updatePosition(exoPlayer)
                        }

                        AnimatedVisibility(
                            visible = actionButton.change == ChangeSlider.CHANGE
                        ) {
                            TimeFormat(
                                currentPosition = currentPosition,
                                duration = duration
                            )
                        }

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                        Slider(
                            value = currentPosition,
                            enabled = !isScrolling,
                            onValueChange = {
                                exoPlayer.playWhenReady = false
                                videoViewModel.onValueChange(it)
                                exoPlayer.seekTo(it.toLong())
                            },
                            onValueChangeFinished = {
                                exoPlayer.playWhenReady = true
                                videoViewModel.onValueFinish()
                            },
                            modifier = Modifier
                                .graphicsLayer {
                                    this.alpha = alpha
                                }
                                .fillMaxWidth()
                                .padding(horizontal = AppTheme.dimens.small3),
                            valueRange = 0f..duration,
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color.White,
                                inactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                            ),
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = color)
                                .height(AppTheme.dimens.extraSmall)
                        )
                    }
                }

                if (video.images != null) {
                    val pageState = rememberPagerState { video.images!!.size }
                    LaunchedEffect(pageState.settledPage) {
                        videoViewModel.onImageStatus(video.images!!, pageState.settledPage)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingBottom)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = {
                                        if (!isScrolling) {
                                            videoViewModel.onActionButton(Perform.Like())
                                        }
                                    },
                                    onTap = {
                                        if (!exoPlayer.isPlaying && isPlaying && pagerState.settledPage == page) {
                                            exoPlayer.play()
                                        } else {
                                            exoPlayer.pause()
                                        }
                                    },
                                    onLongPress = {
                                        if (isPlaying && pagerState.settledPage == page) {
                                            videoViewModel.onActionButton(Perform.General())
                                        }
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalPager(
                            state = pageState,
                        ) { page->

                            AsyncImage(
                                model = video.images!![page],
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                onLoading = {

                                }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(bottom = paddingBottom),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center

                        ) {
                            imageStatus.list.forEachIndexed { ind, _ ->
                                Circle(selected = if (ind == imageStatus.selectedIndex) true else false)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(top = paddingValues.calculateTopPadding())
                            .padding(top = AppTheme.dimens.small3),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        CountText(settledPage = imageStatus.selectedIndex+1, pageCount = imageStatus.list.size)
                    }
                } else {
                    AndroidView(factory = {
                        PlayerView(it).also {
                            it.useController = false
                            it.player = exoPlayer
                            it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        }
                    }, modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .zIndex(0f)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    if (!isScrolling) {
                                        videoViewModel.onActionButton(Perform.Like())
                                    }
                                },
                                onTap = {
                                    if (!exoPlayer.isPlaying && isPlaying && pagerState.settledPage == page) {
                                        exoPlayer.play()
                                    } else {
                                        exoPlayer.pause()
                                    }
                                },
                                onLongPress = {
                                    if (isPlaying && pagerState.settledPage == page) {
                                        videoViewModel.onActionButton(Perform.General())
                                    }
                                }
                            )
                        },
                        update = {
                            it.player = exoPlayer
                        },
                        onRelease = {it.player = null}
                    )
                }

                val alpha: Float by animateFloatAsState(if (actionButton.change == ChangeSlider.CHANGE || actionButton.comment == ShowHide.Show) 0f else if (isPlaying && isScrolling && pagerState.settledPage == page && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) 0.3f else 1f, label = "alpha")

                Column(
                    modifier = Modifier
                        .zIndex(2f)
                        .graphicsLayer {
                            this.alpha = alpha
                        }
                        .constrainAs(action) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = paddingValues.calculateBottomPadding() )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AvatarUser {

                    }
                    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    ActionItem("79.8K", R.drawable.ic_heart, color =actionButton.like, isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.medium3), modifierSpacer = Modifier.height(AppTheme.dimens.border)) {
                        if (isPlaying) {
                            videoViewModel.onActionButton(Perform.Like())
                        }
                    }
                    ActionItem("498", R.drawable.ic_chat, isScrolling = isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.iconAction), modifierSpacer = Modifier.height(AppTheme.dimens.border)) {

                    }
                    ActionItem("10.1K", R.drawable.ic_bookmark, color = actionButton.favorite, isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.medium3), modifierSpacer = Modifier.height(AppTheme.dimens.border)) {
                        if (isPlaying) {
                            videoViewModel.onActionButton(Perform.Favorite())
                        }
                    }
                    ActionItem("1,588", R.drawable.ic_share, isScrolling = isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.iconAction), modifierSpacer = Modifier.height(AppTheme.dimens.border)) {
                        if (isPlaying) {
                            videoViewModel.onActionButton(Perform.Share())
                        }
                    }
                    val angleOperator = if (pagerState.settledPage == page && isPlaying && !isScrolling) angle else 0f
                    AsyncImage(
                        model = "https://i.pinimg.com/1200x/69/78/19/69781905dd57ba144ab71ca4271ab294.jpg",
                        contentDescription = null,
                        modifier = Modifier
                            .size(AppTheme.dimens.icon)
                            .padding(AppTheme.dimens.border)
                            .clip(CircleShape)
                            .rotate(angleOperator)
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
                }

                Column(
                    modifier = Modifier
                        .graphicsLayer {
                            this.alpha = alpha
                        }
                        .zIndex(2f)
                        .constrainAs(description) {
                            start.linkTo(parent.start)
                            end.linkTo(action.start)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .padding(paddingValues)
                        .padding(horizontal = AppTheme.dimens.small3)
                        .padding(AppTheme.dimens.small)
                ) {
                    Row {
                        Text(
                            text = video.channelTitle,
                            style = AppTheme.typography.titleMedium.copy(color = Color.White),
                            maxLines = 1
                        )
                        Text(
                            text = " · ${video.publishedAt}",
                            style = AppTheme.typography.titleMedium.copy(
                                color = Color.White,
                            ),
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                    val maxLine = if (displayText == DisplayText.Hide()) 2 else Int.MAX_VALUE
                    val visible = maxLine == 2
                    if (visible) {
                        Text(
                            text = video.title,
                            style = AppTheme.typography.bodyMedium.copy(
                                color = Color(0xFFFFFFFF),
                            ),
                            maxLines = maxLine,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    if (isPlaying) {
                                        videoViewModel.onDisplayText()
                                    }

                                })
                            }
                        )
                    }
                    AnimatedVisibility(visible = !visible) {
                        Text(
                            text = video.title,
                            style = AppTheme.typography.bodySmall.copy(
                                color = Color(0xFFFFFFFF),
                            ),
                            maxLines = maxLine,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    if (isPlaying) {
                                        videoViewModel.onDisplayText()
                                    }

                                })
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    Row(
                        modifier = Modifier
                            .width(widthScreen)
                            .zIndex(2f)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {

                                })
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.music),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(AppTheme.dimens.medium)
                                .padding(AppTheme.dimens.small)
                        )
                        Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                        val marque = if (pagerState.settledPage == page && isPlaying && !isScrolling) {
                            Modifier.basicMarquee(
                                iterations = Int.MAX_VALUE,
                                animationMode = MarqueeAnimationMode.Immediately,
                                repeatDelayMillis = 2000,
                                initialDelayMillis = 0,
                                spacing = MarqueeSpacing(spacing = AppTheme.dimens.small3)
                            )
                        } else {
                            Modifier
                        }
                        Text(
                            text = video.linkMusic,
                            style = AppTheme.typography.bodyMedium.copy(
                                color = Color(0xFFFFFFFF)
                            ),
                            maxLines = 1,
                            modifier = marque
                        )
                    }

                    Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

                }

            }

        }
    }



}