package com.example.tension.presentation.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun Title(
    text: String,
    align: TextAlign = TextAlign.Center,
    color: Color = LocalColors.current.textPrimary,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = color,
        textAlign = align
    )
}

@Composable
fun Subtitle(
    text: String,
    align: TextAlign = TextAlign.Center,
    color: Color = LocalColors.current.textPrimary,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        textAlign = align
    )
}

@Composable
fun Body(
    text: String,
    align: TextAlign = TextAlign.Center,
    color: Color = LocalColors.current.textPrimary,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        textAlign = align
    )
}

@Composable
fun Label(
    text: String,
    align: TextAlign = TextAlign.Center,
    color: Color = LocalColors.current.textPrimary,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        textAlign = align
    )
}

@Composable
fun SetBarsColor(
    systemBarsColors: Color = LocalColors.current.backgroundPrimary,
    navigationBarColor: Color = LocalColors.current.backgroundPrimary,
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = systemBarsColors,
            darkIcons = useDarkIcons
        )
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = useDarkIcons
        )
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier, body: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LocalColors.current.backgroundPrimary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        body()
    }
}