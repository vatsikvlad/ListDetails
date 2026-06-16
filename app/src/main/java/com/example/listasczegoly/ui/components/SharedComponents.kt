package com.example.listasczegoly.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.listasczegoly.viewmodel.ElementsViewModel
import java.util.Locale

val IconMountain: ImageVector
    get() = ImageVector.Builder(
        name = "Mountain",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(fill = androidx.compose.ui.graphics.SolidColor(Color.Black)) {
        moveTo(14f, 6f)
        lineTo(10.25f, 11f)
        lineTo(13.1f, 11f)
        lineTo(7f, 19f)
        horizontalLineTo(22f)
        lineTo(14f, 6f)
        close()
        moveTo(2f, 19f)
        lineTo(9f, 9f)
        lineTo(13f, 14.6f)
        lineTo(11.2f, 17f)
        lineTo(9f, 14.1f)
        lineTo(5.2f, 19f)
        horizontalLineTo(2f)
        close()
    }.build()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    title: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMenuClick: () -> Unit,
    onThemeToggle: () -> Unit,
    isDarkTheme: Boolean,
    isSearchActive: Boolean,
    onSearchToggle: (Boolean) -> Unit
) {
    TopAppBar(
        title = {
            if (isSearchActive) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Szukaj trasy...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            } else {
                Text(title, style = MaterialTheme.typography.titleLarge)
            }
        },
        navigationIcon = {
            if (isSearchActive) {
                IconButton(onClick = { onSearchToggle(false); onSearchQueryChange("") }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Cofnij")
                }
            } else {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, "Menu")
                }
            }
        },
        actions = {
            if (!isSearchActive) {
                IconButton(onClick = { onSearchToggle(true) }) {
                    Icon(Icons.Default.Search, "Szukaj")
                }
            }
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Motyw"
                )
            }
        }
    )
}

@Composable
fun StopwatchDisplay(viewModel: ElementsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatTime(viewModel.timeElapsed),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(onClick = { viewModel.startTimer() }) {
                Icon(Icons.Default.PlayArrow, null)
                Text("Start")
            }
            FilledTonalButton(onClick = { viewModel.stopTimer() }) {
                Icon(Icons.Default.Stop, null)
                Text("Stop")
            }
            OutlinedButton(onClick = { viewModel.resetTimer() }) {
                Icon(Icons.Default.Refresh, null)
                Text("Reset")
            }
        }
    }
}

@Composable
fun RecordsList(viewModel: ElementsViewModel, routeTitle: String) {
    val records = viewModel.savedRecords[routeTitle] ?: emptyList()
    
    if (records.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Twoje osiągnięcia:", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary)
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) {
            records.reversed().forEach { record ->
                ListItem(
                    headlineContent = { Text(formatTime(record.time), fontWeight = FontWeight.Bold) },
                    supportingContent = { Text(record.date) },
                    leadingContent = {
                        IconButton(onClick = { viewModel.deleteRecord(routeTitle, record) }) {
                            Icon(Icons.Default.Delete, "Usuń", tint = MaterialTheme.colorScheme.error)
                        }
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
                HorizontalDivider()
            }
        }
    }
}

fun formatTime(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return String.format("%02d:%02d:%02d", h, m, s)
}

@Composable
fun TextToggle(isRower: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = !isRower,
            onClick = { onChange(false) },
            label = { Text("Piesze") }
        )
        FilterChip(
            selected = isRower,
            onClick = { onChange(true) },
            label = { Text("Rowerowe") }
        )
    }
}

@Composable
fun ToggleButtonItem(
    text: String, 
    isSelected: Boolean, 
    tiltX: Float = 0f, 
    tiltY: Float = 0f, 
    onToggle: () -> Unit
) {
    val transition = updateTransition(targetState = isSelected, label = "ToggleTransition")

    val scale by transition.animateFloat(
        transitionSpec = {
            if (targetState) {
                spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
            } else {
                tween(durationMillis = 200, easing = LinearOutSlowInEasing)
            }
        },
        label = "ScaleAnimation"
    ) { state -> if (state) 1.05f else 1f }

    val containerColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 300, easing = FastOutSlowInEasing) },
        label = "ColorAnimation"
    ) { state ->
        if (state) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant
    }

    Button(
        onClick = onToggle,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 4.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = IconMountain,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = (tiltX * 4).dp, y = (tiltY * 4).dp),
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.width(12.dp))
            Text(text = text, style = MaterialTheme.typography.titleMedium)
        }
    }
}
