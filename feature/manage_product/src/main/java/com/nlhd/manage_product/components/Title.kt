package com.nlhd.manage_product.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.nlhd.core.theme.AppTheme

@Composable
fun Title(
    text: String
) {
    Text(
        text,
        style = AppTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}