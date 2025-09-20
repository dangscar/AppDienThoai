package com.nlhd.shortvideo.UploadVideo

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel
import com.nlhd.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadVideoScreen(
    viewModel: UploadVideoViewModel = koinViewModel(),
    onClickBack: () -> Unit,
    onClickBackStack: ()-> Unit
) {

    val launcherImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), // Chọn 1 file
        onResult = { uri: Uri? ->
            uri?.let { uriImage ->
                viewModel.setImage(uriImage)
            }

        }
    )
    val launcherVideo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), // Chọn 1 file
        onResult = { uri: Uri? ->
            uri?.let { uriVideo->
                viewModel.setVideo(uriVideo)
            }

        }
    )
    val context = LocalContext.current
    val keyStore by KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")

    val state by viewModel.state.collectAsStateWithLifecycle()
    val addVideoState by viewModel.addVideoState.collectAsStateWithLifecycle()

    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.pause()
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Upload videos", style = AppTheme.typography.headlineLarge.copy(
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
            )
        },
        containerColor = Color.White,
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentColor = Color.Black,
                contentPadding = PaddingValues(AppTheme.dimens.small3),
                tonalElevation = AppTheme.dimens.small,
                modifier = Modifier.border(AppTheme.dimens.extraSmall, containerSearch)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    OutlinedButton(
                        onClick = onClickBackStack,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = containerButtonLightGray,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(AppTheme.dimens.small2),
                        border = BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
                    ) {
                        Text(
                            "Trở về",
                            style = AppTheme.typography.headlineMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(AppTheme.dimens.small)
                        )
                    }
                    Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                    OutlinedButton(
                        onClick = {
                            viewModel.addVideo(token = keyStore, context = context)
                        },
                        modifier = Modifier.weight(1f),
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
    ) { innerPadding->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(AppTheme.dimens.small2)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = state.image ?: R.drawable.anhden,
                        contentDescription = null,
                        modifier = Modifier.size(AppTheme.dimens.large3),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                    Button(
                        onClick = { launcherImage.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Chọn ảnh", style = AppTheme.typography.headlineMedium.copy(
                            color = Color.White
                        ))
                    }
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AndroidView(
                        modifier = Modifier
                            .size(AppTheme.dimens.large3)
                            .background(color = Color.Black)
                            .pointerInput(key1 = Unit) {
                                detectTapGestures(
                                    onTap = {
                                        viewModel.actionVideo()
                                    }
                                )
                            },
                        factory = {
                            PlayerView(it).also {
                                it.useController = false
                                it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                            }
                        },
                        update = {
                            it.player = viewModel.getExoPlayer()
                        }
                    )
                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                    Button(
                        onClick = { launcherVideo.launch("video/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Chọn video", style = AppTheme.typography.headlineMedium.copy(
                            color = Color.White
                        ))
                    }
                }

            }
            item {
                Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
                //Description
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                    val desLenght = if (state.caption == null) "0" else state.caption!!.length.toString()
                    Text(
                        desLenght,
                        style = AppTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                OutlinedTextField(
                    value = state.caption ?: "",
                    onValueChange = {
                        viewModel.setCaption(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.dimens.large2),
                    shape = RoundedCornerShape(AppTheme.dimens.small2),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7F56D9),
                        unfocusedBorderColor = Color.LightGray
                    ),
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
            }
            when (addVideoState) {
                is UploadVideoState.Error -> {
                    item {
                        Text((addVideoState as UploadVideoState.Error).message, style = AppTheme.typography.headlineMedium.copy(
                            color = contentPrice,
                            fontWeight = FontWeight.SemiBold
                        ))
                    }
                }
                UploadVideoState.Loading -> {
                    item {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = contentPrice
                            )
                        }
                    }
                }
                is UploadVideoState.Success -> {
                    val data = (addVideoState as UploadVideoState.Success).data
                    item {
                        Text(data.message, style = AppTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        ))
                    }
                    onClickBack()
                }

                UploadVideoState.Idle -> {

                }
            }
        }
    }
}