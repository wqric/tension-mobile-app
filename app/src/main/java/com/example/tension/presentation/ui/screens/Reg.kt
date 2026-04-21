package com.example.tension.presentation.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tension.R
import com.example.tension.presentation.ui.activities.LoginRoute
import com.example.tension.presentation.ui.activities.MainRoute
import com.example.tension.presentation.ui.theme.*
import com.example.tension.presentation.viewmodels.MainVM

@Composable
fun RegScreen(vm: MainVM, backStack: SnapshotStateList<Any>) {
    val context = LocalContext.current
    val colors = LocalColors.current
    var passwordVisible by remember { mutableStateOf(false) }

    // Логика автоматического перехода при успехе
    LaunchedEffect(vm.user.value) {
        if (vm.user.value != null) {
            backStack.add(MainRoute)
        }
    }

    // Обработка ошибок
    LaunchedEffect(vm.errorState.value) {
        if (vm.errorState.value.isNotEmpty()) {
            Toast.makeText(context, vm.errorState.value, Toast.LENGTH_SHORT).show()
            vm.errorState.value = ""
        }
    }

    Screen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()) // Для маленьких экранов, так как полей много
        ) {
            Spacer(Modifier.height(60.dp))

            // Энергичный заголовок
            Text(
                text = "СТАНЬ\nЛУЧШЕ.",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                lineHeight = 38.sp,
                letterSpacing = (-1.5).sp,
                color = colors.textPrimary
            )

            Text(
                text = "Создайте профиль атлета",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(48.dp))

            // Email поле
            SportInput(
                label = "ВАШ EMAIL",
                value = vm.emailState.value,
                onValueChange = { vm.emailState.value = it },
                placeholder = "example@fit.com",
                colors = colors
            )

            Spacer(Modifier.height(20.dp))

            // Пароль
            SportInput(
                label = "ПАРОЛЬ",
                value = vm.passwordState.value,
                onValueChange = { vm.passwordState.value = it },
                placeholder = "••••••••",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible },
                colors = colors
            )

            Spacer(Modifier.height(20.dp))

            // Повтор пароля
            val passwordsMatch = vm.passwordRepeatState.value == vm.passwordState.value
            SportInput(
                label = "ПОВТОР ПАРОЛЯ",
                value = vm.passwordRepeatState.value,
                onValueChange = { vm.passwordRepeatState.value = it },
                placeholder = "••••••••",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible },
                colors = colors,
                isError = vm.passwordRepeatState.value.isNotEmpty() && !passwordsMatch
            )

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(40.dp))

            // Проверка валидности формы
            val isFormValid = vm.emailState.value.isNotBlank() &&
                    vm.passwordState.value.isNotBlank() &&
                    vm.passwordRepeatState.value.isNotBlank() &&
                    passwordsMatch

            // Градиентная кнопка
            val buttonBrush = if (isFormValid) {
                Brush.horizontalGradient(listOf(colors.special, colors.special.copy(alpha = 0.8f)))
            } else {
                Brush.horizontalGradient(listOf(colors.backgroundSecondary, colors.backgroundSecondary))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(buttonBrush)
                    .clickable(enabled = isFormValid) { vm.reg() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "СОЗДАТЬ АККАУНТ",
                    color = if (isFormValid) Color.Black else colors.textSecondary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            // Переход обратно на логин
            Text(
                text = "УЖЕ ЕСТЬ АККАУНТ? ВОЙТИ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        backStack.add(LoginRoute)
                    },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = colors.textSecondary
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun SportInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    colors: AppColor,
    isError: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderThickness by animateDpAsState(if (isFocused) 2.dp else 1.dp, label = "")
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Color.Red // Можно заменить на colors.error, если есть в теме
            isFocused -> colors.special
            else -> colors.textSecondary.copy(alpha = 0.2f)
        },
        label = ""
    )

    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = if (isError) Color.Red else if (isFocused) colors.special else colors.textSecondary,
            letterSpacing = 1.2.sp
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(borderThickness, borderColor, RoundedCornerShape(14.dp)),
            interactionSource = interactionSource,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword && onTogglePassword != null) {
                    IconButton(onClick = onTogglePassword) {
                        Icon(
                            painter = painterResource(if (passwordVisible) R.drawable.eye_off else R.drawable.eye_on),
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
            placeholder = {
                Text(placeholder, color = colors.textSecondary.copy(alpha = 0.3f), fontSize = 15.sp)
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.special.copy(alpha = 0.03f),
                unfocusedContainerColor = colors.backgroundSecondary.copy(alpha = 0.4f),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = colors.special
            )
        )
    }
}