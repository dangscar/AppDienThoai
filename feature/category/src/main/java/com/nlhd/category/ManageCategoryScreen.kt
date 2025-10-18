package com.nlhd.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nlhd.core.theme.AppTheme

@Composable
fun ManageCategoryScreen(modifier: Modifier = Modifier) {
    Scaffold { innerPadding->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
        ) {
            Text(
                "Danh mục123",
                style = AppTheme.typography.titleMedium,
                modifier = Modifier.padding(AppTheme.dimens.small2)
            )
        }
    }
}