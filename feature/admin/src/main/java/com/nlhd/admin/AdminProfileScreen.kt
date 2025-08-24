package com.nlhd.admin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nlhd.admin.components.ButtonProfile
import com.nlhd.admin.components.CardInfo
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.borderTextField
import com.nlhd.core.utils.containerButtonLogout
import com.nlhd.core.utils.containerTextFieldLogin
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(
    viewModel: AdminProfileViewModel = koinViewModel(),
    onClickBackAdmin: () -> Unit,
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val state = viewModel.state.collectAsStateWithLifecycle()
    val stateLogout = viewModel.stateLogout.collectAsStateWithLifecycle()
    LaunchedEffect(
        key1 = keyStore.value
    ) {
        if (keyStore.value.isNotEmpty()) {
            viewModel.getAdminProfile(keyStore.value)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text("Admin Profile", style = AppTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onClickBackAdmin
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding->
        when (state.value) {

            is AdminProfileState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(((state.value) as AdminProfileState.Error).message, style = AppTheme.typography.titleMedium)
                }
            }
            AdminProfileState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Color.Black
                    )
                }
            }
            is AdminProfileState.Success -> {
                val profileResponse = (state.value as AdminProfileState.Success).data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = AppTheme.dimens.medium)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = R.drawable.ic_profile,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(AppTheme.dimens.large2)
                                    .clip(CircleShape)
                                    .background(color = containerTextFieldLogin)
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
                    }

                    item {
                        ButtonProfile("Chỉnh sửa thông tin cá nhân")
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    }

                    item {
                        CardInfo()
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    }

                    item {
                        Text(
                            "Hỗ trợ",
                            style = AppTheme.typography.bodyMedium.copy(
                                color = Color.Black,
                                fontFamily = Font.fontFamily,
                                fontWeight = FontWeight.ExtraLight
                            )
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        ButtonProfile("Đến trung tâm hỗ trợ")
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    }

                    item {
                        OutlinedButton(
                            onClick = {
                                viewModel.logout(keyStore.value)
                            },
                            shape = RoundedCornerShape(AppTheme.dimens.small3),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = containerButtonLogout,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(
                                width = AppTheme.dimens.extraSmall,
                                color = borderTextField
                            )
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

            is AdminLogoutState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((stateLogout.value as AdminLogoutState.Error).message, style = AppTheme.typography.titleMedium)
                }
            }
            AdminLogoutState.Loading -> {

            }
            AdminLogoutState.LogoutSuccess -> {
                onClickBack()
            }
            is AdminLogoutState.Success -> {
                val logoutResponse = (stateLogout.value as AdminLogoutState.Success).data
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(logoutResponse.message, style = AppTheme.typography.titleMedium)
                }
                viewModel.clearKeyStore(context)
            }
        }
    }

}