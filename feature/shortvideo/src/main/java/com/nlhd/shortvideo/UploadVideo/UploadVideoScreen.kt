package com.nlhd.shortvideo.UploadVideo

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
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
import com.nlhd.core.utils.containerTextFieldLogin

fun getVideoSizeInMB(context: Context, uri: Uri): Double {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    val sizeIndex = cursor?.getColumnIndex(OpenableColumns.SIZE)
    cursor?.moveToFirst()
    val sizeInBytes = sizeIndex?.let { cursor.getLong(it) } ?: 0L
    cursor?.close()

    // Chuyển từ bytes sang MB
    return sizeInBytes / (1024.0 * 1024.0)
}

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadVideoScreen(
    viewModel: UploadVideoViewModel = koinViewModel(),
    onClickBack: () -> Unit,
    onClickBackStack: ()-> Unit
) {

    val context = LocalContext.current
    val keyStore by KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
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
                val size = getVideoSizeInMB(context, uriVideo)
                viewModel.setSize(size)
                viewModel.setVideo(uriVideo)
            }

        }
    )

    val state by viewModel.state.collectAsStateWithLifecycle()
    val size by viewModel.size.collectAsStateWithLifecycle()
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(0.7f)
                    ) {
                        val source = if (state.video.toString().isEmpty()) "Vui lòng chọn source có kích thước dưới 20MB" else "Source: ${state.video}"
                        Text(source, style = AppTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.ExtraLight,
                            fontFamily = Font.fontFamily,
                            color = Color.Gray
                        ),
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        Text("Size: %.2f MB".format(size), style = AppTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.ExtraLight,
                            fontFamily = Font.fontFamily,
                            color = Color.Gray
                        ),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                    AndroidView(
                        modifier = Modifier
                            .size(width = AppTheme.dimens.large3, height = AppTheme.dimens.large3 + AppTheme.dimens.medium3)
                            .clip(RoundedCornerShape(AppTheme.dimens.small2))
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

                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                OutlinedButton(
                    onClick = {
                        launcherVideo.launch("video/*")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentPrice,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(AppTheme.dimens.small2),
                    border = BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
                ) {
                    Text(
                        "Thêm video",
                        style = AppTheme.typography.headlineMedium.copy(
                            fontFamily = Font.fontFamily,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(AppTheme.dimens.small)
                    )
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
                        .height(AppTheme.dimens.large3 + AppTheme.dimens.large),
                    shape = RoundedCornerShape(AppTheme.dimens.small2),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = containerTextFieldLogin,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = contentPrice,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
            }

            item {
                Text(
                    buildAnnotatedString {
                        append("Thêm ảnh tại đây")
                    },
                    style = AppTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            launcherImage.launch("image/*")
                        },
                        modifier = Modifier.weight(0.7f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = containerButtonLightGray,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(AppTheme.dimens.small2),
                        border = BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
                    ) {
                        Text(
                            "Thêm hình ảnh",
                            style = AppTheme.typography.headlineMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(AppTheme.dimens.small)
                        )
                    }
                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                    AsyncImage(
                        model = state.image ?: R.drawable.anhden,
                        contentDescription = null,
                        modifier = Modifier.size(width = AppTheme.dimens.large3, height = AppTheme.dimens.large3 + AppTheme.dimens.medium3)
                            .clip(RoundedCornerShape(AppTheme.dimens.small2)),
                        contentScale = ContentScale.Crop
                    )

                }
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

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadVideoMainScreen(
    paddingValues: PaddingValues,
    viewModel: UploadVideoViewModel = koinViewModel(),
    onClickBack: () -> Unit,
) {

    val context = LocalContext.current
    val keyStore by KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
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
                val size = getVideoSizeInMB(context, uriVideo)
                viewModel.setSize(size)
                viewModel.setVideo(uriVideo)
            }

        }
    )

    val state by viewModel.state.collectAsStateWithLifecycle()
    val size by viewModel.size.collectAsStateWithLifecycle()
    val addVideoState by viewModel.addVideoState.collectAsStateWithLifecycle()

    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.pause()
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(AppTheme.dimens.small2)
    ) {
        stickyHeader {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text("Upload videos", style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                IconButton(
                    onClick = {
                        viewModel.addVideo(token = keyStore, context = context)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_upload),
                        contentDescription = null,
                        tint = contentPrice
                    )
                }
            }

        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(0.7f)
                ) {
                    val source = if (state.video.toString().isEmpty()) "Vui lòng chọn source có kích thước dưới 20MB" else "Source: ${state.video}"
                    Text(source, style = AppTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = Font.fontFamily,
                        color = Color.Gray
                    ),
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    Text("Size: %.2f MB".format(size), style = AppTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = Font.fontFamily,
                        color = Color.Gray
                    ),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                AndroidView(
                    modifier = Modifier
                        .size(width = AppTheme.dimens.large3, height = AppTheme.dimens.large3 + AppTheme.dimens.medium3)
                        .clip(RoundedCornerShape(AppTheme.dimens.small2))
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

            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
            OutlinedButton(
                onClick = {
                    launcherVideo.launch("video/*")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = contentPrice,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(AppTheme.dimens.small2),
                border = BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
            ) {
                Text(
                    "Thêm video",
                    style = AppTheme.typography.headlineMedium.copy(
                        fontFamily = Font.fontFamily,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(AppTheme.dimens.small)
                )
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
                    .height(AppTheme.dimens.large3 + AppTheme.dimens.large),
                shape = RoundedCornerShape(AppTheme.dimens.small2),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = containerTextFieldLogin,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = contentPrice,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
        }

        item {
            Text(
                buildAnnotatedString {
                    append("Thêm ảnh tại đây")
                },
                style = AppTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        launcherImage.launch("image/*")
                    },
                    modifier = Modifier.weight(0.7f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = containerButtonLightGray,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(AppTheme.dimens.small2),
                    border = BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
                ) {
                    Text(
                        "Thêm hình ảnh",
                        style = AppTheme.typography.headlineMedium.copy(
                            fontFamily = Font.fontFamily,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(AppTheme.dimens.small)
                    )
                }
                Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                AsyncImage(
                    model = state.image ?: R.drawable.anhden,
                    contentDescription = null,
                    modifier = Modifier.size(width = AppTheme.dimens.large3, height = AppTheme.dimens.large3 + AppTheme.dimens.medium3)
                        .clip(RoundedCornerShape(AppTheme.dimens.small2)),
                    contentScale = ContentScale.Crop
                )

            }
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
                        .padding(paddingValues), contentAlignment = Alignment.Center) {
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