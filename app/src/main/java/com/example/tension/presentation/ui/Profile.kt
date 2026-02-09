package com.example.tension.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.tension.R
import com.example.tension.presentation.ui.theme.Label
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tension.presentation.ui.theme.Body
import com.example.tension.presentation.ui.theme.LocalColors
import com.example.tension.presentation.ui.theme.Screen
import com.example.tension.presentation.ui.theme.Subtitle
import com.example.tension.presentation.viewmodels.MainVM

@Composable
fun ProfileScreen(vm: MainVM, backStack: SnapshotStateList<Any>) {
    val colors = LocalColors.current
    var isEditing by remember { mutableStateOf(false) }

    // Состояния для редактирования
    var name by remember { mutableStateOf(vm.user.value?.name ?: "") }
    var weight by remember { mutableStateOf(vm.user.value?.weight?.toString() ?: "") }
    var height by remember { mutableStateOf(vm.user.value?.height?.toString() ?: "") }
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
                            interactionSource =  remember { MutableInteractionSource() }
                        ) {
                            backStack.add(MainRoute)
                        }
                )

                Spacer(modifier = Modifier.weight(1f))
                Subtitle("Профиль")
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.logout),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .rotate(180f)
                        .clickable(
                            indication = null,
                            interactionSource =  remember { MutableInteractionSource() }
                        ) {
                            vm.logout()
                            backStack.clear()
                            backStack.add(LoginRoute)
                        }
                )
            }
            Spacer(Modifier.height(24.dp))

            // Аватар и email
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

                Body(vm.user.value?.email ?: "")
            }

            Spacer(Modifier.height(32.dp))

            // Поля профиля
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
                    onValueChange = { name = it }
                )

                MyDivider(color = colors.backgroundPrimary, thickness = 1.dp)

                // Вес
                ProfileField(
                    title = "Вес (кг)",
                    value = weight,
                    isEditing = isEditing,
                    onValueChange = { weight = it },
                    keyboardType = KeyboardType.Number
                )

                Divider(color = colors.backgroundPrimary, thickness = 1.dp)

                // Рост
                ProfileField(
                    title = "Рост (см)",
                    value = height,
                    isEditing = isEditing,
                    onValueChange = { height = it },
                    keyboardType = KeyboardType.Number
                )

                MyDivider(color = colors.backgroundPrimary, thickness = 1.dp)

                // Цель
                ProfileDropdownField(
                    title = "Цель",
                    options = listOf("Похудение", "Набор массы", "Поддержание формы"),
                    selectedIndex = aim,
                    isEditing = isEditing,
                    onSelectionChange = { aim = it }
                )

                MyDivider(color = colors.backgroundPrimary, thickness = 1.dp)

                // Уровень
                ProfileDropdownField(
                    title = "Уровень",
                    options = listOf("Новичок", "Продвинутый", "Профессионал"),
                    selectedIndex = difficult,
                    isEditing = isEditing,
                    onSelectionChange = { difficult = it }
                )
            }

            Spacer(Modifier.weight(1f))

            // Кнопки
            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Кнопка Отмена
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.backgroundSecondary)
                            .clickable {
                                isEditing = false
                                // Сброс значений
                                name = vm.user.value?.name ?: ""
                                weight = vm.user.value?.weight?.toString() ?: ""
                                height = vm.user.value?.height?.toString() ?: ""
                                aim = vm.user.value?.aim ?: 0
                                difficult = vm.user.value?.difficult ?: 0
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Label("Отмена")
                    }

                    // Кнопка Сохранить
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
                // Кнопка Изменить
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.special)
                        .clickable {
                            isEditing = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Label("Изменить")
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
    keyboardType: KeyboardType = KeyboardType.Text
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
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true,
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.backgroundPrimary)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        } else {
            Label(value, color = colors.textSecondary)
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
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
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
            Label(options.getOrNull(selectedIndex) ?: "", color = colors.textSecondary)
        }
    }
}

@Composable
fun MyDivider(color: androidx.compose.ui.graphics.Color, thickness: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}