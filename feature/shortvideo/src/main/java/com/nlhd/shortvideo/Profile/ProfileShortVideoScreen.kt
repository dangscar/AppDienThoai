package com.nlhd.shortvideo.Profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.core.utils.contentPrice
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.shortvideo.ProfileShortVideoState
import com.nlhd.shortvideo.ProfileShortVideoViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileShortVideoScreen(
    viewModel: ProfileShortVideoViewModel = koinViewModel(),
    videoId: Int,
    userId: Int,
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val keyStore by KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")

    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(keyStore) {
        if (keyStore != "") {
            viewModel.getInfoProfile(token = keyStore, videoId = videoId)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopBarProfileShortVideo(
                onClickBack = onClickBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (state) {
                is ProfileShortVideoState.Error -> {
                    item {
                        Text("Có lỗi xảy ra", style = AppTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        ))
                    }
                }
                ProfileShortVideoState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = contentPrice
                            )
                        }
                    }
                }
                is ProfileShortVideoState.Success -> {
                    val video = (state as ProfileShortVideoState.Success).data
                    val user = video.user
                    item {
                        val image = if (user.avatarUrl == null) R.drawable.anhden else  "${Utils.BASE_URL}/" + user.avatarUrl
                        AsyncImage(
                            model = image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(AppTheme.dimens.large2)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                        Text(
                            "@${user.name}",
                            style = AppTheme.typography.labelLarge.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = AppTheme.dimens.large, vertical = AppTheme.dimens.small2),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(AppTheme.dimens.small2))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(AppTheme.dimens.small2)
                            ) {
                                Text(
                                    "Following",
                                    style = AppTheme.typography.headlineMedium.copy(
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                                Text(
                                    user.followingsCount,
                                    style = AppTheme.typography.bodyMedium.copy(Color.Black),
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = Modifier.width(AppTheme.dimens.small2))

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(AppTheme.dimens.small2)
                            ) {
                                Text(
                                    "Follower",
                                    style = AppTheme.typography.headlineMedium.copy(
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                                Text(
                                    user.followersCount,
                                    style = AppTheme.typography.bodyMedium.copy(Color.Black),
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.width(AppTheme.dimens.small2))

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(AppTheme.dimens.small2)
                            ) {
                                Text(
                                    "Likes",
                                    style = AppTheme.typography.headlineMedium.copy(
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                                Text(
                                    user.likesCount,
                                    style = AppTheme.typography.bodyMedium.copy(Color.Black),
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = Modifier.width(AppTheme.dimens.small2))

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(AppTheme.dimens.small2)
                            ) {
                                Text(
                                    "Favorites",
                                    style = AppTheme.typography.headlineMedium.copy(
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                                Text(
                                    user.favoritesCount,
                                    style = AppTheme.typography.bodyMedium.copy(Color.Black),
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                    }
                }
            }

            item {
                Text("Theo dõi kênh",
                    style = AppTheme.typography.headlineMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    ) ,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                Button(
                    onClick = {  },
                    modifier = Modifier.padding(AppTheme.dimens.extraSmall),
                    shape = RoundedCornerShape(AppTheme.dimens.small3),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentPrice,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Follow",
                        style = AppTheme.typography.titleMedium.copy(Color.White),
                        maxLines = 1
                    )
                }


                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
            }


            item {
                val width = LocalConfiguration.current.screenWidthDp.dp/3
                val height = LocalConfiguration.current.screenWidthDp.dp/2.25f

                ContextualFlowRow(
                    itemCount = 10,
                    maxItemsInEachRow = 3
                ) { index: Int ->

                    Image(
                        painter = painterResource(R.drawable.tiktok),
                        contentDescription = null,
                        modifier = Modifier
                            .size(height = height, width = width)
                            .padding(AppTheme.dimens.extraSmall)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {

                                })
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun ProfileShortVideoScreenPre() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopBarProfileShortVideo(
                onClickBack = {}
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.tiktok),
                    contentDescription = null,
                    modifier = Modifier
                        .size(AppTheme.dimens.large2)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                Text(
                    "@Kotlin",
                    style = AppTheme.typography.labelLarge.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = AppTheme.dimens.large, vertical = AppTheme.dimens.small2),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.small3))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(AppTheme.dimens.small2)
                    ) {
                        Text(
                            "Following",
                            style = AppTheme.typography.titleMedium.copy(Color.Black),
                            modifier = Modifier,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                        Text(
                            "500",
                            style = AppTheme.typography.bodySmall.copy(Color.Black),
                            modifier = Modifier,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(AppTheme.dimens.small2)
                    ) {
                        Text(
                            "Follower",
                            style = AppTheme.typography.titleMedium.copy(Color.Black),
                            modifier = Modifier,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                        Text(
                            "500",
                            style = AppTheme.typography.bodySmall.copy(Color.Black),
                            modifier = Modifier,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(AppTheme.dimens.small2)
                    ) {
                        Text(
                            "Likes",
                            style = AppTheme.typography.titleMedium.copy(Color.Black),
                            modifier = Modifier,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                        Text(
                            "500",
                            style = AppTheme.typography.bodySmall.copy(Color.Black),
                            modifier = Modifier,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.small2))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(AppTheme.dimens.small2)
                    ) {
                        Text(
                            "Favorites",
                            style = AppTheme.typography.titleMedium.copy(Color.Black),
                            modifier = Modifier,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                        Text(
                            "500",
                            style = AppTheme.typography.bodySmall.copy(Color.Black),
                            modifier = Modifier,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                Text("Theo dõi kênh",
                    style = AppTheme.typography.titleMedium.copy(Color.Black),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small2))
                Button(
                    onClick = {  },
                    modifier = Modifier.padding(AppTheme.dimens.extraSmall),
                    shape = RoundedCornerShape(AppTheme.dimens.small3),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentPrice,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Follow",
                        style = AppTheme.typography.titleMedium.copy(Color.White),
                        maxLines = 1
                    )
                }


                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
            }

            item {
                val width = LocalConfiguration.current.screenWidthDp.dp/3
                val height = LocalConfiguration.current.screenWidthDp.dp/2.25f

                ContextualFlowRow(
                    itemCount = 10,
                    maxItemsInEachRow = 3
                ) { index: Int ->

                    Image(
                        painter = painterResource(R.drawable.anhden),
                        contentDescription = null,
                        modifier = Modifier
                            .size(height = height, width = width)
                            .padding(AppTheme.dimens.extraSmall)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {

                                })
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}