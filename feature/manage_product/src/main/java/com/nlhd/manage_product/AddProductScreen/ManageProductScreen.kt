package com.nlhd.manage_product.AddProductScreen

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.contentBlueTopBar
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel
import androidx.core.graphics.toColorInt
import com.nlhd.core.utils.borderColor
import com.nlhd.manage_product.components.InputNumber
import com.nlhd.manage_product.components.InputText
import com.nlhd.manage_product.components.Title
import com.nlhd.manage_product.components.Validated
import com.nlhd.manage_product.components.Warning

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    viewModel: ManageProductViewModel = koinViewModel(),
    onClickBack: () -> Unit
) {
    val isExpand = viewModel.isExpand.collectAsStateWithLifecycle()
    val isExpandCategory = viewModel.isExpandCategory.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val state = viewModel.state.collectAsStateWithLifecycle()
    val manageCategoryState = viewModel.manageCategoryState.collectAsStateWithLifecycle()
    val manageProductActionState = viewModel.manageProductAction.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }
    val isValidColor = viewModel.isValidColor.collectAsStateWithLifecycle()
    val color = try {
        Color(manageProductActionState.value.valueColor.toColorInt()).also { viewModel.setValidColor(true) }
    } catch (e: IllegalArgumentException) {
        viewModel.setValidColor(false)
        Color.LightGray
    }

    val category = viewModel.category.collectAsStateWithLifecycle()

    //isValidate
    val isValidateName = manageProductActionState.value.name.length < 6
    val isValidatePrice = manageProductActionState.value.price.isEmpty() || manageProductActionState.value.price.isBlank() ///
    val isValidateColor = manageProductActionState.value.color.length < 2
    val isValidateValueColor = !isValidColor.value
    val isValidateScreenSize = manageProductActionState.value.screenSize.isEmpty() || manageProductActionState.value.screenSize.isBlank() ///
    val isValidateCpu = manageProductActionState.value.cpu.length < 5 || manageProductActionState.value.cpu.length > 50
    val isValidateRam = manageProductActionState.value.ram.isEmpty() || manageProductActionState.value.ram.isBlank() ///
    val isValidateStorage = manageProductActionState.value.storage.isEmpty() || manageProductActionState.value.storage.isBlank() ///
    val isValidateBattery = manageProductActionState.value.battery.isEmpty() || manageProductActionState.value.battery.isBlank() ///
    val isValidateCamera = manageProductActionState.value.camera.length < 2 || manageProductActionState.value.camera.length > 100
    val isValidateOs = manageProductActionState.value.os.length < 2 || manageProductActionState.value.os.length > 50
    val isValidateImage = manageProductActionState.value.image == null

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri->
            uri?.let {
                viewModel.uploadImage(uri)
            }
        }
    )

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
                    Text("Add product", style = AppTheme.typography.headlineLarge.copy(
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
                        onClick = {

                        },
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
                            if (
                                isValidateName || isValidatePrice ||
                                isValidateColor || isValidateValueColor ||
                                isValidateScreenSize || isValidateCpu ||
                                isValidateRam || isValidateStorage ||
                                isValidateBattery || isValidateCamera ||
                                isValidateOs || isValidateImage
                            ) {
                                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                            } else {
                                val isValidatePriceNumber = manageProductActionState.value.price.toDouble() < 100000 || manageProductActionState.value.price.toDouble() > 200000000
                                val isValidateScreenSizeNumber = manageProductActionState.value.screenSize.toDouble() < 0 || manageProductActionState.value.screenSize.toDouble() > 20
                                val isValidateRamNumber = manageProductActionState.value.ram.toDouble() < 0 || manageProductActionState.value.ram.toDouble() > 512
                                val isValidateStorageNumber = manageProductActionState.value.storage.toDouble() < 0 || manageProductActionState.value.storage.toDouble() > 2048
                                val isValidateBatteryNumber = manageProductActionState.value.battery.toDouble() < 0 || manageProductActionState.value.battery.toDouble() > 20000
                                if (isValidatePriceNumber || isValidateScreenSizeNumber || isValidateRamNumber || isValidateStorageNumber || isValidateBatteryNumber) {
                                    Toast.makeText(context, "Vui lòng nhập đúng thông tin", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.addProduct(
                                        context = context,
                                        token = keyStore.value,
                                    )
                                }
                            }


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

        when (state.value) {
            is ManageProductState.Error -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text((state.value as ManageProductState.Error).message, style = AppTheme.typography.titleMedium)
                }
            }
            ManageProductState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0x30FFFFFF)),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            ManageProductState.Pending -> {

            }
            is ManageProductState.Success -> {
                val data = (state.value as ManageProductState.Success).data
                Toast.makeText(context, "Đăng tải thành công", Toast.LENGTH_SHORT).show()
                onClickBack()
            }
        }
        when (manageCategoryState.value) {
            is ManageCategoryState.Error -> {

            }
            ManageCategoryState.Loading -> {

            }
            ManageCategoryState.Pending -> {

            }
            is ManageCategoryState.Success -> {
                val data = (manageCategoryState.value as ManageCategoryState.Success).data

            }
        }

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
                    if (manageProductActionState.value.image == null) {
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
                        AsyncImage(
                            model = manageProductActionState.value.image,
                            contentDescription = "Image",
                            modifier = Modifier.size(AppTheme.dimens.extraLarge),
                            contentScale = ContentScale.Crop
                        )
                    }

                }
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                if (manageProductActionState.value.image != null) {
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
                Title("Thông tin sản phẩm")
                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                //Name
                InputText(
                    title = "Tên sản phẩm",
                    name = manageProductActionState.value.name,
                    isValidate = isValidateName,
                    onValueChange = {
                        viewModel.setName(it)
                    }
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                if (isValidateName) {
                    Warning("Nhập ít nhất 6 ký tự")
                }
                Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

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
                                        val isValidatePriceNumber = manageProductActionState.value.price.toDouble() < 100000 || manageProductActionState.value.price.toDouble() > 200000000
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
                            number = manageProductActionState.value.price,
                            onValueChange = {
                                if (it.all { char-> char.isDigit()  })
                                    viewModel.setPrice(it)
                            }
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        if (isValidatePrice) {
                            Warning("Chưa nhập giá")
                        } else {
                            val isValidatePriceNumber = manageProductActionState.value.price.toDouble() < 100000 || manageProductActionState.value.price.toDouble() > 200000000
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
                                value = viewModel.manageProductAction.value.status.first,
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
                            expanded = isExpand.value,
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
                                        viewModel.setStatus(Pair<String, String>(first = it.first, second = it.second))
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

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
                    val desLenght = if (manageProductActionState.value.description == null) "0" else manageProductActionState.value.description!!.length.toString()
                    Text(
                        desLenght,
                        style = AppTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                OutlinedTextField(
                    value = manageProductActionState.value.description ?: "",
                    onValueChange = {
                        viewModel.setDescription(it)
                    },
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

            }

            item {
                Title("Màu sắc sản phẩm")
                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                //Color
                InputText(
                    title = "Màu sắc",
                    name = manageProductActionState.value.color,
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
                    name = manageProductActionState.value.valueColor,
                    isValidate = isValidateValueColor,
                    onValueChange = {
                        if (it.isNotEmpty() && it[0] == '#') {
                            viewModel.setColorValue(it.uppercase())
                        }
                    }
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                if (!isValidColor.value) {
                    Warning("Mã màu không hợp lệ. Ví dụ: #FF5733")
                }
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
            }
            item {
                Title("Thông số kỹ thuật")
                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

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
                                        val isValidateScreenSizeNumber = manageProductActionState.value.screenSize.toDouble() < 0 || manageProductActionState.value.screenSize.toDouble() > 20
                                        if (isValidateScreenSizeNumber) {
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
                            number = manageProductActionState.value.screenSize,
                            onValueChange = {
                                if (it.all { char-> char.isDigit() || char == '.' })
                                    viewModel.setScreenSize(it)
                            }
                        )

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        if (isValidateScreenSize) {
                            Warning("Chưa nhập ký tự")
                        } else {
                            val isValidateScreenSizeNumber = manageProductActionState.value.screenSize.toDouble() < 0 || manageProductActionState.value.screenSize.toDouble() > 20
                            if (isValidateScreenSizeNumber) {
                                Warning("Nhập từ 0 đến 20 inch")
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
                            value = viewModel.manageProductAction.value.cpu,
                            onValueChange = {
                                viewModel.setCpu(it)
                            },
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
                                append("Ram")
                                withStyle(style = SpanStyle(color = contentPrice)) {
                                    if (isValidateRam) {
                                        append("*")
                                    } else {
                                        val isValidateRamNumber = manageProductActionState.value.ram.toDouble() < 0 || manageProductActionState.value.ram.toDouble() > 512
                                        if (isValidateRamNumber) {
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
                        OutlinedTextField(
                            value = manageProductActionState.value.ram,
                            onValueChange = {
                                if (it.all { char-> char.isDigit() || char == '.' })
                                    viewModel.setRam(it)
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

                        if (isValidateRam) {
                            Text(
                                "Chưa có nhập ký tự",
                                style = AppTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = contentPrice
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        } else {
                            val isValidateRamNumber = manageProductActionState.value.ram.toDouble() < 0 || manageProductActionState.value.ram.toDouble() > 512
                            if (isValidateRamNumber) {
                                Warning("Nhập từ 0 đến 512")
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
                                append("Bộ nhớ")
                                withStyle(style = SpanStyle(color = contentPrice)) {
                                    if (isValidateStorage) {
                                        append("*")
                                    } else {
                                        val isValidateStorageNumber = manageProductActionState.value.storage.toDouble() < 0 || manageProductActionState.value.storage.toDouble() > 2048
                                        if (isValidateStorageNumber) {
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
                        OutlinedTextField(
                            value = manageProductActionState.value.storage,
                            onValueChange = {
                                if (it.all { char-> char.isDigit() || char == '.' })
                                    viewModel.setStorage(it)
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

                        if (isValidateStorage) {
                            Warning("Chưa nhập ký tự")
                        } else {
                            val isValidateStorageNumber = manageProductActionState.value.storage.toDouble() < 0 || manageProductActionState.value.storage.toDouble() > 2048
                            if (isValidateStorageNumber) {
                                Warning("Nhập từ 0 đến 2048")
                            } else {
                                Validated()
                            }
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
                                        val isValidateBatteryNumber = manageProductActionState.value.battery.toDouble() < 0 || manageProductActionState.value.battery.toDouble() > 20000
                                        if (isValidateBatteryNumber) {
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
                        OutlinedTextField(
                            value = manageProductActionState.value.battery,
                            onValueChange = {
                                if (it.all { char-> char.isDigit() })
                                    viewModel.setBattery(it)
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
                            val isValidateBatteryNumber = manageProductActionState.value.battery.toDouble() < 0 || manageProductActionState.value.battery.toDouble() > 20000
                            if (isValidateBatteryNumber) {
                                Warning("Nhập từ 0 đến 20000")
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
                            value = viewModel.manageProductAction.value.camera,
                            onValueChange = {
                                viewModel.setCamera(it)
                            },
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
                            value = viewModel.manageProductAction.value.os,
                            onValueChange = {
                                viewModel.setOs(it)
                            },
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
                                        }
                                    )
                                }

                            }
                        }

                    }

                }


            }

        }


    }

}