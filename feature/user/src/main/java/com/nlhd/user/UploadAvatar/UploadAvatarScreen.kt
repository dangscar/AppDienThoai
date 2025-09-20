package com.nlhd.user.UploadAvatar

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.R
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadAvatarScreen(
    viewModel: UploadAvatarViewModel = koinViewModel(),
    onClickBack: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyStore by KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val uploadAvatarState by viewModel.uploadAvatarState.collectAsStateWithLifecycle()


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri->
            uri?.let { uriImage->
                viewModel.setImage(uriImage)
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("My avatar", style = AppTheme.typography.headlineLarge.copy(
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
                        onClick = onClickBack,
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
                            viewModel.uploadAvatar(keyStore, context)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = state.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(AppTheme.dimens.extraLarge)
                    .border(width = AppTheme.dimens.extraSmall, color = Color.Black, shape = CircleShape)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
            Button(
                onClick = {
                    launcher.launch("image/*")
                },
                modifier = Modifier.padding(AppTheme.dimens.small),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text(
                    "Upload",
                    style = AppTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
            when (uploadAvatarState) {
                is UploadAvatarState.Error -> {
                    Text((uploadAvatarState as UploadAvatarState.Error).message, style = AppTheme.typography.titleMedium.copy(
                        color = Color.White
                    ))
                }
                UploadAvatarState.Idle -> {}
                UploadAvatarState.Loading -> {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = contentPrice
                        )
                    }
                }
                is UploadAvatarState.Success -> {
                    Text((uploadAvatarState as UploadAvatarState.Success).data.message, style = AppTheme.typography.titleMedium.copy(
                        color = Color.White
                    ))
                    onClickBack()
                }
            }
        }
    }
}