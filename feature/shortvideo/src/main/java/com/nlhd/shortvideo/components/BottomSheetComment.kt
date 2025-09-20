package com.nlhd.shortvideo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.Comment

@Composable
fun BottomSheetComment(
    comments: LazyPagingItems<Comment>,
    onSendComment: () -> Unit,
    content: String,
    onValueChange: (String) -> Unit,
    onClickCloseBottomSheet: () -> Unit,
    onClickProfile: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                "${comments.itemCount} comments",
                style = AppTheme.typography.labelMedium.copy(
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
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
        when (comments.loadState.refresh) {
            is LoadState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Có lỗi xảy ra", style = AppTheme.typography.titleMedium)
                }

            }
            LoadState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }

            }
            is LoadState.NotLoading -> {
                if (comments.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Không có bình luận", style = AppTheme.typography.headlineMedium)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppTheme.dimens.small)
                    ) {

                        items(comments.itemCount) { ind->
                            comments[ind]?.let { comment->
                                CommentItem(
                                    comment = comment,
                                    onClickProfile = onClickProfile
                                )
                            }


                        }
                    }
                }

            }
        }

    }
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        InputText(
            onSendComment = onSendComment,
            content = content,
            onValueChange = onValueChange
        )
    }


}
@Composable
fun CommentItem(
    comment: Comment,
    onClickProfile: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = if (comment.user.avatarUrl == null) R.drawable.tiktok else "${Utils.BASE_URL}/" + comment.user.avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(AppTheme.dimens.icon)
                .padding(AppTheme.dimens.small)
                .clip(CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onClickProfile(comment.user.id)
                    })
                }

        )
        Column(
            modifier = Modifier
        ) {
            Text(
                text = comment.user.name,
                style = AppTheme.typography.titleMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.W700
                ),
                modifier = Modifier.padding(AppTheme.dimens.small),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = comment.content,
                style = AppTheme.typography.bodyMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(AppTheme.dimens.small),
            )
            Text(
                text = comment.createdAt,
                style = AppTheme.typography.headlineSmall.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Light
                ),
                modifier = Modifier.padding(AppTheme.dimens.small),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
    Spacer(modifier = Modifier.height(AppTheme.dimens.small))
}

@Composable
fun InputText(
    isFocus: Boolean = false,
    onSendComment: () -> Unit,
    content: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val focusRequester = remember {
            FocusRequester()
        }
        LaunchedEffect(Unit) {
            if (isFocus) {
                focusRequester.requestFocus()
            }
        }
        val focusManager = LocalFocusManager.current
        Divider(
            thickness = AppTheme.dimens.extraSmall,
            color = containerSearch
        )
        BasicTextField(
            value = content,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .heightIn(min = AppTheme.dimens.large)
                .padding(AppTheme.dimens.small),
            cursorBrush = SolidColor(contentPrice),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onSend = {
                //Setup
                onSendComment()
                focusManager.clearFocus()
            }),
            decorationBox = { innerTextField ->
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFEEEAEA),
                            shape = RoundedCornerShape(AppTheme.dimens.medium2)
                        )
                        .padding(horizontal = AppTheme.dimens.small),
                ) {
                    val (text) = createRefs()
                    Box(
                        modifier = Modifier.constrainAs(text) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }.padding(horizontal = AppTheme.dimens.small3),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (content.isEmpty()) {
                            Text(
                                "Add comment...",
                                color = Color.Gray,
                                style = AppTheme.typography.labelMedium,
                                maxLines = 1
                            )
                        }
                        // 👇 chỉ gọi innerTextField, nó sẽ hiển thị content bạn nhập
                        innerTextField()
                    }
                }
            }
        )
    }
    Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

}