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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
                    .border(width = AppTheme.dimens.border, shape = RoundedCornerShape(AppTheme.dimens.small2), color = Color.White)
                    .clip(RoundedCornerShape(AppTheme.dimens.small2))
                    .background(Color(0xFFF2F2F2))
                    .fillMaxWidth(), // nền trắng như ảnh ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = AppTheme.typography.headlineMedium.copy(
                        color = Color(0xF5868686)
                    ),
                    cursorBrush = SolidColor(containerTopBar),
                    visualTransformation = VisualTransformation.None,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch()
                        }
                    ),
                    modifier = Modifier
                        .padding(horizontal = AppTheme.dimens.small2, vertical = AppTheme.dimens.small2)
                    // chừa chỗ cho nút kính lúp
                    ,
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = "Bạn muốn tìm gì?",
                                style = AppTheme.typography.labelMedium.copy(
                                    color = Color(0xF5868686)
                                ),
                            )
                        }
                        innerTextField()
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
                    tint = Color.Black,
                )
            }
        },
        actions = {
            TextButton(
                onClick = onSearch
            ) {
                Text("Tìm kiếm", style = AppTheme.typography.headlineMedium.copy(
                    color = containerTopBar,
                    fontWeight = FontWeight.Medium
                ))
            }

        }
    )
}