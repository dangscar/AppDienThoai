package com.nlhd.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.borderTextField
import com.nlhd.core.utils.containerButtonLogout
import com.nlhd.core.utils.containerLogin
import com.nlhd.core.utils.containerTextFieldLogin
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.user.components.ButtonProfile
import com.nlhd.user.components.CardInfo
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onClickBack: () -> Unit,
    onClickEditProfile: () -> Unit
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val state = profileViewModel.state.collectAsStateWithLifecycle()
    val stateLogout = profileViewModel.stateLogout.collectAsStateWithLifecycle()
    LaunchedEffect(keyStore.value) {
        if (keyStore.value.isNotEmpty()) {
            profileViewModel.fetchProfile(token = keyStore.value)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text("My profile", style = AppTheme.typography.headlineLarge.copy(
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
                )
            )
        }
    ) { innerPadding->
        when (state.value) {
            is ProfileState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((state.value as ProfileState.Error).message, style = AppTheme.typography.titleMedium)
                }
                onClickBack()
            }
            ProfileState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Color.Black
                    )
                }
            }
            is ProfileState.Success -> {
                val profileResponse = (state.value as ProfileState.Success).data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(bottom = innerPadding.calculateBottomPadding(), start = AppTheme.dimens.small, end = AppTheme.dimens.small),
                    verticalArrangement = Arrangement.SpaceBetween,

                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = "https://i.pinimg.com/1200x/69/78/19/69781905dd57ba144ab71ca4271ab294.jpg",
                                contentDescription = null,
                                modifier = Modifier
                                    .size(AppTheme.dimens.large2)
                                    .clip(CircleShape)
                                    .background(color = containerTextFieldLogin),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                            Column (
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(profileResponse.user.name, style = AppTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = Font.fontFamily
                                ))
                                Text(profileResponse.user.email, style = AppTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.ExtraLight,
                                    fontFamily = Font.fontFamily,
                                    color = Color.Gray
                                ),
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        ButtonProfile(
                            "Chỉnh sửa thông tin cá nhân",
                            onClick = onClickEditProfile
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        CardInfo()
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        Text(
                            "Hỗ trợ",
                            style = AppTheme.typography.bodyMedium.copy(
                                color = Color.Black,
                                fontFamily = Font.fontFamily,
                                fontWeight = FontWeight.ExtraLight
                            )
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        ButtonProfile(
                            "Đến trung tâm hỗ trợ",
                            onClick = {

                            }
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    }

                    item {
                        OutlinedButton(
                            onClick = {
                                profileViewModel.logout(keyStore.value)
                            },
                            shape = RoundedCornerShape(AppTheme.dimens.small3),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = containerButtonLogout,
                                contentColor = Color.White
                            ),
                            border = _root_ide_package_.androidx.compose.foundation.BorderStroke(
                                width = AppTheme.dimens.extraSmall,
                                color = borderTextField
                            ),
                        ) {
                            Text(
                                "Đăng xuất",
                                style = AppTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small),
                                textAlign = TextAlign.Center,

                                )
                        }
                    }
                }
            }
        }

        when (stateLogout.value) {
            is LogoutState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((stateLogout.value as LogoutState.Error).message, style = AppTheme.typography.titleMedium)
                }
            }
            LogoutState.Loading -> {

            }
            is LogoutState.Success -> {
                val logoutResponse = (stateLogout.value as LogoutState.Success).data
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(logoutResponse.message, style = AppTheme.typography.titleMedium)
                }
                profileViewModel.clearKeyStore(context)
            }

            LogoutState.LogoutSuccess -> {
                onClickBack()
            }
        }
    }



}