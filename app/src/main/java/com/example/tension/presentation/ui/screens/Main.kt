package com.example.tension.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tension.R
import com.example.tension.presentation.models.Workout
import com.example.tension.presentation.ui.activities.ChatRoute
import com.example.tension.presentation.ui.activities.ProfileRoute
import com.example.tension.presentation.ui.activities.SettingsRoute
import com.example.tension.presentation.ui.theme.Body
import com.example.tension.presentation.ui.theme.Label
import com.example.tension.presentation.ui.theme.LocalColors
import com.example.tension.presentation.ui.theme.Screen
import com.example.tension.presentation.ui.theme.Subtitle
import com.example.tension.presentation.viewmodels.MainVM
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun MainScreen(vm: MainVM, backStack: SnapshotStateList<Any>) {
    var isExpanded by remember { mutableStateOf(false) }
    var isButtonExpanded by remember { mutableStateOf(false) }
    var isWorkoutsExpanded by remember { mutableStateOf(false) }
    val colors = LocalColors.current

    Screen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (!isButtonExpanded) {
                        Modifier.pointerInput(Unit) {
                            detectVerticalDragGestures { change, dragAmount ->
                                change.consume()
                                if (dragAmount > 50 && !isExpanded) {
                                    isExpanded = true
                                } else if (dragAmount < -50 && isExpanded) {
                                    isExpanded = false
                                }
                            }
                        }
                    } else {
                        Modifier
                    }
                )
        ) {
            val heightState by animateDpAsState(
                targetValue = when {
                    isWorkoutsExpanded -> 1000.dp
                    isExpanded -> 700.dp
                    else -> 350.dp
                },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ),
            )

            val topSpacerHeight by animateDpAsState(
                targetValue = if (isExpanded || isWorkoutsExpanded) 50.dp else 80.dp,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ),
            )

            val rowHeight by animateDpAsState(
                targetValue = when {
                    isWorkoutsExpanded -> 800.dp
                    isExpanded -> 400.dp
                    else -> 200.dp
                },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ),
            )

            AnimatedVisibility(visible = !isButtonExpanded) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .height(heightState)
                            .background(colors.special),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(topSpacerHeight))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.settings),
                                contentDescription = null, modifier = Modifier
                                    .padding(24.dp)
                                    .size(30.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        backStack.add(SettingsRoute)
                                    }
                            )
                            Subtitle("Сегодня", modifier = Modifier.padding(12.dp))
                            Icon(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(24.dp)
                                    .size(30.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        backStack.add(ProfileRoute)
                                    }
                            )
                        }

                        if (!isWorkoutsExpanded) {
                            Row(
                                modifier = Modifier
                                    .padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .fillMaxWidth()
                                    .height(rowHeight)
                                    .background(colors.backgroundSecondary)
                            ) {
                                if (vm.currentWorkout.value != null) {
                                    AnimatedVisibility(
                                        visible = !isExpanded,
                                        enter = fadeIn() + expandHorizontally(),
                                        exit = fadeOut() + shrinkHorizontally()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(24.dp))
                                                .fillMaxHeight()
                                                .width(130.dp)
                                                .background(colors.backgroundPrimary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.weight),
                                                contentDescription = null
                                            )
                                        }
                                    }

                                    AnimatedVisibility(visible = !isExpanded) {
                                        Spacer(modifier = Modifier.width(10.dp))
                                    }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 10.dp)
                                    ) {
                                        Text(
                                            text = vm.currentWorkout.value?.title ?: "",
                                            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                                            color = colors.textPrimary,
                                            style = if (isExpanded) MaterialTheme.typography.bodyMedium
                                            else MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
                                        )

                                        Text(
                                            text = if (isExpanded) {
                                                vm.currentWorkout.value?.desc ?: ""
                                            } else {
                                                (vm.currentWorkout.value?.desc?.take(80) ?: "") + "..."
                                            },
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            color = colors.textSecondary,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 17.sp)
                                        )

                                        AnimatedVisibility(visible = isExpanded) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 12.dp)
                                            ) {
                                                Spacer(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(1.dp)
                                                        .background(colors.textSecondary.copy(alpha = 0.3f))
                                                )
                                                Spacer(Modifier.height(12.dp))

                                                Body("Упражнения:", modifier = Modifier.padding(bottom = 8.dp))

                                                repeat(vm.currentWorkout.value?.exercises?.size ?: 0) { index ->
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(vertical = 4.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        val ex = vm.currentWorkout.value?.exercises?.getOrNull(index)
                                                        Label(ex?.title ?: "")
                                                        Label(
                                                            (ex?.reps + "x" + ex?.sets),
                                                            color = colors.textSecondary
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Subtitle("Тренировок нет")
                                    }
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .background(colors.backgroundSecondary)
                                    .padding(16.dp)
                            ) {
                                // Заголовок
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if ((vm.userWorkouts.value?.size ?: 0) != 0) {
                                        Body("Все тренировки")
                                        Label(
                                            "${vm.userWorkouts.value?.size ?: 0} тренировок",
                                            color = colors.textSecondary
                                        )
                                    } else (
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Subtitle("Тренировок нет")
                                            }
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                // Список тренировок
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    vm.userWorkouts.value?.forEach { workout ->
                                        WorkoutListItem(
                                            workout = workout,
                                            onClick = { }
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                                    .padding(bottom = 20.dp)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colors.textPrimary)
                                    .clickable {
                                        isWorkoutsExpanded = false
                                        isExpanded = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Label("Назад", color = colors.backgroundSecondary)
                                }
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                        }


                        if (!isWorkoutsExpanded) {
                            AnimatedVisibility(visible = isExpanded) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    val isDone = vm.currentWorkout.value?.isDone ?: false


                                    Box(
                                        modifier = Modifier
                                            .height(50.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (!isDone) colors.textPrimary else colors.backgroundSecondary)
                                            .clickable(enabled = !isDone) {
                                                isExpanded = false
                                                vm.completeCurrentWorkout()
                                                vm.getData()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Label(
                                            text = if (!isDone) "Завершить" else "Завершено",
                                            color = if (!isDone) colors.backgroundPrimary else colors.textSecondary
                                        )
                                    }

                                    Spacer(Modifier.height(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(50.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(colors.textPrimary)
                                            .clickable {
                                                isExpanded = false
                                                isWorkoutsExpanded = true
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Label("Мои тренировки", color = colors.backgroundSecondary)
                                    }

                                    Spacer(Modifier.height(20.dp))
                                }
                            }
                        }
                    }

                    Body(
                        "Статистика", modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, top = 12.dp, bottom = 16.dp),
                        align = TextAlign.Start
                    )

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.backgroundSecondary)
                            .fillMaxWidth()
                            .height(232.dp)
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        StatRow(
                            vm.userStats.value?.totalWorkouts ?: 0,
                            "Всего тренировок",
                            "за все вермя"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        StatRow(
                            vm.userStats.value?.totalExercises ?: 0,
                            "Упражнений выполнен",
                            "за все тренировки"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        StatRow(
                            vm.userStats.value?.completionRate?.toInt() ?: 0,
                            "Процент выполнения",
                            "всех тренировок",
                            isPer = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                    }
//
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.textPrimary)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                backStack.add(ChatRoute)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.chat),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = colors.special
                            )
                            Label("AI Помощник", color = colors.backgroundPrimary)
                        }
                    }

                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isButtonExpanded) Modifier.fillMaxHeight()
                        else Modifier.wrapContentHeight()
                    )
                    .padding(horizontal = if (isButtonExpanded) 0.dp else 24.dp)
                    .padding(bottom = if (isButtonExpanded) 0.dp else 24.dp)
                    .clip(RoundedCornerShape(if (isButtonExpanded) 0.dp else 12.dp))
                    .background(colors.special)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessHigh
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (!isButtonExpanded) {
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .clickable {
                                isButtonExpanded = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Label("Подобрать тренировки")
                    }
                }


                AnimatedVisibility(
                    visible = isButtonExpanded,
                    enter = fadeIn(
                        animationSpec = tween(durationMillis = 600)
                    ) + expandVertically(
                        expandFrom = Alignment.Bottom,
                        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                    ),
                    exit = fadeOut(
                        animationSpec = tween(durationMillis = 400)
                    ) + shrinkVertically(
                        shrinkTowards = Alignment.Bottom,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )
                ) {
                    

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(50.dp))

                        Subtitle("Подбор тренировок", modifier = Modifier.padding(bottom = 16.dp))

                        Body("Настройте план тренировок:")
                        Spacer(Modifier.height(24.dp))

                        // Частота в неделю
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.backgroundSecondary)
                                .padding(16.dp)
                        ) {
                            Body("Частота тренировок в неделю")
                            Spacer(Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(colors.backgroundPrimary)
                                        .clickable(enabled = vm.freq != null && vm.freq!! > 1) {
                                            vm.freq = vm.freq?.let { if (it > 1) it - 1 else it }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "−",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = if (vm.freq != null && vm.freq!! > 1) colors.textPrimary else colors.textSecondary
                                    )
                                }


                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        if (vm.freq == null) vm.freq = 3 // значение по умолчанию
                                    }
                                ) {
                                    if (vm.freq != null) {
                                        Text(
                                            text = "${vm.freq}",
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                            ),
                                            color = colors.textPrimary
                                        )
                                        Label("раз в неделю", color = colors.textSecondary)
                                    } else {
                                        Text(
                                            text = "—",
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = colors.textSecondary
                                        )
                                        Label("не выбрано", color = colors.textSecondary)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(colors.backgroundPrimary)
                                        .clickable(enabled = vm.freq == null || vm.freq!! < 7) {
                                            vm.freq =
                                                vm.freq?.let { if (it < 7) it + 1 else it } ?: 3
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = if (vm.freq == null || vm.freq!! < 7) colors.textPrimary else colors.textSecondary
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.backgroundSecondary)
                                .padding(16.dp)
                        ) {
                            Body("Срок достижения цели")
                            Spacer(Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(colors.backgroundPrimary)
                                        .clickable(enabled = vm.months != null && vm.months!! > 1) {
                                            vm.months =
                                                vm.months?.let { if (it > 1) it - 1 else it }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "−",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = if (vm.months != null && vm.months!! > 1) colors.textPrimary else colors.textSecondary
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        if (vm.months == null) vm.months = 3 // значение по умолчанию
                                    }
                                ) {
                                    if (vm.months != null) {
                                        Text(
                                            text = "${vm.months}",
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                            ),
                                            color = colors.textPrimary
                                        )
                                        Label(
                                            text = when (vm.months) {
                                                1 -> "месяц"
                                                in 2..4 -> "месяца"
                                                else -> "месяцев"
                                            },
                                            color = colors.textSecondary
                                        )
                                    } else {
                                        Text(
                                            text = "—",
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = colors.textSecondary
                                        )
                                        Label("не выбрано", color = colors.textSecondary)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(colors.backgroundPrimary)
                                        .clickable(enabled = vm.months == null || vm.months!! < 12) {
                                            vm.months = vm.months?.let { if (it < 12) it + 1 else it } ?: 3
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = if (vm.months == null || vm.months!! < 12) colors.textPrimary else colors.textSecondary
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        if (vm.freq != null && vm.months != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colors.special.copy(alpha = 0.3f))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Label(
                                    "План: ${vm.freq!! * vm.months!! * 4} тренировок за ${vm.months} мес.",
                                    color = colors.textSecondary
                                )
                            }

                            Spacer(Modifier.height(16.dp))
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (vm.freq != null && vm.months != null) colors.textPrimary
                                    else colors.backgroundPrimary
                                )
                                .clickable(enabled = vm.freq != null && vm.months != null) {
                                        vm.generateWorkoutPlan()
                                        isButtonExpanded = false

                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Label(
                                "Создать план",
                                color = if (vm.freq != null && vm.months != null) colors.backgroundPrimary
                                else colors.textSecondary
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Кнопка Свернуть
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.backgroundSecondary)
                                .clickable {
                                    isButtonExpanded = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Label("Отмена")
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}



@Composable
fun StatRow(n: Int, title: String, desc: String, isPer: Boolean = false) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .height(60.dp)
            .background(LocalColors.current.backgroundPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 17.sp)
            )
            Text(
                text = desc,
                color = LocalColors.current.textSecondary,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 15.sp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(35.dp)
                .clip(CircleShape)
                .border(color = getColor(n, isPer = isPer), width = 2.dp, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Label(n.toString())
        }
        Spacer(modifier = Modifier.width(20.dp))
    }
}


@Composable
fun getColor(n: Int, isPer: Boolean = false): Color {
    if (isPer) {
        return when (n) {
            0 -> LocalColors.current.backgroundSecondary
            in 1..25 -> Color.Red
            in 26..75 -> Color.Yellow
            else -> Color.Green
        }
    }
    return when (n) {
        0 -> LocalColors.current.backgroundSecondary
        1, 2 -> Color.Yellow
        else -> Color.Green
    }
}

@Composable
fun WorkoutListItem(
    workout: Workout,
    onClick: () -> Unit
) {
    val colors = LocalColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.backgroundPrimary)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = workout.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
                color = colors.textPrimary
            )

            Spacer(Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Label("${workout.exercises?.size ?: 0} упр.", color = colors.textSecondary)
                Label("•", color = colors.textSecondary)
                Label(workout.date.take(10), color = colors.textSecondary)
            }
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (workout.isDone) Color.Green.copy(alpha = 0.2f) else colors.backgroundSecondary),
            contentAlignment = Alignment.Center
        ) {
            if (workout.isDone) {
                Text("✓", color = Color.Green, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}



