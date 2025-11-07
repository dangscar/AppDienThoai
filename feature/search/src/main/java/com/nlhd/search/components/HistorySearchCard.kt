package com.nlhd.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme

@Composable
fun HistorySearchCard(
    query: String,
    onRemove: (String) -> Unit,
    onClickSearch: () -> Unit
) {
    Card(
        onClick = onClickSearch,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.8f)
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_timer),
                        contentDescription = "Back",
                        modifier = Modifier.size(AppTheme.dimens.medium)
                    )
                }
                Text(
                    query,
                    style = AppTheme.typography.headlineLarge,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = {
                    onRemove(query)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cancel),
                    contentDescription = "Cancel",
                    modifier = Modifier.size(AppTheme.dimens.medium)
                )
            }
        }
    }
}

@Preview
@Composable
private fun HistorySearchCardPre() {
    //HistorySearchCard("")
}