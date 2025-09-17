package com.nlhd.shortvideo

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.shortvideo.components.ActionItem
import com.nlhd.shortvideo.components.AvatarUser
import com.nlhd.shortvideo.components.BottomSheet
import com.nlhd.shortvideo.components.BottomSheetComment
import com.nlhd.shortvideo.components.TimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "ConfigurationScreenWidthHeight")
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
    onClickProfile: (Int) -> Unit
) {
    val context = LocalContext.current
    val contentCommonViewModel: ContentCommonViewModel = koinViewModel()
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
            ),
            key = { page-> videos[page]?.id ?: page }
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

            val videoUrl = "${Utils.BASE_URL}/" + video.videoUrl
            val exoPlayer by remember {
                mutableStateOf(
                    contentCommonViewModel.getOrCreatePlayer(page, videoUrl, context)
                )
            }



            if (isAutoScroll) {
                exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
            } else {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            }

            DisposableEffect(key1 = Unit) {
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
                                videoViewModel.increaseView(token, video.id)
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

            LaunchedEffect(key1 = pagerState.settledPage) {
                if (pagerState.settledPage == page && isPlaying) {
                    videoViewModel.increaseView(token, video.id)
                }
            }

            LaunchedEffect(key1 = pagerState.settledPage, key2 = isPlaying, key3 = pagerState.currentPage) {
                if (pagerState.settledPage == page && isPlaying) {
                    exoPlayer.playWhenReady = true
                    if (onSearch != null) {
                        onSearch(video.user.name)
                    }


                } else {
                    exoPlayer.seekTo(0)
                    exoPlayer.playWhenReady = false
                }
            }


            val paddingBottom = paddingValues.calculateBottomPadding()

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (!isScrolling && isPlaying) {

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

                AndroidView(factory = {
                    PlayerView(it).also {
                        it.useController = false
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
                        if (pagerState.settledPage == page) {
                            it.player = exoPlayer
                        } else {
                            it.player = null
                        }

                    },
                    onRelease = {it.player = null}
                )

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


                    val stateFollow by videoViewModel.stateFollow.collectAsStateWithLifecycle()
                    when (stateFollow) {
                        is ShortVideoState.Error -> {

                        }
                        ShortVideoState.Idle -> {
                            if (video.isFollowing && video.canFollow || !video.isFollowing && !video.canFollow)  {
                                videoViewModel.setFollow(Follow.Follow)
                            } else {
                                videoViewModel.setFollow(Follow.UnFollow)
                            }
                        }
                        ShortVideoState.Loading -> {

                        }
                        is ShortVideoState.Success -> {
                            videoViewModel.setFollow(Follow.Follow)
                        }
                    }


                    AvatarUser(
                        avatar = if (video.user.avatarUrl == "null"){
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
                    when (stateLike) {
                        is ShortVideoState.Error -> {}
                        ShortVideoState.Idle -> {
                            if (video.isLiked) {
                                videoViewModel.setLike(Color.Red)
                            } else {
                                videoViewModel.setLike(Color.White)
                            }
                        }
                        ShortVideoState.Loading -> {}
                        is ShortVideoState.Success -> {
                            val message = (stateLike as ShortVideoState.Success).data.message
                            when (message) {
                                "Added" -> {
                                    videoViewModel.setLike(Color.Red)
                                }
                                "Deleted" -> {
                                    videoViewModel.setLike(Color.White)
                                }
                            }
                        }
                    }

                    ActionItem(video.likes, R.drawable.ic_heart, color =actionButton.like, isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.medium3), modifierSpacer = Modifier.height(AppTheme.dimens.border)) {
                        if (isPlaying) {
                            videoViewModel.like(token, video.id.toString())
                        }
                    }

                    ActionItem(video.comments, R.drawable.ic_chat, isScrolling = isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.iconAction), modifierSpacer = Modifier.height(AppTheme.dimens.border)) {
                        videoViewModel.onActionButton(Perform.Comment())
                    }

                    val contentComment by videoViewModel.contentComment.collectAsStateWithLifecycle()
                    val addCommentState by videoViewModel.addCommentState.collectAsStateWithLifecycle()
                    when (addCommentState) {
                        is ShortVideoState.Error -> {}
                        ShortVideoState.Idle -> {}
                        ShortVideoState.Loading -> {}
                        is ShortVideoState.Success -> {
                            if (actionButton.comment == ShowHide.Show) {
                                videoViewModel.onActionButton(Perform.Comment(ShowHide.Hide))
                                videoViewModel.setAddCommentState(ShortVideoState.Idle)
                            }

                        }
                    }

                    if (actionButton.comment == ShowHide.Show) {
                        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                        val commentsFlow = remember(video.id) { videoViewModel.commentsFlow(token, video.id.toString()) }
                        val comments = commentsFlow.collectAsLazyPagingItems()

                        BottomSheet(
                            sheetState = sheetState,
                            onDismissRequest = {
                                scope.launch {
                                    sheetState.hide()
                                    videoViewModel.onActionButton(Perform.Comment(ShowHide.Hide))
                                }
                            }
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
                                }
                            )
                        }

                    }

                    val stateFavorite by videoViewModel.stateFavorite.collectAsStateWithLifecycle()
                    when (stateFavorite) {
                        is ShortVideoState.Error -> {}
                        ShortVideoState.Idle -> {
                            if (video.isFavorited) {
                                videoViewModel.setFavorite(Color.Yellow)
                            } else {
                                videoViewModel.setFavorite(Color.White)
                            }
                        }
                        ShortVideoState.Loading -> {}
                        is ShortVideoState.Success -> {
                            val message = (stateFavorite as ShortVideoState.Success).data.message
                            when (message) {
                                "Added" -> {
                                    videoViewModel.setFavorite(Color.Yellow)
                                }
                                "Deleted" -> {
                                    videoViewModel.setFavorite(Color.White)
                                }
                            }
                        }
                    }
                    ActionItem(video.favorites, R.drawable.ic_bookmark, color = actionButton.favorite, isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.medium3), modifierSpacer = Modifier.height(AppTheme.dimens.border)) {
                        if (isPlaying) {
                            videoViewModel.favorite(token, video.id.toString())
                        }
                    }

                    ActionItem(video.shares, R.drawable.ic_share, isScrolling = isScrolling, modifierIcon = Modifier.size(AppTheme.dimens.iconAction), modifierSpacer = Modifier.height(AppTheme.dimens.border)) {
                        if (isPlaying) {
                            videoViewModel.onActionButton(Perform.Share())
                        }
                    }
                    val angleOperator = if (pagerState.settledPage == page && isPlaying && !isScrolling) angle else 0f
                    AsyncImage(
                        model = if (video.user.avatarUrl == null) null else "${Utils.BASE_URL}/" + video.user.avatarUrl,
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
                    if (visible) {
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
                    }
                    AnimatedVisibility(visible = !visible) {
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
                            text = "Nhạc nền ${video.user.name}",
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