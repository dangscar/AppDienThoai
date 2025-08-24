package com.nlhd.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nlhd.search.components.HistorySearchCard
import com.nlhd.search.components.SearchTopBar
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onClickBack: () -> Unit,
    onClickSearchSuccess: (String) -> Unit
) {
    val context = LocalContext.current
    val query = viewModel.query.collectAsStateWithLifecycle()
    val histories = viewModel.getHistory(context).collectAsStateWithLifecycle(initialValue = emptyList())
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
                .padding(innerPadding)
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
        }
    }
}
