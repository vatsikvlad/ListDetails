package com.example.listasczegoly.ui.tablet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.listasczegoly.data.allTrasy
import com.example.listasczegoly.ui.components.*
import com.example.listasczegoly.viewmodel.ElementsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletLayout(viewModel: ElementsViewModel, drawerState: DrawerState) {
    val selected = viewModel.selected
    val isRower = viewModel.isRower
    val filteredTrasy = viewModel.getFilteredTrasy(allTrasy)
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var isSearchActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchTopBar(
                title = "Szlaki",
                searchQuery = viewModel.searchQuery,
                onSearchQueryChange = { viewModel.searchQuery = it },
                onMenuClick = { scope.launch { drawerState.open() } },
                onThemeToggle = { viewModel.toggleTheme() },
                isDarkTheme = viewModel.isDarkTheme,
                isSearchActive = isSearchActive,
                onSearchToggle = { isSearchActive = it }
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.heightIn(min = 80.dp)) {
                TextToggle(isRower = isRower, onChange = {
                    viewModel.isRower = it
                    viewModel.selected = null
                })
            }
        },
        floatingActionButton = {
            if (selected != null) {
                FloatingActionButton(
                    onClick = { viewModel.saveCurrentRecord() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Done, contentDescription = "Zapisz")
                }
            }
        }
    ) { innerPadding ->
        Row(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(16.dp)) {
                LazyColumn {
                    items(filteredTrasy) { trasa ->
                        ToggleButtonItem(
                            text = trasa.title,
                            isSelected = viewModel.selected == trasa
                        ) {
                            viewModel.selected = if (viewModel.selected == trasa) null else trasa
                        }
                    }
                }
            }

            Column(modifier = Modifier.weight(2f).fillMaxHeight().padding(16.dp).verticalScroll(scrollState)) {
                if (selected != null) {
                    Image(
                        painter = painterResource(id = selected.image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(400.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = selected.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = selected.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Dystans: ${selected.length} km",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    StopwatchDisplay(viewModel)
                    RecordsList(viewModel, selected.title)

                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Wybierz trasę z listy", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
        }
    }
}
