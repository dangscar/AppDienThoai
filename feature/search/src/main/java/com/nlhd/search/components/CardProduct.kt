package com.nlhd.search.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.Utils
import com.nlhd.domain.entity.product.Product
import java.text.NumberFormat

@Composable
fun CardProduct(
    product: Product,
    onClick: () -> Unit
) {
    val formattedPrice = NumberFormat.getNumberInstance().format(product.colors[0].price)
    val description = if (product.description != null) "Mô tả: ${product.description}" else ""
    val status = if (product.colors[0].status == "in-stock") "Còn hàng" else "Hết hàng"
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = AppTheme.dimens.small
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
        ) {
            AsyncImage(
                model = "${Utils.BASE_URL}/"+product.colors[0].image,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
            Text(product.name, style = AppTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            ), maxLines = 2)

            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
            Text("${formattedPrice}đ", maxLines = 1, style = AppTheme.typography.headlineMedium.copy(
                color = Color(0xFFFD2656),
                fontWeight = FontWeight.SemiBold),
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.background(
                        color = Color(0xFFFFF2F4),
                        shape = RoundedCornerShape(AppTheme.dimens.small)
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Mua ngay",
                        style = AppTheme.typography.labelSmall.copy(
                            color = Color(0xFFFD2656),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(
                            horizontal = AppTheme.dimens.small2,
                            vertical = AppTheme.dimens.small
                        )
                    )
                }
                Spacer(modifier = Modifier.width(AppTheme.dimens.small))
                Box(
                    modifier = Modifier
                        .background(color = Color(0xFFFEFEFE))
                        .border(width = AppTheme.dimens.extraSmall, color = Color(0xFFEBE8E8)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "COD",
                        style = AppTheme.typography.labelSmall.copy(
                            color = Color(0xFFFD2656),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(
                            horizontal = AppTheme.dimens.small2,
                            vertical = AppTheme.dimens.small
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
            Text(description, maxLines = 2, style = AppTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
            ))
            Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text("Tình trạng hàng: $status", style = AppTheme.typography.bodySmall)
            }
        }
    }
}