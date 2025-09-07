package com.nlhd.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.borderTextField

@Composable
fun ButtonProfile(
    title: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(AppTheme.dimens.small3),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = _root_ide_package_.androidx.compose.foundation.BorderStroke(
            width = AppTheme.dimens.extraSmall,
            color = borderTextField
        )
    ) {
        Text(
            title,
            style = AppTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small),
            textAlign = TextAlign.Center,

            )
    }

}
