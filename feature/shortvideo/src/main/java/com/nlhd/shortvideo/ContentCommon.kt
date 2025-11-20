package com.nlhd.shortvideo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.Log
import android.view.SurfaceView
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.video.VideoDecoderGLSurfaceView
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.shortvideo.components.ActionItem
import com.nlhd.shortvideo.components.AvatarUser
import com.nlhd.shortvideo.components.BottomSheet
import com.nlhd.shortvideo.components.BottomSheetComment
import com.nlhd.shortvideo.components.BottomSheetGeneral
import com.nlhd.shortvideo.components.BottomSheetShare
import com.nlhd.shortvideo.components.InputText
import com.nlhd.shortvideo.components.TimeFormat
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs

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
var currentToast: Toast? = null
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@SuppressLint("CoroutineCreationDuringComposition", "ConfigurationScreenWidthHeight",
    "ContextCastToActivity"
)
@Composable
fun ContentCommon(
    token: String,
    pageF: String,
    isPlaying: Boolean,
    pagerState: PagerState,
    paddingValues: PaddingValues,
    videos: LazyPagingItems<Video>,
    onSearch: ((String) -> Unit)? = null,
    onHiddenText: ((Boolean) -> Unit)? = null,
    onClickSeeProduct: (Int, Int, Int) -> Unit,
    onClickProfile: (Int) -> Unit,
    onViewer: ((String)-> Unit)? = null,
    onPageSearchSuccess: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val activity = LocalContext.current as ComponentActivity
    val contentCommonViewModel: ContentCommonViewModel = koinViewModel(viewModelStoreOwner = activity)
    val widthScreen = LocalConfiguration.current.screenWidthDp.dp/2
    val infiniteTransition = rememberInfiniteTransition(label = "")
    /*val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        label = "",
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )*/
    val lifeCycleOwner = LocalLifecycleOwner.current

    val isScrolling by remember {
        derivedStateOf {
            pagerState.isScrollInProgress
        }
    }

    val scope = rememberCoroutineScope()

    val colorInfiniteTransition = rememberInfiniteTransition()
    val colorInfinite by colorInfiniteTransition.animateColor(
        initialValue = Color.Gray,
        targetValue = Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500), // Thời gian nhấp nháy
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val density = LocalDensity.current

    var clickedLike by remember { mutableStateOf(false) }
    val scaleLike by animateFloatAsState(
        targetValue = if (clickedLike) 0.85f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "scaleAnim",
        finishedListener = {
            if (clickedLike) {
                clickedLike = false
            }
        }
    )
    var clickedFav by remember { mutableStateOf(false) }
    val scaleFav by animateFloatAsState(
        targetValue = if (clickedFav) 0.85f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "scaleAnim",
        finishedListener = {
            if (clickedFav) {
                clickedFav = false
            }
        }
    )

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val loading = videos.loadState.append is LoadState.Loading
    val error = videos.loadState.append is LoadState.Error && pagerState.settledPage == videos.itemCount - 1 && !isScrolling && pagerState.lastScrolledForward

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                ),
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = if (loading || error) AppTheme.dimens.large2 else 0.dp
                    ),
                beyondViewportPageCount = 1,
                userScrollEnabled = !loading
            ) { page->


                val videoViewModel: VideoViewModel = koinViewModel(key = "$pageF $page")
                val state by videoViewModel.videoState.collectAsStateWithLifecycle()
                val displayText by videoViewModel.displayText.collectAsStateWithLifecycle()
                val video = videos[page]!!
                val actionButton by videoViewModel.actionButton.collectAsStateWithLifecycle()
                val currentPosition by videoViewModel.currentPosition.collectAsStateWithLifecycle()
                val duration by videoViewModel.duration.collectAsStateWithLifecycle()
                val aspectRatio by videoViewModel.aspectRatio.collectAsStateWithLifecycle()
                val isAutoScroll by contentCommonViewModel.isAutoScroll.collectAsStateWithLifecycle()


                val videoUrl = "${Utils.BASE_URL}/" + video.videoUrl
                val exoPlayer by remember {
                    mutableStateOf(
                        contentCommonViewModel.getOrCreatePlayer(pageF,page, videoUrl, context)
                    )
                }
                videoViewModel.getFollowUser(token, video.user.id.toString())


                if (isAutoScroll) {
                    exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
                } else {
                    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                }



                DisposableEffect(key1 = lifeCycleOwner) {
                    val observer = LifecycleEventObserver {_, event->
                        when (event) {
                            Lifecycle.Event.ON_RESUME -> {
                                if (pagerState.settledPage == page && isPlaying) {
                                    exoPlayer.playWhenReady = true
                                    //videoViewModel.increaseView(token, video.id)
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
                                if (page <= pagerState.pageCount - 1 && actionButton.comment == ShowHide.Hide) {
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

                    override fun onVideoSizeChanged(videoSize: VideoSize) {
                        super.onVideoSizeChanged(videoSize)
                        val width = videoSize.width
                        val height = videoSize.height
                        if (width > 0 && height > 0) {
                            videoViewModel.setAspectRatio(width, height)
                        }
                    }
                })

                LaunchedEffect(key1 = pagerState.settledPage) {
                    if (pagerState.settledPage == page && isPlaying) {
                        videoViewModel.increaseView(token, video.id)
                    }
                }

                LaunchedEffect(key1 = pagerState.settledPage, key2 = isPlaying) {
                    if (pagerState.settledPage == page && isPlaying) {
                        exoPlayer.playWhenReady = true
                        if (onSearch != null) {
                            val caption = if (video.caption == "") "Tìm nội dung liên quan" else video.caption
                            onSearch(caption)
                        }
                        onViewer?.invoke(video.views)
                        onPageSearchSuccess?.invoke(page)
                    } else {
                        exoPlayer.seekTo(0)
                        exoPlayer.playWhenReady = false
                    }
                }

                /*LaunchedEffect(pagerState.settledPage) {
                    val runtime = Runtime.getRuntime()
                    val used = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
                    val max = runtime.maxMemory() / 1024 / 1024
                    Log.d("AAA", "Heap usage: ${used}MB / ${max}MB")
                }*/

                val paddingBottom = paddingValues.calculateBottomPadding()

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
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
                        if (state == VideoState.PAUSE && actionButton.comment == ShowHide.Hide) {
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
                        val color = if (state == VideoState.IDLE && pagerState.settledPage == page && isPlaying) colorInfinite else Color.Transparent
                        val alphaSeekBar: Float by animateFloatAsState(if (actionButton.change == ChangeSlider.CHANGE) 1f else 0f, label = "alpha")
                        Column {
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
                                    if (isPlaying) {
                                        exoPlayer.playWhenReady = false
                                        videoViewModel.onValueChange(it)
                                        exoPlayer.seekTo(it.toLong())
                                    }

                                },
                                onValueChangeFinished = {
                                    if (isPlaying) {
                                        exoPlayer.playWhenReady = true
                                        videoViewModel.onValueFinish()
                                    }

                                },
                                modifier = Modifier
                                    .graphicsLayer {
                                        this.alpha = alphaSeekBar
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

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .zIndex(1f),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        val progress = if (duration > 0) (currentPosition / duration) else 0f
                        val alphaLinear: Float by animateFloatAsState(if (actionButton.change == ChangeSlider.CHANGE && pagerState.settledPage == page) 0f else 1f, label = "alpha")
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    this.alpha = alphaLinear
                                }
                                .height(AppTheme.dimens.extraSmall)
                                .padding(horizontal = AppTheme.dimens.small3),
                            color = Color.White,
                            trackColor = Color.Gray.copy(alpha = 0.3f),
                            strokeCap = StrokeCap.Round
                        )
                    }

                    /*if (video.images != null) {
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

                    }*/

                    val minScale = 0.25f + (aspectRatio * 0.55f)  // nhỏ nhất khi sheet chiếm 60%
                    val maxScale = 1f    // scale gốc

                    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

                    var sheetHeight by remember(page) { mutableStateOf(0.dp) }
                    val sheetVisible =  if(sheetHeight / screenHeight >= 0.0f) (sheetHeight / screenHeight).toFloat() else 0f  //Giá trị của bottomSheet hiển thị 0.6f

// Tính tỉ lệ dựa vào sheetVisible
                    val targetScale = (maxScale - (sheetVisible.coerceAtMost(0.6f) / 0.6f) * (maxScale - minScale))

                    val scaleX by animateFloatAsState(targetValue = targetScale, label = "scaleX")
                    val scaleY by animateFloatAsState(targetValue = targetScale, label = "scaleY")

                    val offsetY by animateDpAsState(
                        targetValue = -(screenHeight * sheetVisible / 2f),
                        label = "offsetAnim"
                    )

                    LaunchedEffect(key1 = targetScale, key2 = Unit, key3 = actionButton.commentPush == ShowHide.Show) {
                        if (targetScale != 1f) { //Nếu mở bottomSheet lên thì giá trị sẽ khác 1, khi đó sẽ ẩn text
                            onHiddenText?.invoke(true)
                        }
                        else {
                            onHiddenText?.invoke(false)
                        }
                    }


                    key("$pageF $page") {
                        AndroidView(factory = {
                            TextureView(it)
                        }, modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                this.scaleX = scaleX
                                this.scaleY = scaleY
                                translationY = offsetY.toPx()
                            }
                            .padding(bottom =paddingValues.calculateBottomPadding())
                            .aspectRatio(aspectRatio)
                            .zIndex(0f)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = {
                                        if (isPlaying && !isScrolling) {
                                            videoViewModel.like(token, video.id.toString(), onError = {
                                                currentToast?.cancel()
                                                currentToast = Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT)
                                                currentToast?.show()
                                            })
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
                            update = { textureView ->
                                // 👇 Tính khoảng cách giữa trang hiện tại và trang đang render
                                val distance = abs(pagerState.settledPage - page)

                                // 👇 Nếu trang nằm trong vùng hiển thị (hiện tại ± beyondCount)
                                if (distance < 1) {
                                    exoPlayer.setVideoTextureView(textureView)
                                } else {
                                    exoPlayer.setVideoTextureView(null)
                                }

                            }
                        )
                    }


                    val alpha: Float by animateFloatAsState(if (actionButton.change == ChangeSlider.CHANGE || targetScale != 1f) 0f else if (isPlaying && isScrolling && pagerState.settledPage == page && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) 0.3f else 1f, label = "alpha")


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


                        val stateFollow by videoViewModel.stateFollow.collectAsStateWithLifecycle()
                        val getFollowUserState by videoViewModel.getFollowUserState.collectAsStateWithLifecycle()

                        when (getFollowUserState) {
                            is ShortVideoState.Error -> {}
                            ShortVideoState.Idle -> {}
                            ShortVideoState.Loading -> {}
                            is ShortVideoState.Success -> {
                                val message = (getFollowUserState as ShortVideoState.Success).data.message
                                when(message) {
                                    "Đã follow" -> {
                                        videoViewModel.setFollow(Follow.Follow)
                                    }
                                    "Chưa follow" -> {
                                        videoViewModel.setFollow(Follow.UnFollow)

                                    }
                                    "Không thể follow chính mình" -> {
                                        videoViewModel.setFollow(Follow.Follow)
                                    }
                                }
                            }
                        }

                        when (stateFollow) {
                            is ShortVideoState.Error -> {

                            }
                            ShortVideoState.Idle -> {

                            }
                            ShortVideoState.Loading -> {

                            }
                            is ShortVideoState.Success -> {
                                if (getFollowUserState is ShortVideoState.Success) {
                                    val message = (getFollowUserState as ShortVideoState.Success).data.message
                                    when(message) {
                                        "Chưa follow" -> {
                                            videoViewModel.setFollow(Follow.Follow)
                                        }

                                    }

                                }
                                LaunchedEffect(Unit) {
                                    videoViewModel.setFollowState(ShortVideoState.Idle)
                                    videoViewModel.setFollowUserState(ShortVideoState.Idle)
                                    //videoViewModel.getFollowUser(token, video.user.id.toString())
                                }

                            }
                        }



                        AvatarUser(
                            avatar = if (video.user.avatarUrl == "null" || video.user.avatarUrl == null || video.user.avatarUrl == ""){
                                null
                            } else {
                                "${Utils.BASE_URL}/" + video.user.avatarUrl
                            },
                            isFollow = actionButton.avatar == Follow.Follow,
                            onClick = {
                                if (!isScrolling && isPlaying) {
                                    onClickProfile(video.user.id)
                                }

                            },
                            onClickAdd = {
                                if (!isScrolling && isPlaying) {
                                    videoViewModel.follows(token = token, userId = video.user.id.toString())
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        val stateLike by videoViewModel.stateLike.collectAsStateWithLifecycle()
                        val actionCountState by videoViewModel.actionCountState.collectAsStateWithLifecycle()
                        when (stateLike) {
                            is ShortVideoLikeState.Error -> {

                            }
                            ShortVideoLikeState.Idle -> {
                                if (video.isLiked) {
                                    videoViewModel.setLike(Color.Red)
                                } else {
                                    videoViewModel.setLike(Color.White)
                                }
                                videoViewModel.setLikeCount(video.likes)
                            }
                            ShortVideoLikeState.Loading -> {}
                            is ShortVideoLikeState.Success -> {
                            }
                        }

                        ActionItem(actionCountState.likeCount, R.drawable.ic_heart, color =actionButton.like, isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.medium3+AppTheme.dimens.border), clicked = clickedLike, scale = scaleLike, onScale = {clickedLike = it}) {
                            if (isPlaying && !isScrolling) {
                                videoViewModel.like(token, video.id.toString(), onError = {
                                    currentToast?.cancel()
                                    currentToast = Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT)
                                    currentToast?.show()
                                })
                            }
                        }

                        ActionItem(actionCountState.commentCount, R.drawable.ic_chat, isScrolling = isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.iconAction), clicked = false, scale = 1f, onScale = {}) {
                            if (isPlaying && !isScrolling) {
                                videoViewModel.onActionButton(Perform.Comment())
                            }
                        }

                        val contentComment by videoViewModel.contentComment.collectAsStateWithLifecycle()
                        val addCommentState by videoViewModel.addCommentState.collectAsStateWithLifecycle()
                        when (addCommentState) {
                            is ShortVideoState.Error -> {}
                            ShortVideoState.Idle -> {
                                videoViewModel.setCommentCount(video.comments)
                            }
                            ShortVideoState.Loading -> {}
                            is ShortVideoState.Success -> {
                                if (actionButton.comment == ShowHide.Show) {
                                    videoViewModel.setAddCommentState(ShortVideoState.Loading)
                                    videoViewModel.setContentComment("")

                                }

                            }
                        }

                        if (actionButton.comment == ShowHide.Show) {
                            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                            val commentsFlow = remember(video.id) { videoViewModel.commentsFlow(token, video.id.toString()) }
                            val comments = commentsFlow.collectAsLazyPagingItems()

                            if (addCommentState is ShortVideoState.Success) {
                                comments.refresh()
                            }
                            if (comments.loadState.refresh is LoadState.NotLoading) {
                                videoViewModel.setCommentCount(comments.itemCount.toShortString())
                                videoViewModel.setAddCommentState(ShortVideoState.Loading)
                            }

                            LaunchedEffect(sheetState) {
                                snapshotFlow { runCatching { sheetState.requireOffset() }.getOrNull() }
                                    .filterNotNull()
                                    .distinctUntilChanged()
                                    .collect { offsetPx ->
                                        val heightPx = (screenHeightPx - offsetPx).coerceAtLeast(0f)
                                        sheetHeight = with(density) { heightPx.toDp() }
                                    }
                            }
                            BottomSheet(
                                sheetState = sheetState,
                                onDismissRequest = {
                                    scope.launch {
                                        sheetState.hide()
                                        videoViewModel.onActionButton(Perform.Comment(ShowHide.Hide))
                                    }

                                },
                            ) {
                                BottomSheetComment(
                                    comments = comments,
                                    content = contentComment,
                                    onValueChange = videoViewModel::setContentComment,
                                    onClickCloseBottomSheet = {
                                        scope.launch {
                                            sheetState.hide()
                                            videoViewModel.onActionButton(Perform.Comment(ShowHide.Hide))
                                        }
                                    },
                                    onSendComment = {
                                        videoViewModel.addComment(token, video.id)
                                    },
                                    onClickProfile = { userId->
                                        onClickProfile(userId)
                                    },
                                    onClick = {
                                        scope.launch {
                                            //sheetState.hide()
                                            //videoViewModel.onActionButton(Perform.Comment(ShowHide.Hide))
                                            videoViewModel.onActionButton(Perform.CommentPush(ShowHide.Show))
                                        }
                                    }
                                )
                            }
                        }

                        if (actionButton.commentPush == ShowHide.Show) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    videoViewModel.onActionButton(Perform.CommentPush(ShowHide.Hide))
                                },
                                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                                containerColor = Color.White,
                                dragHandle = {},
                                shape = BottomSheetDefaults.ExpandedShape,
                                scrimColor = Color(0x4D000000),
                                contentWindowInsets = {
                                    BottomSheetDefaults.windowInsets
                                },
                                modifier = Modifier
                                    .fillMaxWidth()

                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        "Gửi đến @${video.user.name}",
                                        style = AppTheme.typography.titleMedium.copy(
                                            Color.Black,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier.padding(horizontal = AppTheme.dimens.small2, vertical = AppTheme.dimens.small3)
                                    )
                                    /*InputText(
                                        isReadOnly = false,
                                        isFocus = true,
                                        onSendComment = {
                                            videoViewModel.addComment(token, video.id)
                                            videoViewModel.onActionButton(Perform.CommentPush(ShowHide.Hide))
                                            //videoViewModel.onActionButton(Perform.Comment(ShowHide.Show))
                                        },
                                        content = contentComment,
                                        onValueChange = videoViewModel::setContentComment
                                    )*/

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        val focusRequester = remember {
                                            FocusRequester()
                                        }
                                        LaunchedEffect(Unit) {
                                            focusRequester.requestFocus()
                                        }
                                        val focusManager = LocalFocusManager.current
                                        Divider(
                                            thickness = AppTheme.dimens.extraSmall,
                                            color = containerSearch
                                        )
                                        BasicTextField(
                                            value = contentComment,
                                            onValueChange = videoViewModel::setContentComment,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .focusRequester(focusRequester)
                                                .heightIn(min = AppTheme.dimens.large3)
                                                .padding(AppTheme.dimens.small2),
                                            cursorBrush = SolidColor(contentPrice),
                                            maxLines = 1,
                                            keyboardOptions = KeyboardOptions(
                                                imeAction = ImeAction.Send,
                                                keyboardType = KeyboardType.Text
                                            ),
                                            keyboardActions = KeyboardActions(onSend = {
                                                //Setup
                                                videoViewModel.addComment(token, video.id)
                                                videoViewModel.onActionButton(Perform.CommentPush(ShowHide.Hide))
                                                focusManager.clearFocus()
                                            }),
                                            decorationBox = { innerTextField ->
                                                ConstraintLayout(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(
                                                            color = Color(0xFFEEEAEA),
                                                            shape = RoundedCornerShape(AppTheme.dimens.small2)
                                                        )
                                                        .padding(horizontal = AppTheme.dimens.small),
                                                ) {
                                                    val (text) = createRefs()
                                                    Box(
                                                        modifier = Modifier.constrainAs(text) {
                                                            start.linkTo(parent.start)
                                                            end.linkTo(parent.end)
                                                            top.linkTo(parent.top)
                                                            width = Dimension.fillToConstraints
                                                        }.padding(AppTheme.dimens.small3),
                                                        contentAlignment = Alignment.CenterStart
                                                    ) {
                                                        if (contentComment.isEmpty()) {
                                                            Text(
                                                                "Add comment...",
                                                                color = Color.Gray,
                                                                style = AppTheme.typography.labelMedium,
                                                                maxLines = 1
                                                            )
                                                        }
                                                        // 👇 chỉ gọi innerTextField, nó sẽ hiển thị content bạn nhập
                                                        innerTextField()
                                                    }
                                                }
                                            }
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))



                                }
                            }
                        }

                        val stateFavorite by videoViewModel.stateFavorite.collectAsStateWithLifecycle()
                        when (stateFavorite) {
                            is ShortVideoFavoriteState.Error -> {}
                            ShortVideoFavoriteState.Idle -> {
                                if (video.isFavorited) {
                                    videoViewModel.setFavorite(Color(0xFFFABA32))
                                } else {
                                    videoViewModel.setFavorite(Color.White)
                                }
                                videoViewModel.setFavoriteCount(video.favorites)
                            }
                            is ShortVideoFavoriteState.Success -> {

                            }
                        }
                        ActionItem(actionCountState.favoriteCount, R.drawable.ic_bookmark, color = actionButton.favorite, isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.medium2+AppTheme.dimens.small), clicked = clickedFav, scale = scaleFav, onScale = {clickedFav = it}) {
                            if (isPlaying && !isScrolling) {
                                videoViewModel.favorite(token, video.id.toString(), onError = {
                                    currentToast?.cancel()
                                    currentToast = Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT)
                                    currentToast?.show()
                                })
                            }
                        }

                        ActionItem(video.shares, R.drawable.ic_share, isScrolling = isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.iconAction), clicked = false, scale = 1f, onScale = {}) {
                            if (isPlaying) {
                                videoViewModel.onActionButton(Perform.Share())
                            }
                        }

                        if (actionButton.share == ShowHide.Show) {
                            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                            BottomSheet(
                                sheetState = sheetState,
                                onDismissRequest = {
                                    scope.launch {
                                        sheetState.hide()
                                        videoViewModel.onActionButton(Perform.Share(ShowHide.Hide))
                                    }
                                }
                            ) {
                                BottomSheetShare(
                                    videoId = video.id,
                                ) {
                                    scope.launch {
                                        sheetState.hide()
                                        videoViewModel.onActionButton(Perform.Share(ShowHide.Hide))
                                    }
                                }

                            }
                        }

                        if (actionButton.general == ShowHide.Show) {
                            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                            BottomSheet(
                                sheetState = sheetState,
                                onDismissRequest = {
                                    scope.launch {
                                        sheetState.hide()
                                        videoViewModel.onActionButton(Perform.General(ShowHide.Hide))
                                    }
                                }
                            ) {
                                BottomSheetGeneral(
                                    onClickScroll = {
                                        contentCommonViewModel.setAutoScroll()
                                        scope.launch {
                                            sheetState.hide()
                                            videoViewModel.onActionButton(Perform.General(ShowHide.Hide))
                                        }
                                    }
                                )

                            }
                        }

                        //val angleOperator = if (pagerState.settledPage == page && isPlaying && !isScrolling) angle else 0f
                        AsyncImage(
                            contentScale = ContentScale.Crop,
                            model = if (video.user.avatarUrl == null || video.user.avatarUrl == "null" || video.user.avatarUrl == "") null else "${Utils.BASE_URL}/" + video.user.avatarUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(AppTheme.dimens.icon)
                                .padding(AppTheme.dimens.border)
                                .clip(CircleShape)
                            /*.rotate(angleOperator)*/
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
                        if (isAutoScroll) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(color = Color(0x3919191F), shape = RoundedCornerShape(AppTheme.dimens.small))
                                    .padding(AppTheme.dimens.small)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = {
                                                if (!isScrolling && isPlaying) {
                                                    contentCommonViewModel.setAutoScroll()
                                                }

                                            }
                                        )
                                    }

                            ) {
                                Text(
                                    text = "AutoScroll: ON",
                                    style = AppTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.SemiBold),
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        }

                        if (video.productId != null && video.versionId != null && video.colorId != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(color = Color(0x3919191F), shape = RoundedCornerShape(AppTheme.dimens.small))
                                    .padding(AppTheme.dimens.small)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = {
                                                if (!isScrolling && isPlaying) {
                                                    onClickSeeProduct(video.productId!!, video.versionId!!, video.colorId!!)
                                                }

                                            }
                                        )
                                    }

                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_cart),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(AppTheme.dimens.medium)
                                )
                                Text(
                                    text = "Xem sản phẩm",
                                    style = AppTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.SemiBold),
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        }

                        Row(
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        if (!isScrolling && isPlaying) {
                                            onClickProfile(video.user.id)
                                        }
                                    }
                                )
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = video.user.name,
                                style = AppTheme.typography.titleMedium.copy(color = Color.White),
                                maxLines = 1
                            )
                            Text(
                                text = " · ${video.createdAt}",
                                style = AppTheme.typography.labelMedium.copy(
                                    color = Color(0x83FAFAFA),
                                    fontWeight = FontWeight.SemiBold
                                ),
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        val maxLine = if (displayText == DisplayText.Hide()) 2 else Int.MAX_VALUE
                        val visible = maxLine == 2
                        if (visible && video.caption != "") {
                            Text(
                                text = video.caption,
                                style = AppTheme.typography.bodyMedium.copy(
                                    color = Color(0xFFFFFFFF),
                                ),
                                maxLines = maxLine,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.pointerInput(Unit) {
                                    detectTapGestures(onTap = {
                                        if (!isScrolling && isPlaying) {
                                            videoViewModel.onDisplayText()
                                        }

                                    })
                                }
                            )
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        }
                        AnimatedVisibility(visible = !visible) {
                            if (video.caption != "") {
                                Text(
                                    text = video.caption,
                                    style = AppTheme.typography.bodyMedium.copy(
                                        color = Color(0xFFFFFFFF),
                                    ),
                                    maxLines = maxLine,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.pointerInput(Unit) {
                                        detectTapGestures(onTap = {
                                            if (!isScrolling && isPlaying) {
                                                videoViewModel.onDisplayText()
                                            }

                                        })
                                    }
                                )
                                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                            }

                        }


                        /*Row(
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
                            *//*val marque = if (pagerState.settledPage == page && isPlaying && !isScrolling) {
                                Modifier.basicMarquee(
                                    iterations = Int.MAX_VALUE,
                                    animationMode = MarqueeAnimationMode.Immediately,
                                    repeatDelayMillis = 2000,
                                    initialDelayMillis = 0,
                                    spacing = MarqueeSpacing(spacing = AppTheme.dimens.small3)
                                )
                            } else {
                                Modifier
                            }*//*
                            Text(
                                text = "${video.user.name} - Hiện tại chưa cập nhật được nhạc nền",
                                style = AppTheme.typography.labelMedium.copy(
                                    color = Color(0xFFFFFFFF)
                                ),
                                maxLines = 1
                            )
                        }*/

                        //Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

                    }

                }

            }


        }

        if (loading) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(paddingValues)
            ) {
                LottieAnimation(
                    composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(AppTheme.dimens.large)

                )
            }
        }

        if (error) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(paddingValues)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            videos.retry()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = contentPrice
                        ),
                        modifier = Modifier.padding(AppTheme.dimens.small2)
                    ) {
                        Text("Retry", style = AppTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ))
                    }
                }
            }
        }

    }




}

fun Int.toShortString(): String {
    return when {
        this >= 1_000_000_000 -> String.format("%.1fB", this / 1_000_000_000.0).removeSuffix(".0")
        this >= 1_000_000     -> String.format("%.1fM", this / 1_000_000.0).removeSuffix(".0")
        this >= 100_000       -> String.format("%dK", this / 1_000) // 100K, 250K...
        this >= 10_000        -> String.format("%dK", this / 1_000) // 10K, 15K...
        this >= 1_000         -> String.format("%.1fK", this / 1_000.0).removeSuffix(".0")
        else                  -> this.toString()
    }
}

class VideoPlayerView(context: Context) : FrameLayout(context) {
    val textureView = TextureView(context)
    init { addView(textureView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) }
}