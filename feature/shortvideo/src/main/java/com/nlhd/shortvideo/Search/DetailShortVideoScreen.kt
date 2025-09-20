package com.nlhd.shortvideo.Search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.shortvideo.ContentCommon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarDetailVideoScreen(
    title: String,
    innerPadding: PaddingValues,
    onClickBack: () -> Unit,
    onSearch: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .zIndex(5f)
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClickBack,
            modifier = Modifier.padding(horizontal = AppTheme.dimens.small)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(AppTheme.dimens.medium2)
            )
        }
        BasicTextField(
            value = "",
            readOnly = true,
            onValueChange = {},
            interactionSource = remember { MutableInteractionSource() },
            keyboardOptions = KeyboardOptions(autoCorrect = false),
            modifier = Modifier
                .padding(end = AppTheme.dimens.small3)
                .fillMaxWidth()
                .border(
                    AppTheme.dimens.border,
                    Color.White,
                    RoundedCornerShape(AppTheme.dimens.small2)
                ).padding(AppTheme.dimens.small)
            ,
            singleLine = true,
            textStyle = TextStyle(color = Color.White),
            decorationBox = { innerTextField ->
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                onSearch(title)
                            })
                        }
                ) {
                    val (icon ,text, line,search) = createRefs()

                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = null,
                        modifier = Modifier
                            .size(AppTheme.dimens.medium2)
                            .constrainAs(icon) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(text.start)
                            }.padding(AppTheme.dimens.paddingAdd),
                        tint = Color.White
                    )

                    Text(
                        title,
                        color = Color.White,
                        style = AppTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .constrainAs(text) {
                                start.linkTo(icon.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(line.start)
                                width =Dimension.fillToConstraints
                            }
                            .padding(
                                end = AppTheme.dimens.small2,
                                top = AppTheme.dimens.small,
                                bottom = AppTheme.dimens.small,
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(
                                width = AppTheme.dimens.extraSmall,
                                height = AppTheme.dimens.small3
                            )
                            .background(color = Color(0x80FAFAFA))
                            .constrainAs(line) {
                                start.linkTo(text.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(search.start)
                            }
                            .padding(
                                horizontal = AppTheme.dimens.small2,
                                vertical = AppTheme.dimens.small
                            )
                    )

                    Text(
                        "Search",
                        color = Color.White,
                        style = AppTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .constrainAs(search) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(line.end)
                            }
                            .padding(
                                horizontal = AppTheme.dimens.small2,
                                vertical = AppTheme.dimens.small
                            )
                    )

                }

            }
        )
    }


}

@Composable
fun DetailShortVideoScreen (
    position: Int,
    videos: LazyPagingItems<Video>,
    onClickBack: () -> Unit,
    onClickSeeProduct: (Int, Int, Int) -> Unit,
    onClickProfile: (Int) -> Unit,
    onSearch: () -> Unit
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val pageState = rememberPagerState(initialPage = position) {
        videos.itemCount
    }
    var search by remember {
        mutableStateOf("Find related content")
    }
    var isHidden by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
    ) { innerPadding ->
        if (!isHidden) {
            TopBarDetailVideoScreen(
                title = search ,
                innerPadding = innerPadding,
                onClickBack = onClickBack,
                onSearch = {
                    onSearch()
                }
            )
        }

        ContentCommon(
            token = keyStore.value,
            pageF = "Page",
            isPlaying = true,
            pagerState = pageState,
            paddingValues = innerPadding,
            videos = videos,
            onSearch = {
                search = it
            },
            onHiddenText = {
                isHidden = it
            },
            onClickSeeProduct = onClickSeeProduct,
            onClickProfile = onClickProfile,
        )

    }
}