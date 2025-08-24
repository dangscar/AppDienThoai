package com.nlhd.cart

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.gson.Gson
import com.nlhd.cart.components.CardAddress
import com.nlhd.cart.components.CartItem
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.colorDivide
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.contentBlueTopBar
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.checkout.CheckoutRequest
import com.nlhd.domain.entity.checkout.CheckoutResponse
import com.nlhd.domain.entity.checkout.ProductCheckout
import com.nlhd.keystore.KeyStoreManager
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

var currentToast: Toast? = null

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel = koinViewModel(),
    onClickBack: () -> Unit,
    onNavigateAddress: () -> Unit,
    onNavigateEditAddress: (Int) -> Unit,
    onNavigateCheckout: (CheckoutResponse) -> Unit
) {

    val gson = Gson()
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val state = cartViewModel.state.collectAsStateWithLifecycle()
    val addCartState = cartViewModel.addCartState.collectAsStateWithLifecycle()
    val checkoutPreviewState = cartViewModel.stateCheckoutPreview.collectAsStateWithLifecycle()
    val price = cartViewModel.totalPrice.collectAsStateWithLifecycle()
    val isSelected = cartViewModel.isSelected.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    val isOpenSheet = cartViewModel.isOpenSheet.collectAsStateWithLifecycle()
    val stateAddress = cartViewModel.stateAddress.collectAsStateWithLifecycle()
    val stateSelectedAddress = cartViewModel.stateSelectedAddress.collectAsStateWithLifecycle()
    val stateDeleteAddress = cartViewModel.stateDeleteAddress.collectAsStateWithLifecycle()
    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            cartViewModel.getCart(keyStore.value)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.pointerInput(
                            key1 = Unit
                        ) {
                            detectTapGestures(
                                onTap = {
                                    scope.launch { sheetState.show() }
                                    cartViewModel.setOpenSheet(true)
                                }
                            )
                        }
                    ) {
                        Text("Shopping cart", style = AppTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        if (state.value is CartState.Success) {
                            val cartResponse = (state.value as CartState.Success).data
                            if (cartResponse.customerInformation != null) {
                                Text("${cartResponse.customerInformation?.address}, ${cartResponse.customerInformation?.fullName}, ${cartResponse.customerInformation?.phoneNumber}", style = AppTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                ),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            } else {
                                Text("${cartResponse.cartItems.size} items", style = AppTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                ),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }

                        }

                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onClickBack
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
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.pointerInput(key1 = Unit) {
                            detectTapGestures(
                                onTap = {
                                    onNavigateAddress()
                                }
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(
                                AppTheme.dimens.medium)
                        )
                        Text("Thêm địa chỉ", style = AppTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ))
                    }

                }
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
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Tổng thanh toán",
                            style = AppTheme.typography.headlineMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = Color.Black,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        val priceFormat = NumberFormat.getNumberInstance().format(price.value)
                        Text(
                            "${priceFormat}đ",
                            style = AppTheme.typography.headlineMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = contentPrice,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                    OutlinedButton(
                        onClick = {
                            if (state.value is CartState.Success) {
                                val cartResponse = (state.value as CartState.Success).data
                                val products = cartResponse.cartItems
                                val productsCheckout = mutableListOf<ProductCheckout>()
                                isSelected.value.forEachIndexed { ind, value->
                                    if (value) {
                                        val product = products[ind]
                                        productsCheckout.add(
                                            ProductCheckout(
                                                color_product_id = product.color_product_id.toString(),
                                                name = product.color_product.product.name,
                                                image = product.color_product.image,
                                                price = product.price.toString(),
                                                quantity = product.quantity.toString(),
                                                color = product.color_product.name,
                                                ram = product.color_product.version.ram.toString(),
                                                storage = product.color_product.version.storage.toString()
                                            )
                                        )
                                    }
                                }

                                if (cartResponse.customerInformation == null) {
                                    currentToast?.cancel()
                                    val info = "Vui lòng thêm địa chỉ"
                                    currentToast = Toast.makeText(context, info, Toast.LENGTH_SHORT)
                                    currentToast?.show()
                                    return@OutlinedButton
                                }

                                if (productsCheckout.isNotEmpty()) {
                                    val jsonList = gson.toJson(productsCheckout) //Phải chuyển đổi thành json
                                    val checkoutRequest = CheckoutRequest(
                                        customer_info = cartResponse.customerInformation!!.id.toInt(),
                                        selected_products = jsonList
                                    )
                                    cartViewModel.checkoutPreview(keyStore.value, checkoutRequest)
                                } else {
                                    currentToast?.cancel()
                                    val info = "Vui lòng chọn ít nhất 1 sản phẩm"
                                    currentToast = Toast.makeText(context, info, Toast.LENGTH_SHORT)
                                    currentToast?.show()
                                }

                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = contentPrice,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(AppTheme.dimens.small2),
                        border = _root_ide_package_.androidx.compose.foundation.BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
                    ) {
                        Text(
                            "Mua ngay",
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
    ) { innerPadding ->

        when (state.value) {
            is CartState.Error -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text((state.value as CartState.Error).message, style = AppTheme.typography.titleMedium)
                }
            }
            CartState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            is CartState.Success -> {
                val cartResponse = (state.value as CartState.Success).data
                if (cartResponse.message.contains("Unauthenticated")) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding), contentAlignment = Alignment.Center) {
                        Text("Vui lòng đăng nhập tài khoản", style = AppTheme.typography.titleMedium.copy(
                            fontFamily = Font.fontFamily
                        ))
                    }
                } else {
                    cartViewModel.setTotalPrice(cartResponse.totalCost)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = containerButtonLightGray)
                            .padding(innerPadding)
                            .padding(horizontal = AppTheme.dimens.small3)
                    ) {
                        item {
                            Text(
                                "Thông tin chi tiết giỏ hàng",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppTheme.dimens.small3),
                                style = AppTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Font.fontFamily,
                                    color = Color.Black
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                        items(cartResponse.cartItems.size) { ind->
                            var cartItem = cartResponse.cartItems[ind]
                            val isSelected = if (isSelected.value.isNotEmpty()) isSelected.value[ind] else false
                            CartItem(
                                cartItem = cartItem,
                                isSelected = isSelected,
                                onCheckedChange = {
                                    cartViewModel.setSelected(ind, it)
                                },
                                onClickCart = { colorProductId, operator->
                                    if (addCartState.value != AddCartState.Loading) {
                                        cartViewModel.addCart(
                                            token = keyStore.value,
                                            colorProductId = colorProductId,
                                            operator = operator,
                                            quantity = 1
                                        )
                                    }

                                }
                            )
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        }
                    }
                    if (addCartState.value == AddCartState.Loading) {
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


                }

            }
        }

        when (addCartState.value) {
            is AddCartState.Error -> {
                val addCart = (addCartState.value as AddCartState.Error).message
                Toast.makeText(context, addCart, Toast.LENGTH_SHORT).show()
            }
            AddCartState.Loading -> {

            }
            is AddCartState.Success -> {
                val addCart = (addCartState.value as AddCartState.Success).data
                if (addCart.message.contains("added") || addCart.message.contains("removed")) {
                    currentToast?.cancel()
                    val info = if (addCart.message.contains("added")) "Thêm số lượng giỏ hàng thành công" else "Giảm số lượng giỏ hàng thành công"
                    currentToast = Toast.makeText(context, info, Toast.LENGTH_SHORT)
                    currentToast?.show()
                    cartViewModel.getCart(keyStore.value)
                    cartViewModel.setAddCartState(AddCartState.Pending)
                }

            }

            AddCartState.Pending -> {

            }
        }

        when (checkoutPreviewState.value) {
            is CheckoutPreviewState.Error -> {
                val error = (checkoutPreviewState.value as CheckoutPreviewState.Error).message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
            CheckoutPreviewState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            is CheckoutPreviewState.Success -> {
                cartViewModel.setCheckoutPreviewState(CheckoutPreviewState.Pending)
                val success = (checkoutPreviewState.value as CheckoutPreviewState.Success).data
                onNavigateCheckout(success)
            }
            CheckoutPreviewState.Pending -> {

            }
        }

        if (isOpenSheet.value) {
            LaunchedEffect(key1 = keyStore.value) {
                if (isOpenSheet.value && keyStore.value != "") {
                    cartViewModel.getAddress(keyStore.value)
                }
            }
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch { sheetState.hide() }
                    cartViewModel.setOpenSheet(false)
                },
                sheetState = sheetState,
                containerColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (stateAddress.value) {
                    is AddressState.Error -> {
                        val error = (stateAddress.value as AddressState.Error).message
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                            .padding(innerPadding), contentAlignment = Alignment.Center) {
                            Text(error, style = AppTheme.typography.titleMedium.copy(
                                fontFamily = Font.fontFamily
                            ))
                        }
                    }
                    AddressState.Loading -> {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                            .padding(innerPadding), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = contentPrice
                            )
                        }
                    }
                    AddressState.Pending -> {

                    }
                    is AddressState.Success -> {
                        val addressResponse = (stateAddress.value as AddressState.Success).data
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.6f)
                                .verticalScroll(rememberScrollState()),
                        ) {
                            Text(
                                "Lựa chọn địa chỉ",
                                style = AppTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
                                textAlign = TextAlign.Center
                            )

                            Divider(color = colorDivide)
                            addressResponse.customerInfomations.forEach { user->
                                Card(
                                    onClick = {
                                        cartViewModel.selectedAddress(keyStore.value, user.id)
                                    },
                                    elevation = CardDefaults.elevatedCardElevation(
                                        defaultElevation = AppTheme.dimens.small
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    modifier = Modifier.padding(AppTheme.dimens.small2)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(user.fullName, style = AppTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                            modifier = Modifier.padding(AppTheme.dimens.small2)
                                        )
                                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                                        Text("(+84)${user.phoneNumber}", style = AppTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Black
                                        ),
                                            modifier = Modifier.padding(AppTheme.dimens.small2)
                                        )
                                        Text(user.address, style = AppTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Black
                                        ),
                                            modifier = Modifier.padding(AppTheme.dimens.small2)
                                        )
                                        Text(user.description ?: "Chưa có mô tả gần đây", style = AppTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Black
                                        ),
                                            modifier = Modifier.padding(AppTheme.dimens.small2)
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            when (user.isSelected) {
                                                1 -> {
                                                    Text("Đang được chọn", style = AppTheme.typography.headlineMedium.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = contentBlueTopBar
                                                    ),
                                                        modifier = Modifier.padding(AppTheme.dimens.small2)
                                                    )
                                                }
                                                else -> {
                                                    Text("Chưa được chọn", style = AppTheme.typography.headlineMedium.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                    ),
                                                        modifier = Modifier.padding(AppTheme.dimens.small2)
                                                    )
                                                }
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        onNavigateEditAddress(user.id)
                                                        scope.launch { sheetState.hide() }
                                                        cartViewModel.setOpenSheet(false)
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = null,
                                                        tint = Color.Blue,
                                                    )
                                                }
                                                IconButton(
                                                    onClick = {
                                                        cartViewModel.deleteAddress(keyStore.value, user.id)
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = null,
                                                        tint = Color.Red,

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

            }
        }

        when (stateSelectedAddress.value) {
            is SelectedAddressState.Error -> {
                val error = (stateSelectedAddress.value as SelectedAddressState.Error).message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
            SelectedAddressState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            SelectedAddressState.Pending -> {

            }
            is SelectedAddressState.Success -> {
                cartViewModel.setOpenSheet(false)
                cartViewModel.updateStateSuccessAddress()
                cartViewModel.getCart(keyStore.value)
            }
        }

        when (stateDeleteAddress.value) {
            is DeleteAddressState.Error -> {
                val error = (stateDeleteAddress.value as DeleteAddressState.Error).message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
            DeleteAddressState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            DeleteAddressState.Pending -> {

            }
            is DeleteAddressState.Success -> {
                val message = (stateDeleteAddress.value as DeleteAddressState.Success).data.message
                cartViewModel.getCart(keyStore.value)
                cartViewModel.setOpenSheet(false)
                cartViewModel.updateStateSuccessAddress()
            }
        }


    }
}

@Preview
@Composable
private fun BottomSheetPre() {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            "Lựa chọn địa chỉ",
            style = AppTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
            textAlign = TextAlign.Center
        )

        Divider(color = Color.Black)
        Card(
            onClick = {},
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = AppTheme.dimens.small
            ),
            modifier = Modifier.padding(AppTheme.dimens.small2)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nguyễn Lê Hải Đăng", style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                    modifier = Modifier.padding(AppTheme.dimens.small2)
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                Text("(+84)9867567567", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),
                    modifier = Modifier.padding(AppTheme.dimens.small2)
                )
                Text("Ap Muong Tra, Xa Tan Thanh", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),
                    modifier = Modifier.padding(AppTheme.dimens.small2)
                )
                Text("Default", style = AppTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                    modifier = Modifier.padding(AppTheme.dimens.small2)
                )
            }
        }
    }
}