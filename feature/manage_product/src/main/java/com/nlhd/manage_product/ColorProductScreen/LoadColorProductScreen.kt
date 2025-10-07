package com.nlhd.manage_product.ColorProductScreen

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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.containerAppBarAdmin
import com.nlhd.core.R
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadColorProductScreen(
    versionProductId: Int,
    viewModel: ColorProductViewModel = koinViewModel(),
    onClickBack: () -> Unit,
    onClickAddColor: (Int) -> Unit,
    onClickEditColor: (Int) -> Unit
) {
    val context = LocalContext.current
    val keyStore by KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = keyStore) {
        if (keyStore != "") {
            viewModel.getColorProducts(keyStore, versionProductId)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("My color products", style = AppTheme.typography.headlineLarge.copy(
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
                    IconButton(onClick = {
                        onClickAddColor(versionProductId)
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        when (state) {
            is ColorProductState.Error -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text((state as ColorProductState.Error).message, style = AppTheme.typography.titleMedium)
                }
            }
            ColorProductState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            is ColorProductState.Success -> {
                val data = (state as ColorProductState.Success).data
                val colors = data.colors
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding)
                ) {
                    items(colors.size) {
                        val color = colors[it]
                        Card(
                            onClick = {  },
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
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = Utils.BASE_URL+color.image,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(AppTheme.dimens.large)
                                    )
                                    Column(
                                        modifier = Modifier
                                    ) {
                                        Text(
                                            color.name, style = AppTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                            ),
                                            modifier = Modifier.padding(AppTheme.dimens.small),
                                            maxLines = 1
                                        )

                                        val formattedPrice = NumberFormat.getNumberInstance().format(color.price)
                                        Row {
                                            Text("${formattedPrice} VND", style = AppTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.Normal
                                            ),
                                                modifier = Modifier.padding(AppTheme.dimens.small)
                                            )
                                            Text("•", style = AppTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.Normal
                                            ),
                                                modifier = Modifier.padding(AppTheme.dimens.small)
                                            )
                                            val status = when (color.status) {
                                                "in-stock" -> {
                                                    "Còn hàng"
                                                }
                                                else -> {
                                                    "Hết hàng"
                                                }
                                            }
                                            Text(status, style = AppTheme.typography.headlineMedium.copy(
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
                                        .padding(AppTheme.dimens.small2)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onTap = {
                                                    onClickEditColor(color.id)

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