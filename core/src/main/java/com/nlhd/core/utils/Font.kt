package com.nlhd.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font

import androidx.compose.ui.text.googlefonts.GoogleFont
import com.nlhd.core.R


object Font {

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val fontName = GoogleFont("Inter")

    @RequiresApi(Build.VERSION_CODES.Q)
    val fontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )

}