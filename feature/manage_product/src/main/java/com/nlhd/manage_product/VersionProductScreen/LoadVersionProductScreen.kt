package com.nlhd.manage_product.VersionProductScreen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.paging.LoadState
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerAppBarAdmin
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.manage_product.components.BottomSheet
import com.nlhd.manage_product.components.BottomSheetVersionProduct
import com.nlhd.manage_product.components.CardVersion
import com.nlhd.manage_product.components.Validated
import com.nlhd.manage_product.components.Warning
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadVersionProductScreen(
    viewModel: LoadVersionProductViewModel = koinViewModel(),
    productId: Int,
    productName: String,
    onClickBack: () -> Unit,
    onClick: (Int) -> Unit,
    onClickAddVersionProduct: (Int) -> Unit
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val state = viewModel.state.collectAsStateWithLifecycle()
    val ram by viewModel.ram.collectAsStateWithLifecycle()
    val storage by viewModel.storage.collectAsStateWithLifecycle()
    val updateVersionProductState by viewModel.updateVersionProductState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val isValidateRam = ram.isEmpty() || ram.isBlank()
    val isValidateStorage = storage.isEmpty() || storage.isBlank()

    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            viewModel.getVersionProducts(keyStore.value, productId)
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(productName, style = AppTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerAppBarAdmin
                ),
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color.White,
        floatingActionButton = {
            IconButton(
                onClick = {
                    onClickAddVersionProduct(productId)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = containerAppBarAdmin
                )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            when (state.value) {
                is LoadVersionProductState.Error -> {
                    val error = (state.value as LoadVersionProductState.Error).message
                    item {
                        Text(error, style = AppTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        ))
                    }
                }
                LoadVersionProductState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = contentPrice
                            )
                        }
                    }
                }
                is LoadVersionProductState.Success -> {
                    val data = (state.value as LoadVersionProductState.Success).data
                    val items = data.versions
                    items(items.size) { ind ->
                        CardVersion(
                            version = items[ind],
                            onClick = {
                                onClick(items[ind].id)
                            },
                            onClickEdit = {
                                viewModel.setVersionId(items[ind].id.toString())
                                viewModel.onValueChangeRam(items[ind].ram.toString())
                                viewModel.onValueChangeStorage(items[ind].storage.toString())
                                scope.launch { sheetState.show() }
                            }
                        )
                    }

                }
            }

        }
        if (sheetState.isVisible) {
            BottomSheet(
                sheetState = sheetState,
                onDismissRequest = { scope.launch { sheetState.hide() } },
            ) {
                Column(
                    modifier = Modifier.padding(AppTheme.dimens.small2),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Text(
                            "Chỉnh sửa",
                            style = AppTheme.typography.labelMedium.copy(
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { scope.launch { sheetState.hide() }}
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(AppTheme.dimens.medium)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    Text(
                        buildAnnotatedString {
                            append("Ram")
                            withStyle(style = SpanStyle(color = contentPrice)) {
                                if (isValidateRam) {
                                    append("*")
                                } else {
                                    val isValidateRamNumber = ram.toDouble() < 0 || ram.toDouble() > 512
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
                        value = ram,
                        onValueChange = {
                            if (it.all { char-> char.isDigit() || char == '.' }) {
                                viewModel.onValueChangeRam(it)
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
                        val isValidateRamNumber = ram.toDouble() < 0 || ram.toDouble() > 512
                        if (isValidateRamNumber) {
                            Warning("Nhập từ 0 đến 512")
                        } else {
                            Validated()
                        }

                    }

                    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                    Text(
                        buildAnnotatedString {
                            append("Bộ nhớ")
                            withStyle(style = SpanStyle(color = contentPrice)) {
                                if (isValidateStorage) {
                                    append("*")
                                } else {
                                    val isValidateStorageNumber = storage.toDouble() < 0 || storage.toDouble() > 2048
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
                        value = storage,
                        onValueChange = {
                            if (it.all { char-> char.isDigit() || char == '.' })
                                viewModel.onValueChangeStorage(it)
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
                        val isValidateStorageNumber = storage.toDouble() < 0 || storage.toDouble() > 2048
                        if (isValidateStorageNumber) {
                            Warning("Nhập từ 0 đến 2048")
                        } else {
                            Validated()
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            if (isValidateRam || isValidateStorage) {
                                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                            } else {
                                val isValidateRamNumber = ram.toDouble() < 0 || ram.toDouble() > 512
                                val isValidateStorageNumber = storage.toDouble() < 0 || storage.toDouble() > 2048
                                if (isValidateRamNumber || isValidateStorageNumber) {
                                    Toast.makeText(context, "Vui lòng nhập đúng thông tin", Toast.LENGTH_SHORT).show()
                                } else {
                                    //Xu ly
                                    viewModel.updateVersionProduct(keyStore.value)
                                }
                            }


                        },
                        modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
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
        when (updateVersionProductState) {
            is UpdateVersionProductState.Error -> {
                val error = (updateVersionProductState as UpdateVersionProductState.Error).message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
            UpdateVersionProductState.Idle -> {}
            UpdateVersionProductState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            is UpdateVersionProductState.Success -> {
                viewModel.getVersionProducts(keyStore.value, productId)
                scope.launch { sheetState.hide() }
                viewModel.setUpdateVersionProductState(UpdateVersionProductState.Idle)
            }
        }
    }
}