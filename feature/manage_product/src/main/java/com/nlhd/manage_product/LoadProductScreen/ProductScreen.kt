package com.nlhd.manage_product.LoadProductScreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.containerAppBarAdmin
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: LoadProductViewModel = koinViewModel(),
    onClickBack: () -> Unit,
    onClickAddProduct: () -> Unit,
    onClickEditProduct: (Int) -> Unit,
    onClick: (Int, String) -> Unit
) {
    val search = viewModel.search.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val products = viewModel.products(keyStore.value).collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("My products", style = AppTheme.typography.headlineLarge.copy(
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
                },
                actions = {
                    IconButton(onClick = onClickAddProduct) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            item {
                TextField(
                    value = search.value,
                    onValueChange = viewModel::setSearch,
                    placeholder = { Text("Tìm kiếm") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search_short),
                            contentDescription = "Search",
                            modifier = Modifier.size(AppTheme.dimens.medium2)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.colors( // màu nền giống ảnh
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color(0xFFF5F6F8),
                        focusedContainerColor = Color(0xFFF5F6F8),
                        cursorColor = containerAppBarAdmin
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.small2),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.onSearchClick()
                        }
                    )
                )
            }
            when (products.loadState.refresh) {
                is LoadState.Error -> {
                    val error = (products.loadState.refresh as LoadState.Error).error.message
                    item {
                        Text(error!!, style = AppTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        ))
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
                    items(products.itemCount) { ind->
                        val product = products[ind]
                        val image = product!!.colors[0].image
                        val price = product.colors[0].price
                        val version = "${product.versions.size} versions"
                        val priceFormat = NumberFormat.getNumberInstance().format(price)
                        Card(
                            onClick = { onClick(product.id, product.name) },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = AppTheme.dimens.extraSmall
                            ),
                            modifier = Modifier.padding(horizontal = AppTheme.dimens.small2, vertical = AppTheme.dimens.small)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small3),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(0.85f)
                                ) {
                                    AsyncImage(
                                        model = "${Utils.BASE_URL}/"+image,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(AppTheme.dimens.large)
                                    )
                                    Column(
                                        modifier = Modifier
                                    ) {
                                        Text(
                                            product.name, style = AppTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                        ),
                                            modifier = Modifier.padding(AppTheme.dimens.small),
                                            maxLines = 1
                                        )

                                        Row {
                                            Text("$priceFormat VND", style = AppTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.Normal
                                            ),
                                                modifier = Modifier.padding(AppTheme.dimens.small)
                                            )
                                            Text("•", style = AppTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.Normal
                                            ),
                                                modifier = Modifier.padding(AppTheme.dimens.small)
                                            )
                                            Text(version, style = AppTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.Normal
                                            ),
                                                modifier = Modifier.padding(AppTheme.dimens.small)
                                            )
                                        }

                                    }
                                }
                                Text("Edit", style = AppTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue
                                ),
                                    modifier = Modifier
                                        .weight(0.15f)
                                        .padding(AppTheme.dimens.small2)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onTap = {
                                                    onClickEditProduct(product.id)
                                                }
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }

        }

    }
}
