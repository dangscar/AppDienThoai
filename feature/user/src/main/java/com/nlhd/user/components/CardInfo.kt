package com.nlhd.user.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerCardProfile
import com.nlhd.core.utils.containerTextFieldLogin

@Composable
fun CardInfo(modifier: Modifier = Modifier) {
    Card(
        onClick = {},
        colors = CardDefaults.cardColors(
            containerColor = containerTextFieldLogin
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small3)
        ) {
            Text(
                "Địa chỉ: Sóc Trăng",
                style = AppTheme.typography.bodyMedium.copy(
                    color = Color.Black,
                    fontFamily = Font.fontFamily,
                    fontWeight = FontWeight.ExtraLight
                ),
                modifier = Modifier
                    .padding(AppTheme.dimens.small),
            )
            Text(
                "Số điện thoại: 09457685456",
                style = AppTheme.typography.bodyMedium.copy(
                    color = Color.Black,
                    fontFamily = Font.fontFamily,
                    fontWeight = FontWeight.ExtraLight
                ),
                modifier = Modifier.padding(AppTheme.dimens.small),
            )

        }
    }
}

@Preview
@Composable
private fun CardInfoPre() {
    CardInfo()
}