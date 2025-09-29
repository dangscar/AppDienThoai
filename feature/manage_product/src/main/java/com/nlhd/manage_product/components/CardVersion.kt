package com.nlhd.manage_product.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.manageProduct.LoadVersionProduct.Version

@Composable
fun CardVersion(
    version: Version,
    onClick: () -> Unit,
    onClickEdit: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = AppTheme.dimens.small
        ),
        modifier = Modifier.padding(horizontal = AppTheme.dimens.small2, vertical = AppTheme.dimens.small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    buildAnnotatedString {
                        append("RAM: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                            append("${version.ram}GB")
                        }
                    },
                    style = AppTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(AppTheme.dimens.small2),
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                Text(
                    buildAnnotatedString {
                        append("Storage: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                            append("${version.storage}GB")
                        }
                    },
                    style = AppTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(AppTheme.dimens.small2),
                )
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
                                onClickEdit()
                            }
                        )
                    }
            )
        }

    }
}
