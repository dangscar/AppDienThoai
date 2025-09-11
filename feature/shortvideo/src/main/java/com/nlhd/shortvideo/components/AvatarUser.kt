package com.nlhd.shortvideo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Blue
import com.nlhd.core.utils.Green

@Composable
fun AvatarUser(
    avatar: String? = null,
    isFollow : Boolean = false,
    isStory: Boolean = false,
    onClick: () -> Unit,
    onClickAdd: () -> Unit
) {
    if (isStory) {
        ConstraintLayout {
            val (image, plus) = createRefs()
            val colorsStory = listOf(
                Color(0xFF00FFFF), // Cyan
                Color(0xFF00A2FF), // Light Blue
                Color(0xFF00FF88)  // Light Green
            )
            Box(
                modifier = Modifier
                    .size(AppTheme.dimens.iconStory)
                    .padding(AppTheme.dimens.border)
                    .clip(CircleShape)
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .border(
                        brush = Brush.linearGradient(
                            colors = colorsStory
                        ),
                        width = AppTheme.dimens.border,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center

            ) {
                AsyncImage(
                    model = avatar ?: R.drawable.tiktok,
                    contentDescription = null,
                    modifier = Modifier
                        .size(AppTheme.dimens.icon)
                        .padding(AppTheme.dimens.border)
                        .clip(CircleShape)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                onClick()
                            })
                        }
                )
            }

            Box(modifier = Modifier
                .constrainAs(plus) {
                    bottom.linkTo(image.bottom)
                    top.linkTo(image.bottom)
                    end.linkTo(image.end)
                    start.linkTo(image.start)
                }
                .border(
                    width = AppTheme.dimens.border,
                    color = Color.Red,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(color = Color.Red, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(AppTheme.dimens.iconAdd)
                        .padding(AppTheme.dimens.paddingAdd)
                )
            }

        }
    } else {
        ConstraintLayout(
            modifier = Modifier.padding(AppTheme.dimens.small2)
        ) {
            val (image, plus) = createRefs()
            AsyncImage(
                model = avatar ?: R.drawable.tiktok,
                contentDescription = null,
                modifier = Modifier
                    .size(AppTheme.dimens.icon)
                    .clip(CircleShape)
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            onClick()
                        })
                    }

            )
            if (!isFollow) {
                Box(modifier = Modifier
                    .constrainAs(plus) {
                        bottom.linkTo(image.bottom)
                        top.linkTo(image.bottom)
                        end.linkTo(image.end)
                        start.linkTo(image.start)
                    }
                    .border(
                        width = AppTheme.dimens.border,
                        color = Color.Red,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(color = Color.Red, shape = CircleShape)
                    .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onClickAdd()
                    })
                }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(AppTheme.dimens.iconAdd)
                            .padding(AppTheme.dimens.paddingAdd)
                    )
                }
            }


        }
    }

}