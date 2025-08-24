package com.nlhd.core.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.material3.Typography
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

val LocalAppDimens = staticCompositionLocalOf {
    CompactSmallDimens
}

val LocalAppTypography = staticCompositionLocalOf {
    CompactSmallTypography
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppTheme(
    @SuppressLint("ContextCastToActivity") activity: Activity = LocalContext.current as Activity,
    content: @Composable () -> Unit
) {
    val window = calculateWindowSizeClass(activity)
    val config = LocalConfiguration.current

    var typography = CompactSmallTypography
    var dimens = CompactSmallDimens

    when (window.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            if (config.screenWidthDp <= 360) {
                dimens = CompactSmallDimens
                typography = CompactSmallTypography
            } else if (config.screenWidthDp < 599) {
                dimens = CompactMediumDimens
                typography = CompactMediumTypography
            } else {
                dimens = MediumDimens
                typography = MediumTypography
            }
        }
        WindowWidthSizeClass.Medium -> {
            dimens = MediumDimens
            typography = MediumTypography
        }
        WindowWidthSizeClass.Expanded -> {
            when (window.heightSizeClass) {
                WindowHeightSizeClass.Compact -> {
                    dimens = CompactMediumDimens
                    typography = CompactMediumTypography
                }
                WindowHeightSizeClass.Medium -> {
                    dimens = MediumDimens
                    typography = MediumTypography
                }
                WindowHeightSizeClass.Expanded -> {
                    dimens = LargeDimens
                    typography = LargeTypography
                }
            }

        }
    }

    CompositionLocalProvider(LocalAppDimens provides dimens, LocalAppTypography provides typography) {
        content.invoke()
    }
}

object AppTheme {
    val dimens: Dimens
        @Composable
        get() = LocalAppDimens.current

    val typography: Typography
        @Composable
        get() = LocalAppTypography.current
}