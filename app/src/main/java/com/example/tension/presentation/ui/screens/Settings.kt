package com.example.tension.presentation.ui.screens

import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tension.R
import com.example.tension.presentation.ui.activities.LoginRoute
import com.example.tension.presentation.ui.activities.ProfileRoute
import com.example.tension.presentation.ui.theme.Body
import com.example.tension.presentation.ui.theme.Label
import com.example.tension.presentation.ui.theme.LocalColors
import com.example.tension.presentation.ui.theme.Screen
import com.example.tension.presentation.ui.theme.SetStatusBarColor
import com.example.tension.presentation.ui.theme.Subtitle
import com.example.tension.presentation.viewmodels.MainVM

@Composable
fun SettingsScreen(vm: MainVM, backStack: SnapshotStateList<Any>) {
    val colors = LocalColors.current
    var notificationsEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    SetStatusBarColor(colors.backgroundPrimary)
    Screen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_down),
                    contentDescription = "Назад",
                    modifier = Modifier
                        .rotate(90f)
                        .size(30.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            backStack.removeAt(backStack.lastIndex)
                        },
                    tint = colors.textPrimary
                )

                Subtitle("Настройки")

                Spacer(Modifier.size(30.dp))
            }

            Spacer(Modifier.height(32.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.backgroundSecondary)
            ) {
                SettingsItem(
                    icon = R.drawable.profile,
                    title = "Профиль",
                    description = vm.user.value?.email ?: "",
                    onClick = {
                        backStack.add(ProfileRoute)
                    }
                )
            }

            Spacer(Modifier.height(24.dp))

            // Уведомления
            Body("Уведомления", modifier = Modifier.padding(bottom = 12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.backgroundSecondary)
            ) {
                SettingsSwitchItem(
                    icon = R.drawable.chat,
                    title = "Push-уведомления",
                    description = "О новых тренировках",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .background(colors.backgroundPrimary)
                )

                SettingsSwitchItem(
                    icon = R.drawable.chat,
                    title = "Звук",
                    description = "Звуковые уведомления",
                    checked = soundEnabled,
                    onCheckedChange = { soundEnabled = it }
                )
            }

            Spacer(Modifier.height(24.dp))

            // Приложение
            Body("Приложение", modifier = Modifier.padding(bottom = 12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.backgroundSecondary)
            ) {
                SettingsItem(
                    icon = R.drawable.settings,
                    title = "О приложении",
                    description = "Версия 1.0.0",
                    onClick = { }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .background(colors.backgroundPrimary)
                )

                SettingsItem(
                    icon = R.drawable.settings,
                    title = "Политика конфиденциальности",
                    onClick = { }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .background(colors.backgroundPrimary)
                )

                SettingsItem(
                    icon = R.drawable.settings,
                    title = "Условия использования",
                    onClick = { }
                )
            }

            Spacer(Modifier.weight(1f))

            // Кнопка выхода
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.backgroundSecondary)
                    .clickable {
                        vm.logout()
                        backStack.clear()
                        backStack.add(LoginRoute)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Label("Выйти из аккаунта", color = androidx.compose.ui.graphics.Color.Red)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun SettingsItem(
    icon: Int,
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    val colors = LocalColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .background(colors.backgroundSecondary)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.backgroundPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colors.textPrimary
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
                    color = colors.textPrimary
                )

                if (description != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                        color = colors.textSecondary
                    )
                }
            }
        }

        Icon(
            painter = painterResource(R.drawable.arrow_down),
            contentDescription = null,
            modifier = Modifier.size(20.dp).rotate(-90f),
            tint = colors.textSecondary
        )
    }
}

@Composable
fun SettingsSwitchItem(
    icon: Int,
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = LocalColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.backgroundSecondary)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.backgroundPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colors.textPrimary
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
                    color = colors.textPrimary
                )

                if (description != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                        color = colors.textSecondary
                    )
                }
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.textPrimary,
                checkedTrackColor = colors.special,
                uncheckedThumbColor = colors.textSecondary,
                uncheckedTrackColor = colors.backgroundPrimary
            )
        )
    }
}