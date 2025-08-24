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

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CardCustomerInfoItem(
    title: String,
    nameCustomer: String,
    phone: String,
    address: String,
    description: String = "Chưa có mô tả"
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
                Text("Tên khách hàng:", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
                Text(nameCustomer, style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
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
                Text("Số điện thoại:", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
                Text(phone, style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
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
                Text("Địa chỉ nhận hàng:", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
                Text(address, style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
            Divider(color = colorDivide)
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = AppTheme.dimens.medium),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Mô tả:", style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
                Text(description, style = AppTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                    maxLines = 1
                )
            }


        }
    }
}