package com.nlhd.shortvideo.Profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarProfileShortVideo(
    onClickBack: () -> Unit
) {
    TopAppBar(title = {
        Text(
            "Android Jetpack Compose",
            style = AppTheme.typography.titleMedium.copy(color = Color.Black),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.White,
        scrolledContainerColor = Color.White,
        navigationIconContentColor = Color.Black,
        titleContentColor = Color.Black,
        actionIconContentColor = Color.Black
    ), navigationIcon = {
        IconButton(onClick = onClickBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(
                AppTheme.dimens.medium2))
        }
    })
}