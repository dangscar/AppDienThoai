package com.nlhd.shortvideo

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShortVideoScreen(
    viewModel: ShortVideoViewModel = koinViewModel(),
    innerPadding: PaddingValues,
    onClickSeeProduct: (Int, Int, Int) -> Unit,
    onClickBack: () -> Unit,
    onClickSearch: () -> Unit,
    onClickProfile: (Int) -> Unit
) {

    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")

    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") viewModel.setToken(keyStore.value)
    }

    val videos = viewModel.videosFlow.collectAsLazyPagingItems()
    val pagerState = rememberPagerState {
        videos.itemCount
    }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = {

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
                                .size(AppTheme.dimens.medium3)
                                .padding(AppTheme.dimens.border)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onClickSearch
                    ) {
                        Icon(
                            painter = painterResource(id = com.nlhd.core.R.drawable.ic_search),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(AppTheme.dimens.medium3)
                                .padding(AppTheme.dimens.border)
                        )
                    }


                }
            )
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

                ContentCommon(
                    token = keyStore.value,
                    pageF = "Page",
                    isPlaying = true,
                    pagerState = pagerState,
                    paddingValues = innerPadding,
                    videos = videos,
                    onClickSeeProduct = onClickSeeProduct,
                    onClickProfile = onClickProfile
                )
            }
        }

    }
}