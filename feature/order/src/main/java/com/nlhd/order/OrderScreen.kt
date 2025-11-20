package com.nlhd.order

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.order.components.CardOrder
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    paddingValues: PaddingValues,
    viewModel: OrderViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val orders = viewModel.getOrders(keyStore.value).collectAsLazyPagingItems()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            Text("My orders", style = AppTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
                modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(AppTheme.dimens.small3),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
        when (orders.loadState.refresh) {
            is LoadState.Error -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.refresh),
                            contentDescription = null,
                            modifier = Modifier.size(AppTheme.dimens.large)
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        Text("Có lỗi gì đó đã xảy ra", style = AppTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        ))
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        OutlinedButton(
                            onClick = {
                                orders.retry()
                            },
                            modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = containerButtonLightGray,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(AppTheme.dimens.small2),
                            border = _root_ide_package_.androidx.compose.foundation.BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
                        ) {
                            Text(
                                "Thử lại",
                                style = AppTheme.typography.headlineMedium.copy(
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(AppTheme.dimens.small)
                            )
                        }
                    }
                }
            }
            LoadState.Loading -> {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = contentPrice
                        )
                    }
                }
            }
            is LoadState.NotLoading -> {
                if (orders.itemCount == 0) {
                    item {
                        Text("Chưa có đơn hàng nào", style = AppTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        ))
                    }
                }
                items(orders.itemCount) { index->
                    orders[index]?.let {
                        CardOrder(it)
                    }
                }
            }
        }

    }
}