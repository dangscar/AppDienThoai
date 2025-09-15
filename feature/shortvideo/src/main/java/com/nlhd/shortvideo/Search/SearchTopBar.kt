package com.nlhd.shortvideo.Search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.containerTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClickBack: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            Row(
                modifier = Modifier
                    .border(width = AppTheme.dimens.border, shape = RoundedCornerShape(AppTheme.dimens.small2), color = containerTopBar)
                    .clip(RoundedCornerShape(AppTheme.dimens.small2))
                    .background(Color.White)
                    .fillMaxWidth(), // nền trắng như ảnh ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = AppTheme.typography.headlineMedium,
                    cursorBrush = SolidColor(containerTopBar),
                    visualTransformation = VisualTransformation.None,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch()
                        }
                    ),
                    modifier = Modifier
                        .padding(horizontal = AppTheme.dimens.small3, vertical = AppTheme.dimens.small2)
                    // chừa chỗ cho nút kính lúp
                    ,
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = "Bạn muốn tìm gì?",
                                style = AppTheme.typography.headlineMedium,
                                color = Color.Black
                            )
                        } else {
                            innerTextField()
                        }
                    }
                )
            }

        },
        navigationIcon = {
            // Nút Back
            IconButton(
                onClick = onClickBack
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFFF2D6C),
                )
            }
        },
        actions = {
            TextButton(
                onClick = onSearch
            ) {
                Text("Search", style = AppTheme.typography.headlineMedium.copy(
                    color = containerTopBar
                ))
            }

        }
    )
}