package com.nlhd.manage_product.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.contentBlueTopBar

@Composable
fun Validated(modifier: Modifier = Modifier) {
    Text(
        "Xác thực đã đúng",
        style = AppTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Medium,
            color = contentBlueTopBar
        ),
    )
}