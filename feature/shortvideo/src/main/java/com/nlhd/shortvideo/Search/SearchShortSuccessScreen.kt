package com.nlhd.shortvideo.Search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Utils
import com.nlhd.keystore.KeyStoreManager
import kotlinx.serialization.Serializable

import org.koin.androidx.compose.koinViewModel

@Serializable
object ListShortVideo

@Serializable
data class DetailShortVideo(
    val position: Int
)

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SearchShortSuccessScreen(
    viewModel: SearchShortVideoSuccessViewModel = koinViewModel(),
    search: String,
    onClickBack: () -> Unit,
    onClickSeeProduct: (Int, Int, Int) -> Unit,
    onClickProfile: (Int) -> Unit
) {
    viewModel.setQuery(search)
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val query by viewModel.query.collectAsStateWithLifecycle()
    val videos = viewModel.videos.collectAsLazyPagingItems()
    LaunchedEffect(keyStore.value) {
        if (keyStore.value != "") {
            viewModel.setToken(keyStore.value)
            viewModel.onSearchClick()
        }
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ListShortVideo
    ) {
        composable<ListShortVideo> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    SearchTopBar(
                        query = query,
                        onQueryChange = viewModel::setQuery,
                        onSearch = viewModel::onSearchClick,
                        onClickBack = onClickBack
                    )
                },
                containerColor = Color.White
            ) { innerPadding ->
                val width = LocalConfiguration.current.screenWidthDp.dp/2
                val height = LocalConfiguration.current.screenWidthDp.dp/1.25f
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    columns = GridCells.Fixed(2),
                ) {
                    items(videos.itemCount) {
                        val video = videos[it]
                        Column(
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(onTap = { offset->
                                    navController.navigate(DetailShortVideo(it))
                                })
                            }
                        ) {
                            AsyncImage(
                                model = if (video?.thumbnailUrl != null && video.thumbnailUrl != "") "${Utils.BASE_URL}/"+ video.thumbnailUrl else R.drawable.anhden,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(width, height)
                                    .padding(AppTheme.dimens.small2)
                                    .clip(RoundedCornerShape(AppTheme.dimens.small3)),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = video!!.caption,
                                style = AppTheme.typography.titleSmall.copy(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.width(width).padding(horizontal = AppTheme.dimens.small2 ,vertical = AppTheme.dimens.extraSmall)
                            )
                            Row(
                                modifier = Modifier.width(width),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = AppTheme.dimens.small2, vertical = AppTheme.dimens.small),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = if (video.user.avatarUrl == null) R.drawable.anhden else "${Utils.BASE_URL}/"+video.user.avatarUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(AppTheme.dimens.medium)
                                            .pointerInput(Unit) {
                                                detectTapGestures(onTap = { offset->
                                                })
                                            }
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        text = video.user.name,
                                        style = AppTheme.typography.titleSmall.copy(
                                            color = Color.Black,
                                            fontWeight = FontWeight.Normal
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(AppTheme.dimens.small)
                                    )
                                }
                                Row(
                                    modifier = Modifier.padding(AppTheme.dimens.small2),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_heart),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(AppTheme.dimens.small3),
                                        tint = Color.Red
                                    )
                                    Text(
                                        text = video.likes,
                                        style = AppTheme.typography.titleSmall.copy(
                                            color = Color.Black,
                                            fontWeight = FontWeight.Normal
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(AppTheme.dimens.small)
                                    )
                                }
                            }

                        }
                    }

                }
            }
        }
        composable<DetailShortVideo> {
            val position = it.toRoute<DetailShortVideo>().position
            DetailShortVideoScreen(
                position = position,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                videos = videos,
                onClickSeeProduct = onClickSeeProduct,
                onClickProfile = onClickProfile
            )
        }
    }

}