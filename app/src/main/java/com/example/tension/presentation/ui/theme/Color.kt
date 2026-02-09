package com.example.tension.presentation.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

@Immutable
class AppColor(
    val backgroundPrimary: Color = Color.Unspecified,
    val textPrimary: Color = Color.Unspecified,
    val backgroundSecondary: Color = Color.Unspecified,
    val textSecondary: Color = Color.Unspecified,
    val special: Color = Color.Unspecified,
)

val LocalColors = compositionLocalOf { AppColor() }

val lightColors = AppColor(
    backgroundPrimary = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF111114),
    backgroundSecondary = Color(0xFFB5B5B5),
    textSecondary = Color(0xFF18191C),
    special = Color(0xFF4E6FD9),
)

val darkColors = AppColor(
    backgroundPrimary = Color(0xFF111114),
    textPrimary = Color(0xFFFFFFFF),
    backgroundSecondary = Color(0xFF18191C),
    textSecondary = Color(0xFFB5B5B5),
    special = Color(0xFF4E6FD9),
)