package com.nlhd.manage_product.ColorProductScreen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.borderColor
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.contentBlueTopBar
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.manage_product.components.InputNumber
import com.nlhd.manage_product.components.InputText
import com.nlhd.manage_product.components.Title
import com.nlhd.manage_product.components.Validated
import com.nlhd.manage_product.components.Warning
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditColorProductScreen(
    viewModel: EditColorProductViewModel = koinViewModel(),
    colorId: Int,
    onClickBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val updateColorState by viewModel.updateProductState.collectAsStateWithLifecycle()
    val manageColorAction by viewModel.manageColorAction.collectAsStateWithLifecycle()
    val isExpand by viewModel.isExpand.collectAsStateWithLifecycle()
    val isValidColor by viewModel.isValidColor.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val interactionSource = remember { MutableInteractionSource() }

    val isValidatePrice = manageColorAction.price.isEmpty() || manageColorAction.price.isBlank() ///
    val isValidateColor = manageColorAction.color.length < 2
    val isValidateValueColor = !isValidColor
    val isValidateImage = manageColorAction.image == null
    val color =try {
        Color(manageColorAction.valueColor.toColorInt()).also { viewModel.setValidColor(true) }
    } catch (e: IllegalArgumentException) {
        viewModel.setValidColor(false)
        Color.LightGray
    }

    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            viewModel.editColorProduct(keyStore.value, colorId)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri->
            uri?.let {
                viewModel.uploadImage(uri)
                Log.d("AAA", uri.toString())
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit color product", style = AppTheme.typography.headlineLarge.copy(
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
                            if (isValidatePrice || isValidateColor || isValidateValueColor || isValidateImage) {
                                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                                return@OutlinedButton
                            }
                            val isValidatePriceNumber = manageColorAction.price.toDouble() < 100000 || manageColorAction.price.toDouble() > 200000000
                            if (isValidatePriceNumber) {
                                Toast.makeText(context, "Vui lòng nhập đúng thông tin", Toast.LENGTH_SHORT).show()
                                return@OutlinedButton
                            }
                            /// Xu ly
                            viewModel.updateColorProduct(keyStore.value, context, colorId)
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

        when (state) {
            is EditColorProductState.Error -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text((state as EditColorProductState.Error).message, style = AppTheme.typography.titleMedium)
                }
            }
            EditColorProductState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            is EditColorProductState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(AppTheme.dimens.small2)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppTheme.dimens.extraLarge)
                                .border(
                                    width = AppTheme.dimens.border,
                                    color = borderColor,
                                    shape = RoundedCornerShape(AppTheme.dimens.small2)
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            launcher.launch("image/*")
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (manageColorAction.image == null) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = "Upload",
                                        tint = Color.Black.copy(alpha = 0.6f),
                                        modifier = Modifier.size(AppTheme.dimens.medium3)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        buildAnnotatedString {
                                            append("Đăng tải ảnh tại đây, hoặc ")
                                            withStyle(style = SpanStyle(color = Color(0xFF7F56D9))) {
                                                append("nhấp vào")
                                            }
                                            append(" để lấy ảnh")
                                        },
                                        style = AppTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                                    Text(
                                        "Giới hạn tệp ảnh, lưu ý tối đa ",
                                        style = AppTheme.typography.labelMedium.copy(
                                            color = Color.Gray
                                        ),
                                    )
                                    Text(
                                        "5MB",
                                        style = AppTheme.typography.labelMedium.copy(
                                            color = Color.Red
                                        ),
                                    )
                                }
                            } else {
                                val conditionText = manageColorAction.image.toString()
                                val image = if (conditionText.contains("com.android") || conditionText == "null" || conditionText == "") {
                                    manageColorAction.image
                                } else {
                                    "${Utils.BASE_URL}/" + manageColorAction.image
                                }
                                AsyncImage(

                                    model = image,
                                    contentDescription = "Image",
                                    modifier = Modifier.size(AppTheme.dimens.extraLarge),
                                    contentScale = ContentScale.Crop
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        if (manageColorAction.image != null) {
                            Text(
                                "Ảnh đã tải",
                                style = AppTheme.typography.bodyMedium.copy(
                                    color = contentBlueTopBar,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = Font.fontFamily
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                "Ảnh chưa tải",
                                style = AppTheme.typography.bodyMedium.copy(
                                    color = contentPrice,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = Font.fontFamily
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
                    }
                    item {
                        //Price, Status
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
                                        append("Giá")
                                        withStyle(style = SpanStyle(color = contentPrice)) {
                                            if (isValidatePrice) {
                                                append("*")
                                            } else {
                                                val isValidatePriceNumber = manageColorAction.price.toDouble() < 100000 || manageColorAction.price.toDouble() > 200000000
                                                if (isValidatePriceNumber) {
                                                    append("*")
                                                }
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
                                    number = manageColorAction.price,
                                    onValueChange = {
                                        if (it.all { char-> char.isDigit()  }) {
                                            viewModel.setPrice(it)
                                        }

                                    }
                                )
                                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                                if (isValidatePrice) {
                                    Warning("Chưa nhập giá")
                                } else {
                                    val isValidatePriceNumber = manageColorAction.price.toDouble() < 100000 || manageColorAction.price.toDouble() > 200000000
                                    if (isValidatePriceNumber) {
                                        Warning("Nhập 100K-200.000K")
                                    } else {
                                        Validated()
                                    }
                                }
                            }


                            Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                            Column(
                                modifier = Modifier.weight(0.5f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    buildAnnotatedString {
                                        append("Trạng thái")
                                    },
                                    style = AppTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            viewModel.setExpand(true)
                                        }
                                ) {
                                    OutlinedTextField(
                                        enabled = false,
                                        readOnly = true,
                                        value = manageColorAction.status.first,
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
                                    expanded = isExpand,
                                    onDismissRequest = {
                                        viewModel.setExpand(false)
                                    },
                                    border = BorderStroke(width = AppTheme.dimens.border, color = Color.LightGray),
                                    modifier = Modifier.fillMaxWidth(0.5f),

                                    ) {
                                    viewModel.statusList.forEach {
                                        DropdownMenuItem(
                                            text = { Text(it.first) },
                                            onClick = {
                                                viewModel.setStatus(Pair(first = it.first, second = it.second))
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
                    }

                    item {
                        Title("Màu sắc sản phẩm")
                        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                        //Color
                        InputText(
                            title = "Màu sắc",
                            name = manageColorAction.color,
                            isValidate = isValidateColor,
                            onValueChange = {
                                viewModel.setColor(it)
                            }
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        if (isValidateColor) {
                            Warning("Nhập ít nhất 2 ký tự")
                        }
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        //ColorValue
                        InputText(
                            title = "Giá trị màu sắc",
                            name = manageColorAction.valueColor,
                            isValidate = isValidateValueColor,
                            onValueChange = {
                                if (it.isNotEmpty() && it[0] == '#') {
                                    viewModel.setColorValue(it.uppercase())
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        if (!isValidColor) {
                            Warning("Mã màu không hợp lệ. Ví dụ: #FF5733")
                        }
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    }
                }
            }
        }

        when (updateColorState) {
            is UpdateColorProductState.Error -> {
                Toast.makeText(context, (updateColorState as UpdateColorProductState.Error).message, Toast.LENGTH_SHORT).show()
            }
            UpdateColorProductState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            UpdateColorProductState.Pending -> {}
            is UpdateColorProductState.Success -> {
                Toast.makeText(context, if( (updateColorState as UpdateColorProductState.Success).data.message == "Success") "Cập nhật thành công" else "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                Log.d("AAA", (updateColorState as UpdateColorProductState.Success).data.message)
                onClickBack()
            }
        }

    }
}