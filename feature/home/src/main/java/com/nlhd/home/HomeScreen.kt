package com.nlhd.home

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.containerTopBar
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.product.Product
import com.nlhd.home.component.CardProduct
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    innerPadding: PaddingValues,
    onClick: (Product) -> Unit,
    onClickCart: () -> Unit,
    onClickSearch: () -> Unit
) {
    val products = homeViewModel.getProducts().collectAsLazyPagingItems()

    val isRefreshing = products.loadState.refresh is LoadState.Loading
    val refreshState = rememberPullToRefreshState()


    PullToRefreshBox(
        isRefreshing = isRefreshing,
        state = refreshState,
        onRefresh = {
            products.refresh()
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopAppBar(
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
                                    .background(color = Color.White,RoundedCornerShape(AppTheme.dimens.small3))
                                    .border(width = AppTheme.dimens.border, color = contentPrice, shape = RoundedCornerShape(AppTheme.dimens.small3))
                                    .padding(AppTheme.dimens.small)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = {
                                                onClickSearch()
                                            }
                                        )
                                    }
                            ) {
                                val (search, text, textSearch) = createRefs()
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier.constrainAs(search) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(text.start)
                                    }.size(AppTheme.dimens.medium3)
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
                                        end.linkTo(textSearch.start)
                                        width = Dimension.fillToConstraints
                                    }
                                )

                                /*IconButton(
                                    onClick = {},
                                    modifier = Modifier.constrainAs(camera) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        end.linkTo(textSearch.start)
                                        start.linkTo(text.end)
                                    }.size(AppTheme.dimens.medium3),
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.camera),
                                        contentDescription = "Camera",
                                        tint = Color.Black,
                                        modifier = Modifier.size(AppTheme.dimens.medium)
                                    )
                                }*/

                                Text(
                                    text = "Search",
                                    style = AppTheme.typography.labelMedium.copy(
                                        color = Color.Black,
                                        fontFamily = Font.fontFamily,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.constrainAs(textSearch) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        end.linkTo(parent.end)
                                        start.linkTo(text.end)
                                    }.padding(end = 12.dp)
                                )

                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(AppTheme.dimens.medium))

                    )
                },
                actions = {
                    IconButton(
                        onClick = onClickCart,
                        modifier = Modifier.padding(AppTheme.dimens.small)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cart),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(
                                AppTheme.dimens.medium3)
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            when (products.loadState.refresh) {
                is LoadState.Error -> {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text((products.loadState.refresh as LoadState.Error).error.message ?: "Error", style = AppTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        ))
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                        Button(
                            onClick = {
                                products.retry()
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
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = contentPrice
                        )
                    }
                }
                is LoadState.NotLoading -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                        verticalItemSpacing = AppTheme.dimens.small2,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small2),
                        contentPadding = PaddingValues(AppTheme.dimens.small)
                    ) {
                        items(products.itemCount) {
                            if (products.itemCount > 0) {
                                products[it]?.let { product ->
                                    CardProduct(
                                        product = product,
                                        onClick = { onClick(product) }
                                    )
                                }
                            }
                        }
                        item {
                            if (products.loadState.append is LoadState.Loading) {
                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }

                        }
                    }
                }
            }




        }
    }






}