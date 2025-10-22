package com.nlhd.checkout.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.containerTextFieldLogin
import com.nlhd.core.utils.containerTopBar
import com.nlhd.domain.entity.checkout.SelectedProduct
import java.text.NumberFormat

@Composable
fun CheckoutItem(
    product: SelectedProduct
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimens.small2),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = "${Utils.BASE_URL}/"+product.image,
                contentDescription = null,
                modifier = Modifier
                    .size(AppTheme.dimens.large2)
            )
            Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
            Column {
                Text(
                    product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.small2),
                    style = AppTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = AppTheme.dimens.small2)
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
                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                val priceFormat = NumberFormat.getNumberInstance().format(product.price.toInt())
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${priceFormat}đ", maxLines = 1, style = AppTheme.typography.headlineMedium.copy(
                        color = Color(0xFFFD2656),
                        fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(AppTheme.dimens.small2)
                    )
                    Text("Số lượng: ${product.quantity}", style = AppTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    ), maxLines = 1,
                        modifier = Modifier.padding(AppTheme.dimens.small2)
                    )

                }
            }
        }
    }
}