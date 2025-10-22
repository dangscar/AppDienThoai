package com.nlhd.checkout.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.colorDivide
import java.text.NumberFormat

@Composable
fun CardOrderSummary(
    title: String,
    quantity: String,
    date: String,
    totalAmount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimens.small2),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                title,
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Số lượng sản phẩm:", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
                Text("$quantity sản phẩm", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ngày đặt hàng:", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
                Text(date, style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
            Divider(color = colorDivide)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = AppTheme.dimens.medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tổng số tiền cần thanh toán:", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
                val priceFormat = NumberFormat.getNumberInstance().format(totalAmount)
                Text("${priceFormat}đ", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
            }


        }
    }
}