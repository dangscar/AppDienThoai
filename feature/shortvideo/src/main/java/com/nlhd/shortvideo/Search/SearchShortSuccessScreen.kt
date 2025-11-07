package com.nlhd.shortvideo.Search

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.shortvideo.ContentCommonViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

import org.koin.androidx.compose.koinViewModel

@Serializable
object ListShortVideo

@Serializable
data class DetailShortVideo(
    val position: Int
)

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("ConfigurationScreenWidthHeight", "ContextCastToActivity")
@Composable
fun SearchShortSuccessScreen(
    viewModel: SearchShortVideoSuccessViewModel = koinViewModel(),
    search: String,
    onClickBack: () -> Unit,
    onClickSeeProduct: (Int, Int, Int) -> Unit,
    onClickProfile: (Int) -> Unit,
    onSearch: () -> Unit
) {
    viewModel.setQuery(search)
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val query by viewModel.query.collectAsStateWithLifecycle()
    val videos = viewModel.videos.collectAsLazyPagingItems()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            viewModel.setToken(keyStore.value)
            viewModel.onSearchClick()
        }
    }

    val navController = rememberNavController()
    val activity = LocalContext.current as ComponentActivity
    val contentCommonViewModel: ContentCommonViewModel = koinViewModel(viewModelStoreOwner = activity)
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyStaggeredGridState()
    val tabs = listOf("Tất cả Video", "Người dùng")
    val pagerState = rememberPagerState {
        tabs.size
    }

    NavHost(
        navController = navController,
        startDestination = ListShortVideo
    ) {
        composable<ListShortVideo> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    Column {
                        SearchTopBar(
                            query = query,
                            onQueryChange = viewModel::setQuery,
                            onSearch = {
                                contentCommonViewModel.releaseAll()
                                viewModel.onSearchClick()
                            },
                            onClickBack = onClickBack
                        )
                        TabRow(
                            selectedTabIndex = pagerState.currentPage,
                            containerColor = Color.White,
                            indicator = { tabPositions ->
                                if (pagerState.settledPage < tabPositions.size) {
                                    TabRowDefaults.SecondaryIndicator(
                                        Modifier.tabIndicatorOffset(tabPositions[pagerState.settledPage]),
                                        color = Color.Black
                                    )
                                }
                            }
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selectedContentColor = Color.Black,
                                    unselectedContentColor = Color.Gray,
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        scope.launch { pagerState.animateScrollToPage(index) }
                                    },
                                    text = { Text(
                                        text = title,
                                        style = AppTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )
                                    ) }
                                )
                            }
                        }
                    }


                },
                containerColor = Color.White
            ) { innerPadding ->
                HorizontalPager(
                    state = pagerState
                ) {
                    when (it) {
                        0 -> {
                            when (videos.loadState.refresh) {
                                is LoadState.Error -> {
                                    val message = (videos.loadState.refresh as LoadState.Error).error.message ?: "Unknown error"
                                    Box(modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding), contentAlignment = Alignment.Center) {
                                        Text(message, style = AppTheme.typography.titleMedium)
                                    }
                                }
                                LoadState.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LottieAnimation(
                                            composition,
                                            iterations = LottieConstants.IterateForever,
                                            modifier = Modifier.size(AppTheme.dimens.large)
                                        )
                                    }
                                }
                                is LoadState.NotLoading -> {
                                    var currentVisibleIndex by remember { mutableIntStateOf(0) }
                                    val width = LocalConfiguration.current.screenWidthDp.dp/2
                                    val height = LocalConfiguration.current.screenHeightDp.dp/2.75f

                                    LaunchedEffect(gridState) {
                                        snapshotFlow {
                                            val layoutInfo = gridState.layoutInfo
                                            val visibleItems = layoutInfo.visibleItemsInfo

                                            if (visibleItems.isEmpty()) return@snapshotFlow null

                                            // 🔹 Nếu item đầu tiên còn phủ màn hình nhiều nhất -> chọn 0
                                            // Ngược lại, chọn item chiếm diện tích nhiều nhất
                                            visibleItems.maxByOrNull { item ->
                                                val top = item.offset.y
                                                val bottom = top + item.size.height

                                                // Giới hạn vùng hiển thị trong viewport (0..viewportHeight)
                                                val visibleTop = maxOf(0, top)
                                                val visibleBottom = minOf(layoutInfo.viewportEndOffset, bottom)
                                                val visibleHeight = (visibleBottom - visibleTop).coerceAtLeast(0)

                                                visibleHeight.toFloat() / item.size.height.toFloat()
                                            }?.index
                                        }
                                            .distinctUntilChanged()
                                            .collect { firstVisible ->
                                                if (firstVisible != null) {
                                                    currentVisibleIndex = firstVisible
                                                }
                                            }
                                    }

                                    LazyVerticalStaggeredGrid(
                                        state = gridState,
                                        modifier = Modifier.fillMaxWidth().padding(innerPadding),
                                        columns = StaggeredGridCells.Fixed(2),
                                    ) {

                                        items(videos.itemCount) {
                                            val video = videos[it]

                                            Column(
                                                modifier = Modifier.pointerInput(Unit) {
                                                    detectTapGestures(onTap = { offset->
                                                        if (currentVisibleIndex != it) {
                                                            contentCommonViewModel.pauseAll()
                                                        }

                                                        navController.navigate(DetailShortVideo(it))
                                                    })
                                                }
                                            ) {
                                                if (currentVisibleIndex == it && video != null) {
                                                    val videoUrl = "${Utils.BASE_URL}/" + video!!.videoUrl
                                                    val exoPlayer by remember {
                                                        mutableStateOf(
                                                            contentCommonViewModel.getOrCreatePlayer(
                                                                "Search",
                                                                it,
                                                                videoUrl,
                                                                context
                                                            )
                                                        )
                                                    }
                                                    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                                                    scope.launch {
                                                        delay(500)
                                                        contentCommonViewModel.playVisiblePlayer(
                                                            pageF = "Search", // hoặc "Following" tùy tab bạn đang ở
                                                            visibleIndex = currentVisibleIndex
                                                        )
                                                    }


                                                    AndroidView(
                                                        factory = { context ->
                                                            val playerView = PlayerView(context).apply {
                                                                useController = false
                                                            }
                                                            playerView
                                                        },
                                                        update = { playerView ->
                                                            playerView.player = exoPlayer
                                                        },
                                                        modifier = Modifier
                                                            .size(width, height)
                                                            .padding(AppTheme.dimens.small)
                                                            .background(color = Color.Black, shape = RoundedCornerShape(AppTheme.dimens.small2))

                                                    )
                                                } else {
                                                    AsyncImage(
                                                        model = if (video?.thumbnailUrl != null && video.thumbnailUrl != "") "${Utils.BASE_URL}/"+ video.thumbnailUrl else R.drawable.anhden,
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(width, height)
                                                            .padding(AppTheme.dimens.small)
                                                            .clip(RoundedCornerShape(AppTheme.dimens.small2)),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                }

                                                if (video!!.caption != "") {
                                                    Text(
                                                        text = video.caption,
                                                        style = AppTheme.typography.headlineMedium.copy(
                                                            color = Color.Black,
                                                            fontWeight = FontWeight.Normal
                                                        ),
                                                        maxLines = 2,
                                                        overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier.width(width).padding(horizontal = AppTheme.dimens.small2 ,vertical = AppTheme.dimens.extraSmall)
                                                    )
                                                }

                                                Row(
                                                    modifier = Modifier.width(width).padding(vertical = AppTheme.dimens.small),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = AppTheme.dimens.small2),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        AsyncImage(
                                                            model = if (video.user.avatarUrl == "null" || video.user.avatarUrl == null || video.user.avatarUrl == "") R.drawable.anhden else "${Utils.BASE_URL}/"+video.user.avatarUrl,
                                                            contentDescription = null,
                                                            modifier = Modifier
                                                                .size(AppTheme.dimens.medium)
                                                                .pointerInput(Unit) {
                                                                    detectTapGestures(onTap = { offset->
                                                                    })
                                                                }
                                                                .clip(CircleShape),
                                                            contentScale = ContentScale.Crop
                                                        )
                                                        Text(
                                                            text = video.user.name,
                                                            style = AppTheme.typography.titleSmall.copy(
                                                                color = Color.Black,
                                                                fontWeight = FontWeight.Normal
                                                            ),
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            modifier = Modifier.padding(AppTheme.dimens.small)
                                                        )
                                                    }
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = AppTheme.dimens.small2),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(R.drawable.ic_heart),
                                                            contentDescription = null,
                                                            modifier = Modifier
                                                                .size(AppTheme.dimens.small3),
                                                            tint = Color.Red
                                                        )
                                                        Text(
                                                            text = video.likes,
                                                            style = AppTheme.typography.titleSmall.copy(
                                                                color = Color.Black,
                                                                fontWeight = FontWeight.Normal
                                                            ),
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            modifier = Modifier.padding(AppTheme.dimens.small)
                                                        )
                                                    }
                                                }

                                            }
                                        }
                                    }

                                }
                            }
                        }
                        else -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Coming soon", style = AppTheme.typography.titleMedium)
                            }
                        }
                    }
                }


            }
        }
        composable<DetailShortVideo> {
            val position = it.toRoute<DetailShortVideo>().position

            DetailShortVideoScreen(
                pageF = "Search",
                position = position,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                videos = videos,
                onClickSeeProduct = onClickSeeProduct,
                onClickProfile = onClickProfile,
                onSearch = onSearch,
                onPageSearchSuccess = {

                }
            )
        }
    }

}