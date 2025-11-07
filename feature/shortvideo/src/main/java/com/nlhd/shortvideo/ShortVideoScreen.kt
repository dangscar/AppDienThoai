package com.nlhd.shortvideo

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel
import com.nlhd.core.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShortVideoScreen(
    viewModel: ShortVideoViewModel = koinViewModel(),
    innerPadding: PaddingValues,
    onClickSeeProduct: (Int, Int, Int) -> Unit,
    onClickBack: () -> Unit,
    onClickSearch: () -> Unit,
    onClickProfile: (Int) -> Unit,
) {

    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")

    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            viewModel.setToken(keyStore.value)
            viewModel.setFollowingToken(keyStore.value)
        }
    }

    val videos = viewModel.videosFlow.collectAsLazyPagingItems()
    val followingVideos = viewModel.followingVideosFlow.collectAsLazyPagingItems()
    val pagerState = rememberPagerState {
        videos.itemCount
    }
    val followingPagerState = rememberPagerState {
        followingVideos.itemCount
    }
    val isHidden by viewModel.isHidden.collectAsStateWithLifecycle()

    val titleHorizontal = listOf("Khám phá","Người dùng", "Đang theo dõi", "Dành cho bạn")
    val pageStateHorizontal = rememberPagerState(initialPage = titleHorizontal.size-1) {
        titleHorizontal.size
    }
    val scope = rememberCoroutineScope()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

    val activity = LocalContext.current as ComponentActivity
    val contentCommonViewModel: ContentCommonViewModel = koinViewModel(viewModelStoreOwner = activity)

    val isRefreshing = videos.loadState.refresh is LoadState.Loading
    val refreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        state = refreshState,
        onRefresh = {
            onClickBack()
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black,
            topBar = {
            }
        ) {

            val alpha = if (isHidden) 0f else 1f
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimens.small2)
                    .graphicsLayer {
                        this.alpha = alpha
                    }
                    .zIndex(1f)
                    .padding(innerPadding)
                    .padding(horizontal = AppTheme.dimens.small2)
            ) {
                val (reload, tabs, search) = createRefs()

                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .constrainAs(reload) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(tabs.start)
                        }
                        .padding(bottom = AppTheme.dimens.small2, start = AppTheme.dimens.small2, end = AppTheme.dimens.small2)
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_reload),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(AppTheme.dimens.medium2)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        onClickBack()
                                    }
                                )
                            }
                    )
                }

                ScrollableTabRow(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.dimens.small)
                        .constrainAs(tabs) {
                        top.linkTo(parent.top)
                        start.linkTo(reload.end)
                        end.linkTo(search.start)
                        width = Dimension.fillToConstraints
                    },
                    selectedTabIndex = pageStateHorizontal.settledPage,
                    containerColor = Color.Transparent,
                    divider = {

                    },
                    indicator = { tabPositions ->
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[pageStateHorizontal.currentPage]),
                            color = Color.White,
                        )
                    }
                ) {
                    titleHorizontal.forEachIndexed { ind, text->
                        Tab(
                            selected = ind == pageStateHorizontal.settledPage,
                            onClick = {
                                if (ind == pageStateHorizontal.settledPage) {
                                    return@Tab
                                }
                                if (contentCommonViewModel.releaseAll()) {
                                    scope.launch {
                                        pageStateHorizontal.scrollToPage(ind)
                                    }
                                }

                            },
                            selectedContentColor = Color.Transparent,
                            unselectedContentColor = Color.Transparent,
                            modifier = Modifier.padding(bottom = AppTheme.dimens.small2)
                        ) {
                            Text(
                                text = text,
                                style = AppTheme.typography.labelMedium.copy(
                                    color = if (ind == pageStateHorizontal.settledPage) Color.White else Color(0xB3FAFAFA),
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(vertical = AppTheme.dimens.small, horizontal = AppTheme.dimens.small),
                            )
                        }
                    }
                }


                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .constrainAs(search) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(tabs.end)
                        }
                        .padding(bottom = AppTheme.dimens.small, end = AppTheme.dimens.small3, start = AppTheme.dimens.small3, top = AppTheme.dimens.small)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search_short),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(AppTheme.dimens.iconAdd)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    onClickSearch()
                                })
                            }
                    )
                }
            }

            when (videos.loadState.refresh) {
                is LoadState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Vui lòng thử lại", style = AppTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        ))
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        Button(
                            onClick = {
                                videos.retry()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = contentPrice
                            )
                        ) {
                            Text("Retry", style = AppTheme.typography.headlineMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ))
                        }
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

                    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                        HorizontalPager(
                            state = pageStateHorizontal,
                            userScrollEnabled = false
                        ) {
                            when (it) {
                                0 -> {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "Đang thực hiện chức năng khám phá",
                                            style = AppTheme.typography.titleMedium.copy(
                                                color = Color.White
                                            )
                                        )
                                    }
                                }
                                1 -> {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "Đang thực hiện chức năng người dùng",
                                            style = AppTheme.typography.titleMedium.copy(
                                                color = Color.White
                                            )
                                        )
                                    }
                                }
                                2 -> {
                                    ContentCommon(
                                        token = keyStore.value,
                                        pageF = "Following",
                                        isPlaying = pageStateHorizontal.settledPage == 2,
                                        pagerState = followingPagerState,
                                        paddingValues = innerPadding,
                                        videos = followingVideos,
                                        onClickSeeProduct = onClickSeeProduct,
                                        onClickProfile = onClickProfile,
                                        onHiddenText = {
                                            viewModel.setHidden(it)
                                        }
                                    )
                                }
                                else -> {
                                    ContentCommon(
                                        token = keyStore.value,
                                        pageF = "ForYou",
                                        isPlaying = pageStateHorizontal.settledPage == 3,
                                        pagerState = pagerState,
                                        paddingValues = innerPadding,
                                        videos = videos,
                                        onClickSeeProduct = onClickSeeProduct,
                                        onClickProfile = onClickProfile,
                                        onHiddenText = {
                                            viewModel.setHidden(it)
                                        }
                                    )
                                }
                            }
                        }
                    }

                }
            }

        }
    }

}