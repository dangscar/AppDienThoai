package com.nlhd.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.product.Product
import com.nlhd.home.component.CardProduct
import org.koin.androidx.compose.koinViewModel

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
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))


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
                                    .border(width = AppTheme.dimens.border+AppTheme.dimens.extraSmall, color = contentPrice, shape = RoundedCornerShape(AppTheme.dimens.small3))
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
                                        painter = painterResource(R.drawable.ic_search_short),
                                        contentDescription = "Search",
                                        tint = Color.Black,
                                        modifier = Modifier.size(AppTheme.dimens.medium)
                                    )
                                }

                                Text(
                                    text = "Oppo Find X9",
                                    style = AppTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xF5868686),
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


                                Text(
                                    text = "Search",
                                    style = AppTheme.typography.labelMedium.copy(
                                        color = contentPrice,
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
                            painter = painterResource(R.drawable.ic_shop),
                            contentDescription = null,
                            tint = contentPrice,
                            modifier = Modifier.size(
                                AppTheme.dimens.medium2)
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            when (products.loadState.refresh) {
                is LoadState.Error -> {
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
                                products.retry()
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
                LoadState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LottieAnimation(
                            composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(AppTheme.dimens.large)
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
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LottieAnimation(
                                        composition,
                                        iterations = LottieConstants.IterateForever,
                                        modifier = Modifier.size(AppTheme.dimens.large)
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