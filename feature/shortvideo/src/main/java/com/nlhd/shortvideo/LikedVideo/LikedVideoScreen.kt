package com.nlhd.shortvideo.LikedVideo

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.PlayArrow
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.shortvideo.Search.DetailShortVideoScreen
import com.nlhd.shortvideo.components.ListShortVideo
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object ListLikedVideo

@Serializable
data class DetailLikedVideo(
    val position: Int
)

@Serializable
data class DetailFavoriteVideo(
    val position: Int
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LikedVideoScreen(
    viewModel: LikedVideoViewModel = koinViewModel(),
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")

    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") viewModel.setToken(keyStore.value)
    }

    val likedVideos = viewModel.likedVideosFlow.collectAsLazyPagingItems()
    val favoriteVideos = viewModel.favoriteVideosFlow.collectAsLazyPagingItems()
    val navController = rememberNavController()

    val tabs = listOf("Liked Video", "Favorite Video")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState {
        tabs.size
    }
    val scope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = ListLikedVideo
    ) {
        composable<ListLikedVideo> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.White,
                topBar = {
                    TopAppBar(
                        title = {
                            Text("My liked video", style = AppTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        ),
                        navigationIcon = {
                            IconButton(
                                onClick = onClickBack
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(
                                        AppTheme.dimens.medium2)
                                )
                            }
                        }
                    )
                }
            ) { innerPadding->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    stickyHeader {


                        Column {
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
                    }

                    item {
                        HorizontalPager(
                            state = pagerState,
                            verticalAlignment = Alignment.Top,
                        ) {
                            when (it) {
                                0 -> {
                                    ListShortVideo(
                                        videos = likedVideos,
                                        onClick = { position ->
                                            navController.navigate(DetailLikedVideo(position))
                                        }
                                    )
                                }
                                1 -> {
                                    ListShortVideo(
                                        videos = favoriteVideos,
                                        onClick = { position ->
                                            navController.navigate(DetailFavoriteVideo(position))
                                        }
                                    )
                                }
                            }
                        }
                    }



                }
            }
        }
        composable<DetailLikedVideo> {
            val position = it.toRoute<DetailLikedVideo>().position
            DetailShortVideoScreen(
                isTopBar = false,
                position = position,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                videos = likedVideos,
                onClickSeeProduct = { productId, versionId, colorId ->

                },
                onClickProfile = { userId ->

                },
                onSearch = {

                }
            )
        }

        composable<DetailFavoriteVideo> {
            val position = it.toRoute<DetailFavoriteVideo>().position
            DetailShortVideoScreen(
                isTopBar = false,
                position = position,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                videos = favoriteVideos,
                onClickSeeProduct = { productId, versionId, colorId ->

                },
                onClickProfile = { userId ->

                },
                onSearch = {

                }
            )
        }
    }

}