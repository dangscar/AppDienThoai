package com.nlhd.manage_product.ColorProductScreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.containerAppBarAdmin
import com.nlhd.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadColorProductScreen(modifier: Modifier = Modifier) {
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
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
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
            items(10) {
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
                                model = R.drawable.ic_product,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(AppTheme.dimens.large)
                            )
                            Column(
                                modifier = Modifier
                            ) {
                                Text(
                                    "Green", style = AppTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                    ),
                                    modifier = Modifier.padding(AppTheme.dimens.small),
                                    maxLines = 1
                                )

                                Row {
                                    Text("$100000 VND", style = AppTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Normal
                                    ),
                                        modifier = Modifier.padding(AppTheme.dimens.small)
                                    )
                                    Text("•", style = AppTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Normal
                                    ),
                                        modifier = Modifier.padding(AppTheme.dimens.small)
                                    )
                                    Text("Còn hàng", style = AppTheme.typography.headlineMedium.copy(
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