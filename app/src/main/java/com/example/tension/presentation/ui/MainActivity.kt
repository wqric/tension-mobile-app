package com.example.tension.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.tension.R
import com.example.tension.presentation.ui.theme.Body
import com.example.tension.presentation.ui.theme.Label
import com.example.tension.presentation.ui.theme.LocalColors
import com.example.tension.presentation.ui.theme.Screen
import com.example.tension.presentation.ui.theme.SetBarsColor
import com.example.tension.presentation.ui.theme.Subtitle
import com.example.tension.presentation.ui.theme.TensionTheme
import com.example.tension.presentation.ui.theme.Title
import com.example.tension.presentation.viewmodels.MainVM
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel


object MainRoute
object LoginRoute
object RegRoute
object SplashRoute
object ProfileRoute
object SettingsRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val vm: MainVM by viewModel()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TensionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val backstack = remember { mutableStateListOf<Any>(SplashRoute) }
                    SetBarsColor()
                    Screen {
                        NavDisplay(
                            backStack = backstack,
                            onBack = { backstack.removeLastOrNull() }) { key ->
                            when (key) {
                                is MainRoute -> NavEntry(key) {
                                    MainScreen(vm, backstack)
                                }
                                is SplashRoute -> NavEntry(key) {
                                    SplashScreen(vm, backstack)
                                }
                                is LoginRoute -> NavEntry(key) {
                                    LoginScreen(vm, backstack)
                                }
                                is RegRoute -> NavEntry(key) {
                                    RegScreen(vm, backstack)
                                }
                                is ProfileRoute -> NavEntry(key) {
                                    ProfileScreen(vm, backstack)
                                }
                                else -> error("")
                            }
                        }
                    }
                }
            }
        }
    }
}

