package com.nlhd.address

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nlhd.address.components.InputNumber
import com.nlhd.address.components.InputText
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.colorDivide
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.containerConfirm
import com.nlhd.core.utils.containerUnConfirm
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    viewModel: AddressViewModel = koinViewModel(),
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val state = viewModel.state.collectAsStateWithLifecycle()
    val stateAdd = viewModel.stateAdd.collectAsStateWithLifecycle()

    val isValidateName = state.value.name.length < 8 || state.value.name.length > 50
    val isValidatePhone = state.value.phone.length < 9 || state.value.phone.length > 12
    val isValidateAddress = state.value.address.isEmpty() || state.value.address.length > 255
    val isPass = !isValidateName && !isValidatePhone && !isValidateAddress

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = containerButtonLightGray,
        topBar = {
            TopAppBar(
                title = {
                    Text("Add new address", style = AppTheme.typography.headlineLarge.copy(
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
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onClickBack()
                        }
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
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider(color = colorDivide)
                Text(
                    "By clicking Save, you acknowledge that you have read the Privacy Policy",
                    style = AppTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    ),
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.small3)
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                Button(
                    onClick = {
                        if (isPass) {
                            viewModel.addAddress(token = keyStore.value)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPass) containerConfirm else containerUnConfirm
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 0.dp,
                            start = AppTheme.dimens.small2,
                            end = AppTheme.dimens.small2,
                            bottom = AppTheme.dimens.small3
                        )
                ) {
                    Text(
                        "Save",
                        style = AppTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.small2)
                .padding(innerPadding)
        ) {
            InputText(
                title = "Họ tên",
                name = state.value.name,
                isValidate = isValidateName,
                onValueChange = {
                    viewModel.setName(it)
                }
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
            InputNumber(
                title = "Số điện thoại",
                number = state.value.phone,
                isValidate = isValidatePhone,
                onValueChange = {
                    viewModel.setPhone(it)
                }
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
            InputText(
                title = "Địa chỉ",
                name = state.value.address,
                isValidate = isValidateAddress,
                onValueChange = {
                    viewModel.setAddress(it)
                }
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

            //Description
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    buildAnnotatedString {
                        append("Mô tả")
                    },
                    style = AppTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
                //val desLenght = if (manageProductActionState.value.description == null) "0" else manageProductActionState.value.description!!.length.toString()
                val desLenght = "Tối đa 255"
                Text(
                    desLenght,
                    style = AppTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
            val description = state.value.description ?: ""
            OutlinedTextField(
                value = description,
                onValueChange = {
                    viewModel.setDescription(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimens.large2),
                shape = RoundedCornerShape(AppTheme.dimens.small2),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7F56D9),
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
            )
        }
        when (stateAdd.value) {
            is AddressState.Error -> {
                val message = (stateAdd.value as AddressState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            AddressState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .clickable(enabled = false) {}
                        .background(Color.Black.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            AddressState.Pending -> {

            }
            is AddressState.Success -> {
                val message = (stateAdd.value as AddressState.Success).data.message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                onClickBack()
            }
        }


    }
}

@Preview
@Composable
private fun AddressPre() {
    InputText(
        title = "Họ tên",
        name = "Nguyễn Đăng",
        isValidate = false,
        onValueChange = {

        }
    )
}

