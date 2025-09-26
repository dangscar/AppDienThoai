package com.nlhd.shortvideo.components

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.shortvideo.LikedVideo.DetailLikedVideo

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ListShortVideo(
    videos: LazyPagingItems<Video>,
    onClick: (Int) -> Unit
) {
    val width = LocalConfiguration.current.screenWidthDp.dp/3
    val height = LocalConfiguration.current.screenWidthDp.dp/2.25f

    ContextualFlowRow(
        modifier = Modifier.fillMaxWidth(),
        itemCount = videos.itemCount,
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.Start
    ) { index: Int ->

        Box(
            modifier = Modifier
                .size(height = height, width = width)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onClick(index)
                    })
                },
            contentAlignment = Alignment.BottomStart
        ){
            videos[index]?.let { video ->
                val image = if (video.thumbnailUrl == "") R.drawable.anhden else  "${Utils.BASE_URL}/" + video.thumbnailUrl
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(height = height, width = width)
                        .padding(AppTheme.dimens.extraSmall),
                    contentScale = ContentScale.Crop
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(AppTheme.dimens.small)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(AppTheme.dimens.small3),
                        tint = Color.White
                    )
                    Text(
                        video.views,
                        style = AppTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start
                        ),
                    )

                }

            }


        }

    }
}