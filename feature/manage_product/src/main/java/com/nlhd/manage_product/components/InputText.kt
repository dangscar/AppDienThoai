package com.nlhd.manage_product.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.contentPrice

@Composable
fun InputText(
    title: String,
    name: String,
    isValidate: Boolean,
    onValueChange: (String) -> Unit,
) {
    Text(
        buildAnnotatedString {
            append(title)
            withStyle(style = SpanStyle(color = contentPrice)) {
                if (isValidate) {
                    append("*")
                }

            }
        },
        style = AppTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.SemiBold
        ),
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
    OutlinedTextField(
        value = name,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimens.small2),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7F56D9),
            unfocusedBorderColor = Color.LightGray
        ),
        singleLine = true
    )
}