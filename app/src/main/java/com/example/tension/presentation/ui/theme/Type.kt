package com.example.tension.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.tension.R

// Set of Material typography styles to start with
val BaseFontFamily = FontFamily(
    Font(R.font.jetbrains_mono_medium, weight = FontWeight.Medium),
    Font(R.font.jetbrains_mono_regular, weight = FontWeight.Normal),
)
val em = (-0.01).em

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = BaseFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 44.sp,
        letterSpacing = em,
    ),
    titleMedium = TextStyle(
        fontFamily = BaseFontFamily,
        fontSize = 34.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = em
    ),
    bodyMedium = TextStyle(
        fontFamily = BaseFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        letterSpacing = em
    ),
    labelSmall = TextStyle(
        fontFamily = BaseFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        letterSpacing = em
    ),

)