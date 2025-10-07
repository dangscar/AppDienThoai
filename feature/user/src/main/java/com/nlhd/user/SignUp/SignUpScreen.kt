package com.nlhd.user.SignUp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerTextFieldLogin
import com.nlhd.core.utils.contentPrice
import com.nlhd.user.Login.LoginState
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = koinViewModel(),
    onSignUpSuccess: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sign up", style = AppTheme.typography.headlineLarge.copy(
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
                value = state.name,
                onValueChange = viewModel::setName,
                singleLine = true,
                placeholder = {
                    Text("Name", style = AppTheme.typography.labelMedium.copy(
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
                value = state.email,
                onValueChange = viewModel::setEmail,
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
                value = state.password,
                onValueChange = viewModel::setPassword,
                singleLine = true,
                placeholder = {
                    Text("Password", style = AppTheme.typography.labelMedium.copy(
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

            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::setConfirmPassword,
                singleLine = true,
                placeholder = {
                    Text("Password Confirmation", style = AppTheme.typography.labelMedium.copy(
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
                    //Sign up
                    viewModel.signUp()
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

            when (signUpState) {
                is SignUpState.Error -> {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                    Text("*Có lỗi xảy ra, vui lòng thử lại", style = AppTheme.typography.labelMedium.copy(
                        fontFamily = Font.fontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Red
                    ))
                }
                SignUpState.Idle -> {}
                SignUpState.Loading -> {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                    Box(modifier = Modifier
                        .fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = contentPrice
                        )
                    }
                }
                is SignUpState.Success -> {
                    val token = (signUpState as SignUpState.Success).data.token
                    viewModel.saveToken(context, token)
                    onSignUpSuccess()
                }
            }


            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            Row {
                Text("Bạn đã có tài khoản?", style = AppTheme.typography.labelMedium.copy(
                    fontFamily = Font.fontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),
                )
                Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                Text("Đăng nhập", style = AppTheme.typography.labelMedium.copy(
                    fontFamily = Font.fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = contentPrice
                ))
            }
        }
    }



}
