package com.nlhd.cart.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerTopBar
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.cart.CustomerInformation

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CartTest() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimens.small2),
        colors = CardDefaults.cardColors(
            containerColor = containerTopBar,
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                "Thông tin giao hàng",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small3),
                style = AppTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = Font.fontFamily,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
            Text(
                "Họ tên: Nguyen Van A",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small2),
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
            Text(
                "Địa chỉ: Sóc Trăng",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small2),
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
            Text(
                "Điện thoại: 0934543456",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small2),
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun CardAddress(
    customerInformation: CustomerInformation? = null
) {
    val name = customerInformation?.fullName ?: "Chưa biết"
    val address = customerInformation?.address ?: "Chưa biết"
    val phone = customerInformation?.phoneNumber ?: "Chưa biết"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimens.small2),
        colors = CardDefaults.cardColors(
            containerColor = containerTopBar,
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                "Thông tin giao hàng",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small3),
                style = AppTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = Font.fontFamily,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
            Text(
                "Họ tên: $name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small),
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
            Text(
                "Địa chỉ: $address",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small),
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
            Text(
                "Điện thoại: $phone",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small),
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = Font.fontFamily,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
        }
    }
}