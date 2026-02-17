package com.example.tension.presentation.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tension.R
import com.example.tension.presentation.models.ChatUiMessage
import com.example.tension.presentation.ui.activities.MainRoute
import com.example.tension.presentation.ui.theme.Body
import com.example.tension.presentation.ui.theme.Label
import com.example.tension.presentation.ui.theme.LocalColors
import com.example.tension.presentation.ui.theme.Screen
import com.example.tension.presentation.ui.theme.Subtitle
import com.example.tension.presentation.viewmodels.MainVM
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(vm: MainVM, backStack: SnapshotStateList<Any>) {
    val colors = LocalColors.current
    val messages by vm.messages.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    var messageText by remember { mutableStateOf("") }
    var isNewChat by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }
    if (vm.errorState.value != "") {
        Toast.makeText(LocalContext.current, vm.errorState.value, Toast.LENGTH_SHORT).show()
        vm.errorState.value = ""
    }
    Screen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(50.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.arrow_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .rotate(180f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            backStack.add(MainRoute)
                        }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Subtitle("AI Ассистент")
                Spacer(modifier = Modifier.weight(1f))
            }

            // Список сообщений
            if (messages.isEmpty()) {
                // Пустое состояние
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(colors.special),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.chat), // добавьте иконку
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = colors.textPrimary
                            )
                        }
                        Body("Начните диалог с AI ассистентом")
                        Label(
                            "Задавайте вопросы о тренировках",
                            color = colors.textSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(messages) { message ->
                        MessageBubble(
                            message = message
                        )
                    }

                    if (isLoading) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(colors.backgroundSecondary)
                                        .padding(16.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = colors.textPrimary,
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "новый чат",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colors.textSecondary,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.clickable(
                        enabled = !isNewChat && !isLoading,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        vm.clearChat()
                        isNewChat = true
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                BasicTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.backgroundSecondary)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = colors.textPrimary,
                        fontSize = 16.sp
                    ),
                    decorationBox = { innerTextField ->
                        if (messageText.isEmpty()) {
                            Column(
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(
                                    text = "Введите сообщение...",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colors.textSecondary,
                                        fontSize = 16.sp
                                    )
                                )
                            }

                        }
                        innerTextField()
                    }
                )

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (messageText.isNotBlank() && !isLoading) colors.special
                            else colors.backgroundSecondary
                        )
                        .clickable(enabled = messageText.isNotBlank() && !isLoading) {
                            vm.sendMessage(messageText)
                            messageText = ""
                            isNewChat = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.send),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (messageText.isNotBlank() && !isLoading) colors.textPrimary
                        else colors.textSecondary
                    )
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun MessageBubble(
    message: ChatUiMessage,
) {
    val colors = LocalColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (message.isUser) 20.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 20.dp
                    )
                )
                .background(
                    if (message.isUser) colors.special
                    else colors.backgroundSecondary
                )
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    color = colors.textPrimary
                ),
                textAlign = TextAlign.Start
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = formatTimestamp(message.timestamp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp,
                    color = colors.textSecondary.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    return format.format(date)
}