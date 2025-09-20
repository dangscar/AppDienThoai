package com.nlhd.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nlhd.domain.entity.profile.User
import com.nlhd.keystore.KeyStoreManager
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object LoginScreen

@Serializable
object ProfileScreen

@Serializable
object Admin

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun UserScreen(
    userViewModel: UserViewModel = koinViewModel(),
    onNavigateAdmin: () -> Unit,
    onClickEditProfile: () -> Unit,
    onClickAddVideo: () -> Unit,
    onClickBack: () -> Unit,
    onClickAvatar: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")

    val state = userViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            userViewModel.fetchProfile(token = keyStore.value)
        }
    } //Nguyen nhan

    NavHost(
        navController = navController,
        startDestination = if (state.value is UserState.Success) ProfileScreen else LoginScreen
    ) {
        composable<LoginScreen> {
            LoginScreen(
                onLoginSuccess = { role->
                    if (role == "admin") {
                        onNavigateAdmin()
                    } else {
                        navController.navigate(ProfileScreen)
                    }

                }
            )
        }

        composable<ProfileScreen> {
            ProfileScreen(
                onClickBack = {
                    onClickBack()
                },
                onClickEditProfile = onClickEditProfile,
                onClickAddVideo = onClickAddVideo,
                onClickAvatar = onClickAvatar
            )
        }
    }
}