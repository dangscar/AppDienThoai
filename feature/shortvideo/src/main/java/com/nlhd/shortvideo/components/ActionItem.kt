package com.nlhd.shortvideo.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
    clicked: Boolean,
    scale: Float,
    onScale: (Boolean) -> Unit,
    onClick: () -> Unit,
) {


    Column(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    if (!isScrolling) {
                        onScale(true)
                        onClick()
                    }
                })
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = color,
            modifier = modifierIcon.scale(scale)
        )

        Text(
            text = title,
            style = AppTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            ),
        )
        Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
    }

}