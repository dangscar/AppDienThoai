package com.nlhd.manage_product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.containerAppBarBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Product List", style = AppTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerAppBarBlue
                ),
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {

                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            item {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Tìm kiếm") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(com.nlhd.core.R.drawable.ic_search),
                            contentDescription = "Search",
                            modifier = Modifier.size(AppTheme.dimens.medium2)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.colors( // màu nền giống ảnh
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color(0xFFF5F6F8)
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.small2)
                )
            }
            items(5) {
                Card(
                    onClick = {},
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = AppTheme.dimens.small
                    ),
                    modifier = Modifier.padding(AppTheme.dimens.small2)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small3),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(com.nlhd.core.R.drawable.ic_cart),
                                contentDescription = null,
                                modifier = Modifier.size(AppTheme.dimens.large)
                            )
                            Column(
                                modifier = Modifier
                            ) {
                                Text("Iphone 14 Pro Max", style = AppTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    ),
                                    modifier = Modifier.padding(AppTheme.dimens.small)
                                )

                                Row {
                                    Text("88$", style = AppTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Normal
                                    ),
                                        modifier = Modifier.padding(AppTheme.dimens.small)
                                    )
                                    Text("*", style = AppTheme.typography.headlineMedium.copy(
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
                            modifier = Modifier.padding(AppTheme.dimens.small2)
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun ProductScreenPre() {
    ProductScreen()
}