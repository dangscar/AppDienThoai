package com.nlhd.manage_product.EditProductScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.manage_product.AddProductScreen.ManageCategoryState
import com.nlhd.manage_product.components.InputNumber
import com.nlhd.manage_product.components.InputText
import com.nlhd.manage_product.components.Validated
import com.nlhd.manage_product.components.Warning
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    viewModel: EditProductViewModel = koinViewModel(),
    id: Int,
    onClickBack: () -> Unit
) {

    val manageCategoryState = viewModel.manageCategoryState.collectAsStateWithLifecycle()
    val category = viewModel.category.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }

    val isExpandCategory = viewModel.isExpandCategory.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")

    val editProductAction = viewModel.editProductAction.collectAsStateWithLifecycle()
    val updateProductState = viewModel.updateProductState.collectAsStateWithLifecycle()

    val isValidateName = editProductAction.value.name.length < 6
    val isValidateScreenSize = editProductAction.value.screenSize.isEmpty() || editProductAction.value.screenSize.isBlank()
    val isValidateCpu = editProductAction.value.cpu.length < 5 || editProductAction.value.cpu.length > 50
    val isValidateBattery = editProductAction.value.battery.isEmpty() || editProductAction.value.battery.isBlank()
    val isValidateCamera = editProductAction.value.camera.length < 2 || editProductAction.value.camera.length > 100
    val isValidateOs = editProductAction.value.os.length < 2 || editProductAction.value.os.length > 50
    val isPass = !isValidateName && !isValidateScreenSize && !isValidateCpu && !isValidateBattery && !isValidateCamera && !isValidateOs
    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            viewModel.getProduct(keyStore.value, id)
        }
    }

    LaunchedEffect(keyStore.value) {
        if (keyStore.value.isNotEmpty()) {
            viewModel.getCategory(keyStore.value)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit product", style = AppTheme.typography.headlineLarge.copy(
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
                            if (!isPass) {
                                Toast.makeText(context, "Chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show()
                                return@OutlinedButton
                            }
                            viewModel.updateProduct(keyStore.value, id)
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
                .padding(innerPadding)
                .padding(AppTheme.dimens.small2)
        ) {
            item {
                val product = editProductAction.value
                InputText(
                    title = "Tên sản phẩm",
                    name = product.name,
                    isValidate = isValidateName,
                    onValueChange = viewModel::setName
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                if (isValidateName) {
                    Warning("Nhập ít nhất 6 ký tự")
                    Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
                }

                //Description
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        buildAnnotatedString {
                            append("Mô tả sản phẩm ")
                        },
                        style = AppTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                    val desLenght = if (editProductAction.value.description == null) "0" else editProductAction.value.description!!.length.toString()
                    Text(
                        desLenght,
                        style = AppTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                OutlinedTextField(
                    value = product.description ?: "",
                    onValueChange = viewModel::setDescription,
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

                //ScreenSize & Cpu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            buildAnnotatedString {
                                append("Màn hình")
                                withStyle(style = SpanStyle(color = contentPrice)) {
                                    if (isValidateScreenSize) {
                                        append("*")
                                    } else {
                                        /*val isValidateScreenSizeNumber = manageProductActionState.value.screenSize.toDouble() < 0 || manageProductActionState.value.screenSize.toDouble() > 20
                                        if (isValidateScreenSizeNumber) {
                                            append("*")
                                        }*/
                                    }

                                }
                            },
                            style = AppTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        InputNumber(
                            number = product.screenSize,
                            onValueChange = {
                                if (it.all { char -> char.isDigit() || char == '.' }) {
                                    viewModel.setScreenSize(it)
                                }

                            }
                        )

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        if (isValidateScreenSize) {
                            Warning("Chưa nhập ký tự")
                        } else {
                            /*val isValidateScreenSizeNumber = manageProductActionState.value.screenSize.toDouble() < 0 || manageProductActionState.value.screenSize.toDouble() > 20
                            if (isValidateScreenSizeNumber) {
                                Warning("Nhập từ 0 đến 20 inch")
                            } else {
                                Validated()
                            }*/

                        }

                    }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            buildAnnotatedString {
                                append("Cpu")
                                withStyle(style = SpanStyle(color = contentPrice)) {
                                    if (isValidateCpu) {
                                        append("*")
                                    }
                                }
                            },
                            style = AppTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        OutlinedTextField(
                            value = product.cpu,
                            onValueChange = viewModel::setCpu,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppTheme.dimens.small2),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7F56D9),
                                unfocusedBorderColor = Color.LightGray,
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        if (isValidateCpu) {
                            Warning("Nhập từ 5 đến 50 ký tự")
                        } else {
                            Validated()
                        }

                    }
                }
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            buildAnnotatedString {
                                append("Pin")
                                withStyle(style = SpanStyle(color = contentPrice)) {
                                    if (isValidateBattery) {
                                        append("*")
                                    } else {
                                        /*val isValidateBatteryNumber = manageProductActionState.value.battery.toDouble() < 0 || manageProductActionState.value.battery.toDouble() > 20000
                                        if (isValidateBatteryNumber) {
                                            append("*")
                                        }*/
                                    }

                                }
                            },
                            style = AppTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        OutlinedTextField(
                            value = product.battery,
                            onValueChange = {
                                if (it.all { char-> char.isDigit() }) {
                                    viewModel.setBattery(it)
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(AppTheme.dimens.small2),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7F56D9),
                                unfocusedBorderColor = Color.LightGray
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        if (isValidateBattery) {
                            Warning("Chưa có nhập ký tự")
                        } else {
                            /*val isValidateBatteryNumber = manageProductActionState.value.battery.toDouble() < 0 || manageProductActionState.value.battery.toDouble() > 20000
                            if (isValidateBatteryNumber) {
                                Warning("Nhập từ 0 đến 20000")
                            } else {
                                Validated()
                            }*/
                        }

                    }


                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            buildAnnotatedString {
                                append("Camera")
                                withStyle(style = SpanStyle(color = contentPrice)) {
                                    if (isValidateCamera) {
                                        append("*")
                                    }
                                }
                            },
                            style = AppTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        OutlinedTextField(
                            value = product.camera,
                            onValueChange = viewModel::setCamera,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppTheme.dimens.small2),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7F56D9),
                                unfocusedBorderColor = Color.LightGray,
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        if (isValidateCamera) {
                            Warning("Nhập từ 2 đến 100 ký tự")
                        } else {
                            Validated()
                        }


                    }

                }
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            buildAnnotatedString {
                                append("Hệ điều hành")
                                withStyle(style = SpanStyle(color = contentPrice)) {
                                    if (isValidateOs) {
                                        append("*")
                                    }
                                }
                            },
                            style = AppTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        OutlinedTextField(
                            value = product.os,
                            onValueChange = viewModel::setOs,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppTheme.dimens.small2),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7F56D9),
                                unfocusedBorderColor = Color.LightGray,
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        if (isValidateOs) {
                            Warning("Nhập từ 2 đến 50 ký tự")
                        } else {
                            Validated()
                        }


                    }


                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            buildAnnotatedString {
                                append("Danh mục")
                            },
                            style = AppTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        if (manageCategoryState.value is ManageCategoryState.Success) {
                            val categories = (manageCategoryState.value as ManageCategoryState.Success).data.categories
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        viewModel.setExpandCategory(true)
                                    }
                            ) {

                                OutlinedTextField(
                                    enabled = false,
                                    readOnly = true,
                                    value = category.value.name,
                                    onValueChange = {},
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppTheme.dimens.small2),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF7F56D9),
                                        unfocusedBorderColor = Color.LightGray,
                                        disabledBorderColor = Color.LightGray,
                                        disabledTextColor = Color.Black
                                    ),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                                Text(
                                    "A",
                                    style = AppTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Transparent
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                            DropdownMenu(
                                expanded = isExpandCategory.value,
                                onDismissRequest = {
                                    viewModel.setExpandCategory(false)
                                },
                                border = BorderStroke(width = AppTheme.dimens.border, color = Color.LightGray),
                                modifier = Modifier.fillMaxWidth(0.5f),

                                ) {

                                categories.forEach { category->
                                    DropdownMenuItem(
                                        text = { Text(category.name) },
                                        onClick = {
                                            viewModel.setCategoryAttr(category)
                                            viewModel.setExpandCategory(false)
                                        }
                                    )
                                }

                            }
                        }

                    }

                }

            }
        }
        when (updateProductState.value) {
            is UpdateProductState.Error -> {
                Toast.makeText(context, (updateProductState.value as UpdateProductState.Error).message, Toast.LENGTH_SHORT).show()
            }
            UpdateProductState.Pending -> {}
            is UpdateProductState.Success -> {
                onClickBack()
            }
        }
    }
}