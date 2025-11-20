package com.nlhd.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nlhd.core.R

val TikTokSans = FontFamily(
    Font(R.font.tiktoksans_light, FontWeight.Light),
    Font(R.font.tiktoksans_regular, FontWeight.Normal),
    Font(R.font.tiktoksans_medium, FontWeight.Medium),
    Font(R.font.tiktoksans_semibold, FontWeight.SemiBold),
    Font(R.font.tiktoksans_bold, FontWeight.Bold),
    Font(R.font.tiktoksans_extrabold, FontWeight.ExtraBold)
)
val CompactSmallTypography = Typography(
    titleSmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 9.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    titleMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    titleLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    bodySmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    bodyMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    bodyLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    labelSmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 7.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    labelMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    headlineMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black,
        lineHeight = 20.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black,
        lineHeight = 22.sp
    )
)

val CompactMediumTypography = Typography(
    titleSmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    titleMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    titleLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    bodySmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    bodyMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.4.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    labelMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    labelLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    headlineSmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 11.5.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black,
        lineHeight = 23.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black,
        lineHeight = 19.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black,
        lineHeight = 23.sp
    )
)

val MediumTypography = androidx.compose.material3.Typography(
    titleSmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    labelMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    labelLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    displaySmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 5.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    )
)

val LargeTypography = Typography(
    titleSmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 49.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 23.sp,
        lineHeight = 23.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    labelMedium = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    labelLarge = TextStyle(
        fontFamily = TikTokSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    )
)
