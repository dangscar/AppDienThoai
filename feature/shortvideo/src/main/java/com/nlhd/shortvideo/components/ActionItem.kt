package com.nlhd.shortvideo.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.nlhd.core.theme.AppTheme

@Composable
fun ActionItem(
    title: String,
    icon: Int,
    color: Color = Color.White,
    isScrolling: Boolean,
    modifierIcon: Modifier = Modifier,
    modifierSpacer: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        tint = color,
        modifier = modifierIcon
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    if (!isScrolling) {
                        onClick()
                    }
                })
            }
    )
    Spacer(modifier = modifierSpacer)
    Text(
        text = title,
        style = AppTheme.typography.headlineSmall.copy(
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        ),
    )
    Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
}