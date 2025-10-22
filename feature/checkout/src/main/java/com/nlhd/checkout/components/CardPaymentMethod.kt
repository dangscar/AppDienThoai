package com.nlhd.checkout.components

import android.app.Activity
import android.os.Build
import android.util.Log
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.pay.button.PayButton
import com.nlhd.checkout.allowedPaymentMethodsJson
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.colorDivide
import com.nlhd.core.utils.containerTopBar
import java.text.NumberFormat

@Composable
fun CardPaymentMethod(
    title: String,
    payments : List<Triple<Int, Boolean, String>>,
    onChange: (Int) -> Unit
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
            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
            payments.forEach { triple->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val title = when(triple.first) {
                        0 -> "Thanh toán khi nhận hàng"
                        1 -> "Thanh toán qua thẻ ngân hàng"
                        else -> ""
                    }
                    Text(title, style = AppTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    ),
                        maxLines = 1
                    )
                    RadioButton(
                        selected = triple.second,
                        onClick = {
                            onChange(triple.first)
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = containerTopBar,
                            unselectedColor = Color.Black
                        )
                    )
                }
            }


        }
    }
}

