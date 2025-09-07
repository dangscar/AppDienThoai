package com.nlhd.order

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.order.components.CardOrder
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    viewModel: OrderViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val orders = viewModel.getOrders(keyStore.value).collectAsLazyPagingItems()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("My orders", style = AppTheme.typography.headlineLarge.copy(
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
            )
        },
        containerColor = Color.White
    ) { innerPadding->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (orders.loadState.refresh) {
                is LoadState.Error -> {
                    item {
                        Text("Chưa có đơn hàng nào", style = AppTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        ))
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        Button(
                            onClick = {
                                orders.retry()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = contentPrice
                            )
                        ) {
                            Text("Retry", style = AppTheme.typography.headlineMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ))
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
                    items(orders.itemCount) { index->
                        orders[index]?.let {
                           CardOrder(it)
                        }
                    }
                }
            }

        }

    }
}