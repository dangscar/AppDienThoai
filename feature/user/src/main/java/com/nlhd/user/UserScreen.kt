package com.nlhd.user

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.shortvideo.ContentCommonViewModel
import com.nlhd.user.Login.LoginScreen
import com.nlhd.user.SignUp.SignUpScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object LoginScreen

@Serializable
object ProfileScreen

@Serializable
object SignUpScreen

@SuppressLint("ContextCastToActivity")
@Composable
fun UserScreen(
    userViewModel: UserViewModel = koinViewModel(),
    onNavigateAdmin: () -> Unit,
    onClickEditProfile: () -> Unit,
    onClickAddVideo: () -> Unit,
    onClickBack: () -> Unit,
    onClickAvatar: () -> Unit,
    onClickLikedVideo: () -> Unit,
    onClickMyVideo: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val activity = LocalContext.current as ComponentActivity
    val contentCommonViewModel: ContentCommonViewModel = koinViewModel(viewModelStoreOwner = activity)

    val state = userViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            userViewModel.fetchProfile(token = keyStore.value)
        }
    } //Nguyen nhan

    NavHost(
        navController = navController,
        startDestination = if (state.value is UserState.Success) ProfileScreen else LoginScreen //
    ) {
        composable<LoginScreen> {
            LoginScreen(
                onLoginSuccess = { role ->
                    if (role == "admin") {
                        onNavigateAdmin()
                    } else {
                        navController.navigate(ProfileScreen)
                        onClickBack()
                    }

                },
                onSignUp = {
                    navController.navigate(SignUpScreen)
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
                onClickAvatar = onClickAvatar,
                onClickLikedVideo = onClickLikedVideo,
                onClickMyVideo = onClickMyVideo
            )
        }

        composable<SignUpScreen> {
            SignUpScreen(
                onSignUpSuccess = {
                    if (contentCommonViewModel.releaseAll()) {
                        navController.navigate(ProfileScreen) {
                            popUpTo(LoginScreen) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }

                }
            )
        }

    }
}