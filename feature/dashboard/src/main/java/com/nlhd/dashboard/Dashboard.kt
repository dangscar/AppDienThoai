package com.nlhd.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.R
import com.nlhd.core.utils.containerAppBarAdmin
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import me.bytebeats.views.charts.bar.BarChar
import me.bytebeats.views.charts.bar.BarChartData
import org.koin.androidx.compose.koinViewModel

data class StatCardData(
    val icon: Int,
    val title: String,
    val color: Color,
    val count: Int,
    val navigate: Navigate
)

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    onClickNavigate: (Navigate) -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val scrollState = rememberScrollState()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")

    val width = if (LocalConfiguration.current.screenWidthDp < 600) LocalConfiguration.current.screenWidthDp.dp/2f else LocalConfiguration.current.screenWidthDp.dp/4.2f


    LaunchedEffect(key1 = keyStore.value) {
        if (keyStore.value != "") {
            viewModel.getDashboard(token = keyStore.value)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text("Dashboard", style = AppTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerAppBarAdmin
                ),
                actions = {
                    IconButton(onClick = {
                        onClickNavigate(Navigate.Profile)
                    }) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
        ) {
            when (state.value) {
                is DashboardState.Error -> {
                    val error = (state.value as DashboardState.Error).message
                    Text(error, style = AppTheme.typography.headlineMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Normal
                    ))
                }
                DashboardState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = contentPrice
                        )
                    }
                }
                DashboardState.Pending -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = containerAppBarAdmin
                        )
                    }
                }
                is DashboardState.Success -> {
                    val data = (state.value as DashboardState.Success).data
                    Text(
                        "Thông tin chung",
                        style = AppTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(AppTheme.dimens.small2)
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        maxItemsInEachRow = if (LocalConfiguration.current.screenWidthDp > 600) 4 else 2,
                    ) {
                        //Card
                        val cards = listOf<StatCardData>(
                            StatCardData(icon = R.drawable.ic_product, title = "Tổng sản phẩm", color = Color(0xFFBAE0F3), count = data.products, navigate = Navigate.Product),
                            StatCardData(icon = R.drawable.ic_cart, title = "Tổng đơn hàng", color = Color(0xFF8EE58D), count = data.orderCount, navigate = Navigate.Order),
                            StatCardData(icon = R.drawable.ic_profile, title = "Tổng số khách hàng", color = Color(0xFFCDA5F6), count = data.customerCount, navigate = Navigate.Customer),
                            StatCardData(icon = R.drawable.ic_money, title = "Tổng doanh thu", color = Color(0xFFF9AA86), count = data.totalRevenue, navigate = Navigate.Balance)
                        )
                        cards.forEach { card->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = card.color
                                ),
                                modifier = Modifier.width(width).padding(AppTheme.dimens.small),
                                onClick = {
                                    when (card.navigate) {
                                        Navigate.Product -> {
                                            onClickNavigate(Navigate.Product)
                                        }
                                        Navigate.Order -> {
                                            onClickNavigate(Navigate.Order)
                                        }
                                        Navigate.Customer -> {
                                            onClickNavigate(Navigate.Customer)
                                        }
                                        Navigate.Balance -> {
                                            onClickNavigate(Navigate.Balance)
                                        }
                                        else -> {}
                                    }
                                },
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = AppTheme.dimens.small
                                )
                            ) {
                                Column(
                                    modifier = Modifier.width(width).height(width/1.2f).padding(AppTheme.dimens.small),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        IconButton(
                                            onClick = {}
                                        ) {
                                            Icon(painter = painterResource(card.icon), contentDescription = null, tint = Color.White)
                                        }
                                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                                        Text(card.count.toString(), style = AppTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        ))
                                    }
                                    Text(card.title, style = AppTheme.typography.bodyMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                        }
                    }
                    Text(
                        "Thống kê",
                        style = AppTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(AppTheme.dimens.small2)
                    )

                    //BarChart
                    val barData = listOf(
                        BarChartData.Bar(1200f, Color(0xFF4CAF50), "Jan"),
                        BarChartData.Bar(9000f, Color(0xFF2196F3), label = "Feb"),
                        BarChartData.Bar(15000f, Color(0xFFFF9800), label = "Mar"),
                        BarChartData.Bar(7000f, Color(0xFFE91E63), label = "Apr"),
                        BarChartData.Bar(11000f, Color(0xFF9C27B0), label = "May")
                    )
                    val barChartData = data.graph.map {
                        BarChartData.Bar(it.revenue.toFloat(), Color(0xFF2196F3), label = it.month.toString())
                    }
                    if (data.graph.isNotEmpty()) {
                        BarChar(
                            barChartData = BarChartData(bars = barChartData),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppTheme.dimens.small2)
                                .height(AppTheme.dimens.extraLarge),
                        )
                    }

                }
            }


        }
    }

}


