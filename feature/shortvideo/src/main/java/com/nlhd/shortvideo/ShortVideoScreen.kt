package com.nlhd.shortvideo

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.compose.collectAsLazyPagingItems
import com.nlhd.core.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShortVideoScreen(
    viewModel: ShortVideoViewModel = koinViewModel(),
    innerPadding: PaddingValues
) {

    val videos = viewModel.videoPagingDataFriends.collectAsLazyPagingItems()
    val pagerState = rememberPagerState {
        videos.itemCount
    }
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
                actions = {
                    Icon(
                        painter = painterResource(id = com.nlhd.core.R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(AppTheme.dimens.medium3)
                            .padding(AppTheme.dimens.border)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {

                                })
                            }
                    )

                }
            )
        }
    ) {

        ContentCommon(
            pagerState = pagerState,
            isPlaying = true,
            pageF = "F",
            paddingValues = innerPadding,
            videos = videos
        )
    }
}