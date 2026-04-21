package com.example.tension.presentation.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.tension.presentation.ui.activities.MainRoute
import com.example.tension.presentation.ui.activities.RegRoute
import com.example.tension.presentation.ui.theme.*
import com.example.tension.presentation.viewmodels.MainVM

@Composable
fun LoginScreen(vm: MainVM, backStack: SnapshotStateList<Any>) {
    val context = LocalContext.current
    val colors = LocalColors.current
    var passwordVisible by remember { mutableStateOf(false) }

    // Логика перехода
    if (vm.user.value != null) {
        LaunchedEffect(Unit) { backStack.add(MainRoute) }
    }

    Screen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
        ) {
            Spacer(Modifier.height(70.dp))

            // Заголовок с акцентом
            Text(
                text = "БЫСТРЕЕ.\nВЫШЕ.\nСИЛЬНЕЕ.",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                lineHeight = 36.sp,
                letterSpacing = (-1).sp,
                color = colors.textPrimary
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Авторизуйтесь, чтобы продолжить тренировки",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(56.dp))

            // Поля ввода
            SportInput(
                label = "ВАШ EMAIL",
                value = vm.emailState.value,
                onValueChange = { vm.emailState.value = it },
                placeholder = "example@fit.com",
                colors = colors
            )

            Spacer(Modifier.height(24.dp))

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

            Spacer(Modifier.weight(1f))

            // Энергичная кнопка с градиентом
            val isFormValid = vm.emailState.value.isNotBlank() && vm.passwordState.value.isNotBlank()

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
                    .clickable(enabled = isFormValid) { vm.login() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ПРОДОЛЖИТЬ",
                    color = if (isFormValid) Color.Black else colors.textSecondary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp
                )
            }

            // Ссылка на регистрацию
            Text(
                text = "НОВЫЙ АТЛЕТ? СОЗДАТЬ АККАУНТ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { backStack.add(RegRoute) },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = colors.special
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
    colors: AppColor
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Анимация толщины обводки и цвета при фокусе
    val borderThickness by animateDpAsState(if (isFocused) 2.dp else 1.dp, label = "")
    val borderColor by animateColorAsState(if (isFocused) colors.special else colors.textSecondary.copy(alpha = 0.2f), label = "")

    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = if (isFocused) colors.special else colors.textSecondary,
            letterSpacing = 1.sp
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
                            tint = if (isFocused) colors.special else colors.textSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
            placeholder = {
                Text(placeholder, color = colors.textSecondary.copy(alpha = 0.3f), fontSize = 16.sp)
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.special.copy(alpha = 0.05f),
                unfocusedContainerColor = colors.backgroundSecondary.copy(alpha = 0.4f),
                focusedBorderColor = Color.Transparent, // Убираем стандартную, так как юзаем .border
                unfocusedBorderColor = Color.Transparent,
                cursorColor = colors.special
            )
        )
    }
}