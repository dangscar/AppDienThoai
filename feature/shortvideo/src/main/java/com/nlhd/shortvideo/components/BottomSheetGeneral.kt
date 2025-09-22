package com.nlhd.shortvideo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme

@Composable
fun BottomSheetGeneral(
    onClickScroll: () -> Unit,
) {
    Column {
        LazyRow {
            item {
                ShareItem(title = "AutoScroll", image = R.drawable.ic_scroll) {
                    onClickScroll()
                }
            }
        }
    }
}

@Composable
fun ShareItem(
    title: String = "User",
    image: Int = R.drawable.tiktok,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    onClick()
                }
            )
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .size(AppTheme.dimens.icon)
                .padding(AppTheme.dimens.small)
                .clip(CircleShape)
        )
        Text(
            text = title,
            style = AppTheme.typography.labelMedium.copy(
                color = Color.Black
            ),
            modifier = Modifier.padding(horizontal = AppTheme.dimens.medium2)
        )
    }

}