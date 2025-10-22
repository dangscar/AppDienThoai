package com.nlhd.order.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.order.Data
import java.text.NumberFormat
@Composable
fun CardOrder(
    data: Data
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.small2)
        ) {
            data.order_details.forEach { orderDetail ->
                val image = orderDetail.color_product.image
                val name = orderDetail.color_product.version.product.name
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = "${Utils.BASE_URL}/"+image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(AppTheme.dimens.large)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))
                    Column(
                        modifier = Modifier.height(AppTheme.dimens.large),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            name,
                            style = AppTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            ),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("x${orderDetail.quantity}", style = AppTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            ), maxLines = 1,
                                modifier = Modifier.padding(AppTheme.dimens.small)
                            )
                            val priceFormat = NumberFormat.getNumberInstance().format(orderDetail.price)
                            Text("${priceFormat}đ", style = AppTheme.typography.headlineMedium.copy(
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            ), maxLines = 1,
                                modifier = Modifier.padding(AppTheme.dimens.small),
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
            }

            val priceFormat = NumberFormat.getNumberInstance().format(data.total_amount)
            Text(
                buildAnnotatedString {
                    append("Total: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("${priceFormat}đ")
                    }
                }, style = AppTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End,
                color = Color.Black
            ), maxLines = 1,
                modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small),
            )

            val status = when (data.status) {
                "pending" -> "Đang chờ xác nhận"
                "completed" -> "Đã xác nhận"
                else -> "Đã hủy"
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(status, style = AppTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF1776B1)
                ), maxLines = 1,
                    modifier = Modifier.padding(AppTheme.dimens.small)
                )
                OutlinedButton(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentPrice,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(AppTheme.dimens.small2),
                    border = _root_ide_package_.androidx.compose.foundation.BorderStroke(AppTheme.dimens.extraSmall, Color.Transparent)
                ) {
                    Text(
                        "Chi tiết sản phẩm",
                        style = AppTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(AppTheme.dimens.extraSmall)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CardTest() {
    //CardOrder()
}