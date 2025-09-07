package com.nlhd.manage_product.LoadVersionProductScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.containerAppBarAdmin
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.manage_product.components.CardVersion
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadVersionProductScreen(
    viewModel: LoadVersionProductViewModel = koinViewModel(),
    productId: Int,
    productName: String,
    onClickBack: () -> Unit,
    onClick: (Int) -> Unit,
    onClickAddVersionProduct: (Int) -> Unit
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            viewModel.getVersionProducts(keyStore.value, productId)
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(productName, style = AppTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerAppBarAdmin
                ),
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color.White,
        floatingActionButton = {
            IconButton(
                onClick = {
                    onClickAddVersionProduct(productId)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = containerAppBarAdmin
                )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            when (state.value) {
                is LoadVersionProductState.Error -> {
                    val error = (state.value as LoadVersionProductState.Error).message
                    item {
                        Text(error, style = AppTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        ))
                    }
                }
                LoadVersionProductState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = contentPrice
                            )
                        }
                    }
                }
                is LoadVersionProductState.Success -> {
                    val data = (state.value as LoadVersionProductState.Success).data
                    val items = data.versions
                    items(items.size) { ind ->
                        CardVersion(
                            version = items[ind],
                            onClick = {
                                onClick(ind)
                            }
                        )
                    }
                }
            }

        }
    }
}