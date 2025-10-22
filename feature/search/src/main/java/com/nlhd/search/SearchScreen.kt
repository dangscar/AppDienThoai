package com.nlhd.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.contentPrice
import com.nlhd.search.components.HistorySearchCard
import com.nlhd.search.components.SearchTopBar
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onClickBack: () -> Unit,
    onClickSearchSuccess: (String) -> Unit
) {
    val context = LocalContext.current
    val query = viewModel.query.collectAsStateWithLifecycle()
    val histories = viewModel.getHistory(context).collectAsStateWithLifecycle(initialValue = emptyList())
    val categories by viewModel.categorySearchState.collectAsStateWithLifecycle()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchTopBar(
                query = query.value,
                onQueryChange = viewModel::setQuery,
                onSearch = {
                    viewModel.addSearch(context)
                    onClickSearchSuccess(query.value)
                },
                onClickBack = onClickBack
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
        ) {
            items(histories.value.size) {
                val query = histories.value[it]
                HistorySearchCard(
                    query,
                    onRemove = {
                        viewModel.removeHistoryItem(context, query)
                    },
                    onClickSearch = {
                        viewModel.setQuery(query)
                        onClickSearchSuccess(query)
                    }
                )
            }

            when (categories) {
                is CategorySearchState.Error -> {
                    item {
                        Box(modifier = Modifier
                            .fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text((categories as CategorySearchState.Error).message, style = AppTheme.typography.titleMedium)
                        }
                    }
                }
                CategorySearchState.Loading -> {
                    item {
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
                is CategorySearchState.Success -> {
                    val categories = (categories as CategorySearchState.Success).data.categories
                    item {
                        Text(
                            "Danh mục",
                            style = AppTheme.typography.titleMedium,
                            modifier = Modifier.padding(AppTheme.dimens.small2)
                        )
                    }

                    item {
                        ContextualFlowRow(
                            itemCount = categories.size,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) { index->
                            val category = categories[index]
                            Card(
                                onClick = {
                                    onClickSearchSuccess(category.name)
                                },
                                modifier = Modifier.fillMaxWidth(fraction = 0.5f).padding(AppTheme.dimens.small),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.dimens.small),
                                shape = RoundedCornerShape(AppTheme.dimens.small)
                            ) {
                                Text(
                                    category.name,
                                    style = AppTheme.typography.headlineLarge.copy(
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(vertical = AppTheme.dimens.medium3, horizontal = AppTheme.dimens.medium)
                                )
                            }
                        }
                    }
                }
            }


        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun TestComp() {
    ContextualFlowRow(
        itemCount = 4,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            onClick = {},
            modifier = Modifier.fillMaxWidth(fraction = 0.5f).padding(AppTheme.dimens.small2),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.dimens.small2),
            shape = RoundedCornerShape(AppTheme.dimens.small)
        ) {
            Text(
                "Samsung",
                style = AppTheme.typography.headlineLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(vertical = AppTheme.dimens.medium3, horizontal = AppTheme.dimens.medium)
            )
        }
    }

}
