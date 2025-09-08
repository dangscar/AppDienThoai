package com.nlhd.shortvideo.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nlhd.core.theme.AppTheme

@SuppressLint("DefaultLocale")
fun formatTime(millisecond: Long): String {
    val minutes = millisecond / (1000 * 60)
    val seconds = (millisecond / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
fun TimeFormat(
    currentPosition: Float = 0f,
    duration: Float = 0f
) {
    val currentPositionFormat = formatTime(currentPosition.toLong())
    val durationFormat = formatTime(duration.toLong())
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            currentPositionFormat,
            style = AppTheme.typography.titleLarge.copy(
                color = Color.White
            ),
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(AppTheme.dimens.medium))
        Text(
            "/",
            style = AppTheme.typography.titleMedium.copy(
                color = Color.White
            ),
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(AppTheme.dimens.medium))
        Text(
            durationFormat,
            style = AppTheme.typography.titleLarge.copy(
                color = Color.White
            ),
            maxLines = 1
        )
    }
}