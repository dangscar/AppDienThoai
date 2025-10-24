package com.nlhd.shortvideo.MyVideos

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.shortvideo.Search.DetailShortVideoScreen
import com.nlhd.shortvideo.components.BottomSheet
import com.nlhd.shortvideo.components.DeleteConfirmDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object ListMyVideos

@Serializable
data class DetailMyVideos(
    val position: Int
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyVideoScreen(
    viewModel: MyVideoViewModel = koinViewModel(),
    onClickBack: () -> Unit,
) {

    val context = LocalContext.current
    val keyStore by KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val videos = viewModel.videos.collectAsLazyPagingItems()
    val deleteVideoState by viewModel.deleteVideoState.collectAsStateWithLifecycle()
    val updateVideoState by viewModel.updateCaptionState.collectAsStateWithLifecycle()
    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()
    val caption by viewModel.caption.collectAsStateWithLifecycle()
    LaunchedEffect(keyStore) {
        if (keyStore != "") {
            viewModel.setToken(keyStore)
            viewModel.onSearchClick()
        }
    }

    val navController = rememberNavController()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = ListMyVideos
    ) {
        composable<ListMyVideos> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text("My videos", style = AppTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        },
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
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        ),
                    )
                },
                containerColor = Color.White,
            ) { innerPadding->
                when (videos.loadState.refresh) {
                    is LoadState.Error -> {
                        val error = videos.loadState.refresh as LoadState.Error
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), contentAlignment = Alignment.Center) {
                            Text(error.error.message.toString(), style = AppTheme.typography.titleMedium)
                        }
                    }
                    LoadState.Loading -> {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = contentPrice
                            )
                        }
                    }
                    is LoadState.NotLoading -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(innerPadding),
                        ) {
                            items(videos.itemCount) {
                                VideoItemRow(
                                    video = videos[it]!!,
                                    onEdit = {
                                        viewModel.setVideoId(videos[it]!!.id)
                                        viewModel.setCaption(videos[it]!!.caption)
                                        scope.launch { sheetState.show() }
                                    },
                                    onDelete = {
                                        viewModel.setVideoId(videos[it]!!.id)
                                        viewModel.setShowDialog(true)
                                    },
                                    onClickVideo = {
                                        navController.navigate(DetailMyVideos(it))
                                    }
                                )

                            }
                        }
                    }
                }

                DeleteConfirmDialog(
                    showDialog = showDialog,
                    onConfirm = {
                        viewModel.deleteVideo(keyStore)
                        viewModel.setShowDialog(false)
                    },
                    onDismiss = {
                        viewModel.setShowDialog(false)
                    }
                )

                if (sheetState.isVisible) {
                    BottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = {
                            viewModel.setCaption("")
                            scope.launch { sheetState.hide() }
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f)
                        ) {
                            Box(
                                modifier = Modifier,
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Text(
                                    "Chỉnh sửa",
                                    style = AppTheme.typography.labelMedium.copy(
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center)
                                )
                                IconButton(
                                    onClick = { scope.launch { sheetState.hide() }}
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(AppTheme.dimens.medium)
                                    )
                                }

                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    buildAnnotatedString {
                                        append("Caption")
                                    },
                                    style = AppTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                )
                                val desLenght = if (caption == null) "0" else caption!!.length.toString()
                                Text(
                                    desLenght,
                                    style = AppTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                )
                            }

                            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                            OutlinedTextField(
                                value = caption ?: "",
                                onValueChange = {
                                    viewModel.setCaption(it)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppTheme.dimens.small2)
                                    .height(AppTheme.dimens.large2),
                                shape = RoundedCornerShape(AppTheme.dimens.small2),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF7F56D9),
                                    unfocusedBorderColor = Color.LightGray
                                ),
                            )
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                            OutlinedButton(
                                onClick = {
                                    viewModel.updateCaption(keyStore)
                                },
                                modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = contentPrice,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(AppTheme.dimens.small2),
                                border = BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
                            ) {
                                Text(
                                    "Đăng tải",
                                    style = AppTheme.typography.headlineMedium.copy(
                                        fontFamily = Font.fontFamily,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(AppTheme.dimens.small)
                                )
                            }
                        }

                    }
                }


                when (deleteVideoState) {
                    is MyVideoState.Error -> {
                        val error = (deleteVideoState as MyVideoState.Error).message
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        viewModel.setDeleteVideoState(MyVideoState.Idle)
                    }
                    MyVideoState.Idle -> {}
                    MyVideoState.Loading -> {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = contentPrice
                            )
                        }
                    }
                    is MyVideoState.Success -> {
                        val message = (deleteVideoState as MyVideoState.Success).data.message
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        videos.refresh()
                        viewModel.setDeleteVideoState(MyVideoState.Idle)
                    }
                }

                when (updateVideoState) {
                    is MyVideoState.Error -> {
                        val error = (updateVideoState as MyVideoState.Error).message
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        viewModel.setUpdateCaptionState(MyVideoState.Idle)
                    }
                    MyVideoState.Idle -> {}
                    MyVideoState.Loading -> {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = contentPrice
                            )
                        }
                    }
                    is MyVideoState.Success -> {
                        scope.launch { sheetState.hide() }
                        videos.refresh()
                        viewModel.setUpdateCaptionState(MyVideoState.Idle)
                    }
                }

            }
        }
        composable<DetailMyVideos> {
            val position = it.toRoute<DetailMyVideos>().position
            DetailShortVideoScreen(
                pageF = "MyVideo",
                position = position,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                videos = videos,
                onClickSeeProduct = {_, _, _->},
                onClickProfile = {},
                onSearch = {},
                isTopBar = false
            )
        }
    }


}

@Composable
fun VideoItemRow(
    video: Video,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClickVideo: () -> Unit
) {
    val image = if (video.thumbnailUrl == "null" || video.thumbnailUrl == null || video.thumbnailUrl == "") R.drawable.anhden else  "${Utils.BASE_URL}/"+ video.thumbnailUrl
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.small3, vertical = AppTheme.dimens.small2)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onClickVideo()
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ảnh vuông bên trái (placeholder)
        AsyncImage(
            model = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(AppTheme.dimens.large2)
                .clip(RoundedCornerShape(AppTheme.dimens.small)),

        )

        Spacer(Modifier.width(AppTheme.dimens.small2))

        // Nội dung giữa – chiếm hết phần còn lại
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = video.caption,
                style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(AppTheme.dimens.small2))

            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small3)) {
                Text(
                    text = "${video.views} views",
                    style = AppTheme.typography.bodyMedium,
                    color = Color.Black,
                    maxLines = 1
                )
                Text(
                    text = "${video.likes} likes",
                    style = AppTheme.typography.bodyMedium,
                    color = Color.Black,
                    maxLines = 1
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // Actions bên phải
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Edit",
                style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                ),
                modifier = Modifier
                    .clickable(onClick = onEdit)
            )
            Text(
                text = "Delete",
                style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFFD32F2F),
                modifier = Modifier
                    .clickable(onClick = onDelete)
            )
        }
    }
}

