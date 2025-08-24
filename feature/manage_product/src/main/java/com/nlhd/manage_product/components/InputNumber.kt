package com.nlhd.manage_product.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.contentBlueTopBar
import com.nlhd.core.utils.contentPrice

@Composable
fun InputNumber(
    number: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = number,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimens.small2),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7F56D9),
            unfocusedBorderColor = Color.LightGray
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )

}