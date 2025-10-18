package com.nlhd.shortvideo

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
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

    val titleHorizontal by remember {
        mutableStateOf(listOf("Following", "For You"))
    }
    val pageStateHorizontal = rememberPagerState(initialPage = titleHorizontal.size-1) {
        titleHorizontal.size
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        topBar = {
            if (!isHidden) {
                /*TopAppBar(
                    title = {
                        Text(
                            "Short Videos",
                            style = AppTheme.typography.headlineMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onClickBack()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = com.nlhd.core.R.drawable.ic_reload),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(AppTheme.dimens.medium2)
                                    .padding(AppTheme.dimens.border)
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = onClickSearch
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(AppTheme.dimens.medium2)
                            )
                        }


                    }
                )*/


            }
        }
    ) {

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

            }
            is LoadState.NotLoading -> {

                if (!isHidden) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(1f)
                            .padding(innerPadding)
                            .padding(AppTheme.dimens.small3),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TabRow(
                            selectedTabIndex = pageStateHorizontal.settledPage+1,
                            containerColor = Color.Transparent,
                            divider = {

                            },
                            indicator = { tabPositions ->
                                TabRowDefaults.PrimaryIndicator(
                                    modifier = Modifier
                                        .tabIndicatorOffset(tabPositions[pageStateHorizontal.currentPage+1]),
                                    color = Color.White,
                                )
                            }
                        ) {
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier.padding(bottom = AppTheme.dimens.small2)
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

                            titleHorizontal.forEachIndexed { ind, text->
                                Tab(
                                    selected = ind == pageStateHorizontal.settledPage,
                                    onClick = {
                                        scope.launch {
                                            pageStateHorizontal.animateScrollToPage(ind)
                                        }
                                    },
                                    selectedContentColor = Color.Transparent,
                                    unselectedContentColor = Color.Transparent,
                                    modifier = Modifier.padding(bottom = AppTheme.dimens.small2)
                                ) {
                                    Text(
                                        text = text,
                                        style = AppTheme.typography.titleMedium.copy(color = if (ind == pageStateHorizontal.settledPage) Color.White else Color(
                                            0xB3FAFAFA
                                        )
                                        ),
                                        modifier = Modifier,
                                    )
                                }
                            }
                            Box(
                                contentAlignment = Alignment.CenterEnd,
                                modifier = Modifier.padding(bottom = AppTheme.dimens.small2)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.search),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(AppTheme.dimens.medium)
                                        .pointerInput(Unit) {
                                            detectTapGestures(onTap = {
                                                onClickSearch()
                                            })
                                        }
                                )
                            }

                        }
                    }
                }

                CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                    HorizontalPager(state = pageStateHorizontal) {
                        when (it) {
                            0 -> {
                                ContentCommon(
                                    token = keyStore.value,
                                    pageF = "Page1",
                                    isPlaying = pageStateHorizontal.settledPage == 0,
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
                                    pageF = "Page2",
                                    isPlaying = pageStateHorizontal.settledPage == 1,
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