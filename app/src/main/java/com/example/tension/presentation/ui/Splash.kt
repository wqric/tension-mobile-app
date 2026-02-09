package com.example.tension.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.example.tension.R
import com.example.tension.presentation.ui.theme.LocalColors
import com.example.tension.presentation.ui.theme.Screen
import com.example.tension.presentation.ui.theme.Title
import com.example.tension.presentation.viewmodels.MainVM
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(vm: MainVM, backStack: SnapshotStateList<Any>) {
    LaunchedEffect({}) {
        delay(2000)
        backStack.removeLastOrNull()
        if (vm.user.value != null) {
            vm.getData()
            backStack.add(MainRoute)
        } else {
            backStack.add(LoginRoute)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.current.backgroundPrimary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.tension),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
                .padding(bottom = 20.dp),
            tint = LocalColors.current.special
        )
        Title("Tension")
    }
}