package com.example.tension.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.tension.R
import com.example.tension.presentation.ui.activities.LoginRoute
import com.example.tension.presentation.ui.activities.MainRoute
import com.example.tension.presentation.ui.theme.*
import com.example.tension.presentation.viewmodels.MainVM

@Composable
fun ProfileScreen(vm: MainVM, backStack: SnapshotStateList<Any>) {
    val colors = LocalColors.current
    var isEditing by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(vm.user.value?.name ?: "") }
    var weight by remember {
        mutableStateOf(vm.user.value?.weight?.let { if (it > 0) it.toString() else "" } ?: "")
    }
    var height by remember {
        mutableStateOf(vm.user.value?.height?.let { if (it > 0) it.toString() else "" } ?: "")
    }
    var aim by remember { mutableStateOf(vm.user.value?.aim ?: 0) }
    var difficult by remember { mutableStateOf(vm.user.value?.difficult ?: 0) }

    Screen {
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .rotate(90f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            backStack.add(MainRoute)
                        },
                    tint = colors.textPrimary
                )

                Spacer(modifier = Modifier.weight(1f))
                Subtitle("Профиль")
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(R.drawable.logout),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            vm.logout()
                            backStack.clear()
                            backStack.add(LoginRoute)
                        },
                    tint = colors.textPrimary
                )
            }

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.special),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.headlineLarge,
                        color = colors.textPrimary
                    )
                }

                Spacer(Modifier.height(16.dp))
                Body(vm.user.value?.email ?: "", color = colors.textSecondary)
            }

            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.backgroundSecondary)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Имя
                ProfileField(
                    title = "Имя",
                    value = name,
                    isEditing = isEditing,
                    onValueChange = { name = it },
                    keyboardType = KeyboardType.Text
                )

                MyDivider(color = colors.backgroundPrimary, thickness = 1.dp)

                // Вес (валидация: цифры и одна точка)
                ProfileField(
                    title = "Вес (кг)",
                    value = weight,
                    isEditing = isEditing,
                    placeholder = "0.0",
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it == '.' } && input.count { it == '.' } <= 1) {
                            weight = input
                        }
                    }
                )

                MyDivider(color = colors.backgroundPrimary, thickness = 1.dp)

                // Рост (валидация: только цифры)
                ProfileField(
                    title = "Рост (см)",
                    value = height,
                    isEditing = isEditing,
                    placeholder = "0",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            height = input
                        }
                    }
                )

                MyDivider(color = colors.backgroundPrimary, thickness = 1.dp)

                // Цель
                ProfileDropdownField(
                    title = "Цель",
                    options = listOf("Похудение", "Набор массы", "Поддержание формы"),
                    selectedIndex = aim,
                    isEditing = isEditing,
                    onSelectionChange = { aim = it },
                )

                MyDivider(color = colors.backgroundPrimary, thickness = 1.dp)

                // Уровень
                ProfileDropdownField(
                    title = "Уровень",
                    options = listOf("Новичок", "Продвинутый", "Профессионал"),
                    selectedIndex = difficult,
                    isEditing = isEditing,
                    onSelectionChange = { difficult = it },
                )
            }

            Spacer(Modifier.weight(1f))

            // Кнопки управления
            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.backgroundSecondary)
                            .clickable {
                                isEditing = false
                                // Сброс к исходным значениям из VM
                                name = vm.user.value?.name ?: ""
                                weight = vm.user.value?.weight?.takeIf { it > 0 }?.toString() ?: ""
                                height = vm.user.value?.height?.takeIf { it > 0 }?.toString() ?: ""
                                aim = vm.user.value?.aim ?: 0
                                difficult = vm.user.value?.difficult ?: 0
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Label("Отмена")
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.textPrimary)
                            .clickable {
                                vm.updateUser(
                                    name = name,
                                    weight = weight.toDoubleOrNull() ?: 0.0,
                                    height = height.toDoubleOrNull() ?: 0.0,
                                    aim = aim,
                                    difficult = difficult
                                )
                                isEditing = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Label("Сохранить", color = colors.backgroundPrimary)
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.special)
                        .clickable { isEditing = true },
                    contentAlignment = Alignment.Center
                ) {
                    Label("Изменить", color = colors.textPrimary)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileField(
    title: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    placeholder: String = ""
) {
    val colors = LocalColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Body(title)

        if (isEditing) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 17.sp,
                    color = colors.textPrimary,
                    textAlign = TextAlign.End
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.backgroundPrimary)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = colors.textSecondary.copy(alpha = 0.3f),
                                textAlign = TextAlign.End
                            )
                        }
                        innerTextField()
                    }
                }
            )
        } else {
            // Логика "Не указано"
            val isUnspecified = value.isEmpty() || value == "0" || value == "0.0"
            val displayText = if (isUnspecified) "Не указано" else value
            val displayColor = if (isUnspecified) colors.textSecondary.copy(alpha = 0.4f) else colors.textSecondary

            Label(displayText, color = displayColor)
        }
    }
}

@Composable
fun ProfileDropdownField(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    isEditing: Boolean,
    onSelectionChange: (Int) -> Unit
) {
    val colors = LocalColors.current
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Body(title)

        if (isEditing) {
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.backgroundPrimary)
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Label(options.getOrNull(selectedIndex) ?: "Выбрать")
                    Icon(
                        painter = painterResource(R.drawable.arrow_down),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colors.textPrimary
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(focusable = true),
                    modifier = Modifier.background(colors.backgroundSecondary)
                ) {
                    options.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            text = { Label(option) },
                            onClick = {
                                onSelectionChange(index)
                                expanded = false
                            }
                        )
                    }
                }
            }
        } else {
            Label(options.getOrNull(selectedIndex) ?: "Не указано", color = colors.textSecondary)
        }
    }
}

@Composable
fun MyDivider(color: Color, thickness: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}