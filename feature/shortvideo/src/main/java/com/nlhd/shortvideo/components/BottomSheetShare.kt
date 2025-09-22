package com.nlhd.shortvideo.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.containerButtonLightGray
import com.nlhd.core.R
import com.nlhd.core.utils.Utils
import androidx.core.net.toUri

@Composable
fun BottomSheetShare(
    videoId: Int,
    onClickCloseBottomSheet: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val urlDirect = Utils.BASE_URL+"/video/watch/$videoId"
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                "Send to",
                style = AppTheme.typography.labelMedium.copy(
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
            IconButton(
                onClick = onClickCloseBottomSheet
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(AppTheme.dimens.medium)
                )
            }

        }

        LazyColumn(
            contentPadding = PaddingValues(AppTheme.dimens.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.small)
                        .background(color = containerButtonLightGray)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    clipboardManager.setText(AnnotatedString(urlDirect))
                                }
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center

                ) {
                    Text(
                        urlDirect,
                        style = AppTheme.typography.labelMedium.copy(
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .padding(horizontal = AppTheme.dimens.small),
                        maxLines = 1
                    )
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(urlDirect))
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_copy),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(AppTheme.dimens.medium)
                        )
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.small),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center

                ) {
                    TextButton(
                        onClick = {
                            val browserIntent = Intent(Intent.ACTION_VIEW, urlDirect.toUri())
                            context.startActivity(browserIntent)
                        }
                    ) {
                        Text(
                            "Đi đến trang",
                            style = AppTheme.typography.labelMedium.copy(
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Normal
                            ),
                        )
                    }
                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, urlDirect)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Chia sẻ qua...")
                            context.startActivity(shareIntent)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(AppTheme.dimens.medium)
                        )
                    }
                }

            }
        }

    }
}

@Preview
@Composable
private fun SharePre() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.dimens.small)
            .background(color = containerButtonLightGray, shape = RoundedCornerShape(AppTheme.dimens.small2)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Text(
            "http://localhost/Shop/public/video/watch/31",
            style = AppTheme.typography.labelMedium.copy(
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(horizontal = AppTheme.dimens.small)
        )
        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(AppTheme.dimens.medium)
            )
        }

    }
}