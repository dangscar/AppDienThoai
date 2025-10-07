package com.nlhd.user.Login

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import org.koin.androidx.compose.koinViewModel
import com.nlhd.core.utils.containerTextFieldLogin
import com.nlhd.core.utils.contentPrice

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: (String) -> Unit,
    onSignUp: () -> Unit
) {
    val context = LocalContext.current

    val state = loginViewModel.state.collectAsStateWithLifecycle()
    val email = loginViewModel.email.collectAsStateWithLifecycle()
    val password = loginViewModel.password.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Log in", style = AppTheme.typography.headlineLarge.copy(
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
        },
        containerColor = Color.White
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(AppTheme.dimens.medium)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = email.value,
                onValueChange = { loginViewModel.setEmail(it) },
                singleLine = true,
                placeholder = {
                    Text("Email", style = AppTheme.typography.labelMedium.copy(
                        fontFamily = Font.fontFamily,
                        fontWeight = FontWeight.ExtraLight
                    ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                shape = RoundedCornerShape(AppTheme.dimens.small3),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = containerTextFieldLogin,
                    unfocusedContainerColor = containerTextFieldLogin
                ),
                modifier = Modifier.fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

            OutlinedTextField(
                value = password.value,
                onValueChange = { loginViewModel.setPassword(it) },
                singleLine = true,
                placeholder = {
                    Text("Mật khẩu", style = AppTheme.typography.labelMedium.copy(
                        fontFamily = Font.fontFamily,
                        fontWeight = FontWeight.ExtraLight
                    ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                shape = RoundedCornerShape(AppTheme.dimens.small3),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = containerTextFieldLogin,
                    unfocusedContainerColor = containerTextFieldLogin
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            Button(
                onClick = {
                    loginViewModel.login()
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = contentPrice),
                shape = RoundedCornerShape(AppTheme.dimens.small3)
            ) {
                Text("Tiếp tục", style = AppTheme.typography.labelMedium.copy(
                    fontFamily = Font.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                    modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
                    textAlign = TextAlign.Center
                )
            }

            when (state.value) {
                is LoginState.Error -> {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                    Text("*Đăng nhập sai thông tin, vui lòng thử lại", style = AppTheme.typography.labelMedium.copy(
                        fontFamily = Font.fontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Red
                    ))
                }
                LoginState.Loading -> {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                    Box(modifier = Modifier
                        .fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = contentPrice
                        )
                    }
                }
                is LoginState.Success -> {
                    val token = (state.value as LoginState.Success).data.token
                    val role = (state.value as LoginState.Success).data.user.role
                    loginViewModel.saveToken(context, token, role)
                }

                is LoginState.SaveTokenSuccess -> {
                    onLoginSuccess((state.value as LoginState.SaveTokenSuccess).role)
                    loginViewModel.setState(LoginState.Loading)
                }

                LoginState.Idle -> {}
            }


            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            Row {
                Text("Bạn chưa có tài khoản?", style = AppTheme.typography.labelMedium.copy(
                    fontFamily = Font.fontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),
                )
                Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                Text(
                    "Đăng ký",
                    style = AppTheme.typography.labelMedium.copy(
                        fontFamily = Font.fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = contentPrice
                    ),
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                onSignUp()
                            }
                        )
                    }
                )
            }
        }
    }



}
