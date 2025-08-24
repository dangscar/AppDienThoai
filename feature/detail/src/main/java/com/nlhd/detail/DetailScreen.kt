package com.nlhd.detail

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import coil.compose.AsyncImage
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel
import androidx.core.graphics.toColorInt
import com.nlhd.core.R
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.containerTopBar
import com.nlhd.core.utils.contentPrice
import java.text.NumberFormat

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel = koinViewModel(),
    productId: Int,
    version: Int,
    color: Int,
    onClickBack: () -> Unit,
    onClickCart: () -> Unit
) {
    val context = LocalContext.current
    val state = detailViewModel.state.collectAsStateWithLifecycle()
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val versionState = detailViewModel.version.collectAsStateWithLifecycle()
    val colorState = detailViewModel.color.collectAsStateWithLifecycle()
    val addCartState = detailViewModel.addCartState.collectAsStateWithLifecycle()
    LaunchedEffect(
        key1 = Unit
    ) {
        detailViewModel.setVersionAndColor(version, color)
        detailViewModel.getProductDetail(productId, version, color)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerTopBar
                ),
                title = {
                    BasicTextField(
                        value = "Bạn muốn tìm gì?",
                        onValueChange = {},
                        textStyle = TextStyle(color = Color.Black),
                        singleLine = true,
                        readOnly = true,
                        decorationBox = {
                            ConstraintLayout(
                                modifier = Modifier
                                    .background(
                                        color = containerButtonLightGray,
                                        RoundedCornerShape(AppTheme.dimens.small3)
                                    )
                                    .padding(AppTheme.dimens.small)
                            ) {
                                val (search, text) = createRefs()
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier
                                        .constrainAs(search) {
                                            top.linkTo(parent.top)
                                            bottom.linkTo(parent.bottom)
                                            start.linkTo(parent.start)
                                            end.linkTo(text.start)
                                        }
                                        .size(AppTheme.dimens.medium3)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_search),
                                        contentDescription = "Search",
                                        tint = Color.Black,
                                        modifier = Modifier.size(AppTheme.dimens.medium)
                                    )
                                }

                                Text(
                                    text = "Bạn muốn tìm gì?",
                                    style = AppTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black,
                                        fontFamily = Font.fontFamily
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.constrainAs(text) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(search.end)
                                        end.linkTo(parent.end)
                                        width = Dimension.fillToConstraints
                                    }
                                )

                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(containerSearch, RoundedCornerShape(AppTheme.dimens.medium))
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClickBack,
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(AppTheme.dimens.medium2)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onClickCart
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cart),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(
                                AppTheme.dimens.medium3)
                        )
                    }
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(
                                AppTheme.dimens.medium2)
                        )
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
                if (state.value is DetailState.Success) {
                    val productDetailResponse = (state.value as DetailState.Success).data
                    val product = productDetailResponse.product
                    val versions = product.versions
                    val colors = versions.first { it.id == versionState.value }.colors

                    val colorsSelected = colors.filter { it.id == colorState.value }
                    val statusResponse = if (colorsSelected.isEmpty()) colors[0].status else colorsSelected[0].status
                    if (statusResponse == "in-stock") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().background(Color.White)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val productDetailResponse = (state.value as DetailState.Success).data
                                    val product = productDetailResponse.product
                                    val versions = product.versions
                                    val colors = versions.first { it.id == versionState.value }.colors

                                    val colorsSelected = colors.filter { it.id == colorState.value }
                                    val image = if (colorsSelected.isEmpty()) colors[0].id else colorsSelected[0].id
                                    detailViewModel.addCart(
                                        token = keyStore.value,
                                        colorProductId = detailViewModel.color.value,
                                        operator = 1,
                                        quantity = 1
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = containerButtonLightGray,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(AppTheme.dimens.small2),
                                border = _root_ide_package_.androidx.compose.foundation.BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
                            ) {
                                Text(
                                    "Thêm vào giỏ",
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
                                onClick = {},
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
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().background(Color.White)
                        ) {
                            Text(
                                "Hết hàng",
                                style = AppTheme.typography.titleMedium.copy(
                                    fontFamily = Font.fontFamily,
                                    color = contentPrice,
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }

            }

        }
    ) { innerPadding ->

        when (state.value) {
            is DetailState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((state.value as DetailState.Error).message, style = AppTheme.typography.titleMedium)
                }
            }
            is DetailState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }

            is DetailState.Success -> {

                val productDetailResponse = (state.value as DetailState.Success).data
                val product = productDetailResponse.product
                val versions = product.versions
                val colors = versions.first { it.id == versionState.value }.colors

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    item {
                        val colorsSelected = colors.filter { it.id == colorState.value }
                        val image = if (colorsSelected.isEmpty()) colors[0].image else colorsSelected[0].image
                        AsyncImage(
                            model = "${Utils.BASE_URL}/"+image,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppTheme.dimens.extraLarge)
                                .background(color = containerButtonLightGray),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small3))


                        val price = if (colorsSelected.isEmpty()) colors[0].price else colorsSelected[0].price
                        val priceFormat = NumberFormat.getNumberInstance().format(price)
                        Column(
                            modifier = Modifier.padding(AppTheme.dimens.small3)
                        ) {
                            Text(
                                "Giá: ₫$priceFormat",
                                style = AppTheme.typography.titleMedium.copy(
                                    color = contentPrice,
                                    fontFamily = Font.fontFamily,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.background(
                                        color = Color(0xFFFFF2F4),
                                        shape = RoundedCornerShape(AppTheme.dimens.small)
                                    ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        "Mua ngay",
                                        style = AppTheme.typography.labelSmall.copy(
                                            color = Color(0xFFFD2656),
                                            fontFamily = Font.fontFamily,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(
                                            horizontal = AppTheme.dimens.small2,
                                            vertical = AppTheme.dimens.small
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                                Box(
                                    modifier = Modifier
                                        .background(color = Color(0xFFFEFEFE))
                                        .border(
                                            width = AppTheme.dimens.extraSmall,
                                            color = Color(0xFFEBE8E8)
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        "COD",
                                        style = AppTheme.typography.labelSmall.copy(
                                            color = Color(0xFFFD2656),
                                            fontFamily = Font.fontFamily,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(
                                            horizontal = AppTheme.dimens.small2,
                                            vertical = AppTheme.dimens.small
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

                            Text(product.name, style = AppTheme.typography.headlineLarge.copy(
                                fontFamily = Font.fontFamily,
                                fontWeight = FontWeight.SemiBold
                            ))
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                            val statusResponse = if (colorsSelected.isEmpty()) colors[0].status else colorsSelected[0].status
                            val status = if (statusResponse == "in-stock") "Còn hàng" else "Hết hàng"
                            Text("Tình trạng hàng: $status", style = AppTheme.typography.labelMedium.copy(
                                fontFamily = Font.fontFamily
                            ))

                            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
                            // Phiên bản RAM/ROM
                            Text("Phiên bản", style = AppTheme.typography.headlineMedium.copy(
                                fontFamily = Font.fontFamily,
                                fontWeight = FontWeight.Bold
                            ))

                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                            LazyRow {
                                items(versions.size)  { index->
                                    val version = versions[index]
                                    val selectColor = if (versionState.value == version.id) contentPrice else Color.Black
                                    val selectBorder = if (versionState.value == version.id) contentPrice else Color(0x42000000)
                                    OutlinedButton(
                                        shape = RoundedCornerShape(AppTheme.dimens.small2),
                                        onClick = {
                                            detailViewModel.setVersionAndColor(version.id, version.colors[0].id)
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White,
                                            contentColor = selectColor,
                                        ),
                                        border = _root_ide_package_.androidx.compose.foundation.BorderStroke(AppTheme.dimens.extraSmall,selectBorder)
                                    ) {
                                        Text("${version.ram}/${version.storage}GB", style = AppTheme.typography.bodyMedium)
                                    }
                                    Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                                }
                            }

                            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

                            // Màu sắc
                            Text("Màu sắc", style = AppTheme.typography.headlineMedium.copy(
                                fontFamily = Font.fontFamily,
                                fontWeight = FontWeight.Bold
                            ))

                            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

                            LazyRow(
                                contentPadding = PaddingValues(AppTheme.dimens.small),
                            ) {
                                items(colors.size) { index->
                                    val color = colors[index]
                                     val selectColor = if (colorState.value != color.id) Color.Transparent else contentPrice
                                    Card(
                                        onClick = {
                                            detailViewModel.setVersionAndColor(
                                                versionState.value,
                                                color.id
                                            )
                                        },
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = AppTheme.dimens.small
                                        ),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White,
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(AppTheme.dimens.small),
                                        border = _root_ide_package_.androidx.compose.foundation.BorderStroke(AppTheme.dimens.extraSmall,selectColor)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            AsyncImage(
                                                model = "${Utils.BASE_URL}/"+color.image,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(AppTheme.dimens.large2)
                                                    .background(color = containerButtonLightGray),
                                            )
                                            Text(color.name, style = AppTheme.typography.bodySmall.copy(
                                                fontFamily = Font.fontFamily,
                                                color = Color.Black
                                            ),
                                                modifier = Modifier.padding(AppTheme.dimens.small)
                                            )

                                        }
                                    }
                                    Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                                }
                            }

                            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

                            // Mô tả
                            Text("Mô tả sản phẩm", style = AppTheme.typography.headlineMedium.copy(
                                fontFamily = Font.fontFamily,
                                fontWeight = FontWeight.Bold
                            ))
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                            Text(product.description, style = AppTheme.typography.bodyMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = Color.Black
                            ))

                            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

                            Text("Thông tin sản phẩm", style = AppTheme.typography.headlineMedium.copy(
                                fontFamily = Font.fontFamily,
                                fontWeight = FontWeight.Bold
                            ))
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                            Text("-Kích thước màn hình: ${product.screenSize}inch", style = AppTheme.typography.bodyMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = Color.Black
                            ))
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                            Text("-Hệ điều hành: ${product.os}", style = AppTheme.typography.bodyMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = Color.Black
                            ))
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                            Text("-Vi xử lý: ${product.cpu}", style = AppTheme.typography.bodyMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = Color.Black
                            ))
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                            Text("-Camera: ${product.camera}", style = AppTheme.typography.bodyMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = Color.Black
                            ))
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                            Text("-Dung lượng pin: ${product.battery}mah", style = AppTheme.typography.bodyMedium.copy(
                                fontFamily = Font.fontFamily,
                                color = Color.Black
                            ))
                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))


                        }

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
        when (addCartState.value) {
            is AddCartState.Error -> {
                val addCart = (addCartState.value as AddCartState.Error).message
                Toast.makeText(context, addCart, Toast.LENGTH_SHORT).show()
            }
            AddCartState.Loading -> {

            }
            AddCartState.Pending -> {

            }
            is AddCartState.Success -> {
                val addCart = (addCartState.value as AddCartState.Success).data
                if (addCart.message.contains("added")) {
                    detailViewModel.setAddCartState(AddCartState.Pending)
                    onClickCart()
                } else if (addCart.message.contains("Unauthenticated")) {
                    Toast.makeText(context, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
private fun OutlineButtonPre() {
    LazyRow(
        contentPadding = PaddingValues(AppTheme.dimens.small),
    ) {
        items(5) {
            Card(
                onClick = {},
                elevation = CardDefaults.cardElevation(
                    defaultElevation = AppTheme.dimens.small
                ),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_cart),
                        contentDescription = null,
                        modifier = Modifier
                            .size(AppTheme.dimens.large3)
                            .background(color = Color(0xFFD9D9D9)),
                    )
                    Text("Màu sắc", style = AppTheme.typography.labelMedium.copy(
                        fontFamily = Font.fontFamily,
                        color = Color.Black
                    ))

                }
            }
            Spacer(modifier = Modifier.width(AppTheme.dimens.small))
        }
    }

}

@Preview
@Composable
private fun ActionPre() {
    // Nút hành động
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        Divider(
            color = containerButtonLightGray,
            thickness = AppTheme.dimens.extraSmall
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(AppTheme.dimens.small3)
        ) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerButtonLightGray,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(AppTheme.dimens.small3),
                border = _root_ide_package_.androidx.compose.foundation.BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
            ) {
                Text(
                    "Thêm vào giỏ",
                    style = AppTheme.typography.headlineLarge.copy(
                        fontFamily = Font.fontFamily,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = contentPrice,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(AppTheme.dimens.small3),
                border = _root_ide_package_.androidx.compose.foundation.BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
            ) {
                Text(
                    "Mua ngay",
                    style = AppTheme.typography.headlineLarge.copy(
                        fontFamily = Font.fontFamily,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }

}