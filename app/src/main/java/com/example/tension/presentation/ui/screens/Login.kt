package com.example.tension.presentation.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tension.presentation.ui.activities.MainRoute
import com.example.tension.presentation.ui.activities.RegRoute
import com.example.tension.presentation.ui.theme.Body
import com.example.tension.presentation.ui.theme.Label
import com.example.tension.presentation.ui.theme.LocalColors
import com.example.tension.presentation.ui.theme.Screen
import com.example.tension.presentation.ui.theme.Subtitle
import com.example.tension.presentation.viewmodels.MainVM

@Composable
fun LoginScreen(vm: MainVM, backStack: SnapshotStateList<Any>) {
    if (vm.user.value != null) {
        backStack.add(MainRoute)
    }
    val colors = LocalColors.current
    Screen {
        Spacer(Modifier.height(80.dp))
        if (vm.errorState.value != "") {
            Toast.makeText(LocalContext.current, vm.errorState.value, Toast.LENGTH_SHORT).show()
            vm.errorState.value = ""
        }
        Subtitle("С возвращением!")
        Spacer(Modifier.height(10.dp))
        Body("Введите ваши данные", color = colors.textSecondary)
        Spacer(Modifier.height(100.dp))
        Label(
            "Email",
            align = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp),
            color = colors.textSecondary
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 10.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                ),
            value = vm.emailState.value,
            onValueChange = { vm.emailState.value = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = colors.backgroundSecondary,
                focusedContainerColor = colors.backgroundSecondary
            ),
            singleLine = true,
            placeholder = {
                Label("test@example.com", color = colors.textSecondary.copy(alpha = 0.25f))
            }
        )
        Spacer(Modifier.height(30.dp))
        Label(
            "Пароль",
            align = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp),
            color = colors.textSecondary
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 10.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                ),
            value = vm.passwordState.value,
            onValueChange = { vm.passwordState.value = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = colors.backgroundSecondary,
                focusedContainerColor = colors.backgroundSecondary
            ),
            singleLine = true,
            placeholder = {
                Label("**********", color = colors.textSecondary.copy(alpha = 0.25f))
            }
        )
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(50.dp)
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (vm.emailState.value != "" && vm.passwordState.value != "") colors.special else colors.backgroundSecondary)
                .clickable(
                    enabled = vm.emailState.value != "" && vm.passwordState.value != ""
                ) {
                    vm.login()
                },
            contentAlignment = Alignment.Center
        ) {
            Label("Войти")
        }
        Spacer(Modifier.height(20.dp))
        Label(
            "Создать аккаунт",
            modifier = Modifier.clickable(
                indication = null,
                interactionSource =  remember { MutableInteractionSource() }
            ) {
                backStack.add(RegRoute)
                Log.d("backstack", backStack.toString())
            },
            color = colors.textSecondary
        )
        Spacer(Modifier.height(70.dp))
    }
}