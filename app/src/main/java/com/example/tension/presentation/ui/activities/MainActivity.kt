package com.example.tension.presentation.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.tension.presentation.ui.screens.ChatScreen
import com.example.tension.presentation.ui.screens.LoginScreen
import com.example.tension.presentation.ui.screens.MainScreen
import com.example.tension.presentation.ui.screens.ProfileScreen
import com.example.tension.presentation.ui.screens.RegScreen
import com.example.tension.presentation.ui.screens.SplashScreen
import com.example.tension.presentation.ui.theme.Screen
import com.example.tension.presentation.ui.theme.SetBarsColor
import com.example.tension.presentation.ui.theme.TensionTheme
import com.example.tension.presentation.viewmodels.MainVM
import org.koin.androidx.viewmodel.ext.android.viewModel


object MainRoute
object LoginRoute
object RegRoute
object SplashRoute
object ProfileRoute
object ChatRoute
object SettingsRoute

class MainActivity : ComponentActivity() {
    val vm: MainVM by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
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
                                is ChatRoute -> NavEntry(key) {
                                    ChatScreen(vm, backstack)
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

    override fun onDestroy() {
        super.onDestroy()
        vm.clearChat()
    }
}

