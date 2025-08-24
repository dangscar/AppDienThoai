package com.nlhd.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nlhd.core.theme.AppTheme
import com.nlhd.domain.entity.product.Product
import com.nlhd.search.components.CardProduct
import com.nlhd.search.components.SearchTopBar
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SearchSuccessScreen(
    viewModel: SearchViewModel = koinViewModel(),
    search: String,
    onClickBack: () -> Unit,
    onClickProduct: (Product) -> Unit
) {
    viewModel.setQuery(search)
    val query = viewModel.query.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        viewModel.onSearchClick()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchTopBar(
                query = query.value,
                onQueryChange = {
                    viewModel.setQuery(it)
                },
                onSearch = {
                    viewModel.onSearchClick()
                },
                onClickBack = onClickBack
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding),
            verticalItemSpacing = AppTheme.dimens.small2,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small2),
            contentPadding = PaddingValues(AppTheme.dimens.small)
        ) {
            items(products.itemCount) {
                if (products.itemCount > 0) {
                    products[it]?.let { product ->
                        CardProduct(
                            product = product,
                            onClick = {
                                onClickProduct(product)
                            }
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