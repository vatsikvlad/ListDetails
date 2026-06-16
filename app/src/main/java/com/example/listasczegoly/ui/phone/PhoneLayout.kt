package com.example.listasczegoly.ui.phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listasczegoly.data.allTrasy
import com.example.listasczegoly.ui.components.RecordsList
import com.example.listasczegoly.ui.components.SearchTopBar
import com.example.listasczegoly.ui.components.StopwatchDisplay
import com.example.listasczegoly.ui.components.TextToggle
import com.example.listasczegoly.viewmodel.ElementsViewModel
import kotlinx.coroutines.launch

@Composable
fun PhoneLayout(viewModel: ElementsViewModel, drawerState: DrawerState) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Screen1(navController, viewModel, drawerState)
        }
        composable("details/{trasaIndex}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("trasaIndex")?.toInt() ?: 0
            Screen2(navController, index, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(navController: NavHostController, viewModel: ElementsViewModel, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    var isSearchActive by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = if (viewModel.isRower) 1 else 0)

    LaunchedEffect(pagerState.currentPage) {
        viewModel.isRower = pagerState.currentPage == 1
    }

    LaunchedEffect(viewModel.isRower) {
        pagerState.animateScrollToPage(if (viewModel.isRower) 1 else 0)
    }

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
            BottomAppBar {
                TextToggle(isRower = viewModel.isRower, onChange = { viewModel.isRower = it })
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) { page ->
            val isRowerPage = page == 1
            val pageFilteredTrasy = allTrasy.filter { trasa ->
                trasa.isRower == isRowerPage &&
                (trasa.title.contains(viewModel.searchQuery, ignoreCase = true) || 
                 trasa.description.contains(viewModel.searchQuery, ignoreCase = true))
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pageFilteredTrasy) { trasa ->
                    val originalIndex = allTrasy.indexOf(trasa)
                    Card(
                        modifier = Modifier.clickable {
                            viewModel.selected = trasa
                            navController.navigate("details/$originalIndex")
                        },
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column {
                            Image(
                                painter = painterResource(id = trasa.image),
                                contentDescription = trasa.title,
                                modifier = Modifier.height(150.dp).fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = trasa.title,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2(navController: NavHostController, trasaIndex: Int, viewModel: ElementsViewModel) {
    val localTrasa = allTrasy[trasaIndex]
    viewModel.selected = localTrasa
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text(localTrasa.title) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Powrót")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleTheme() }) {
                        Icon(if (viewModel.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode, null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.saveCurrentRecord() }) {
                Icon(Icons.Default.Done, contentDescription = "Zapisz")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(localTrasa.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
            
            Column(modifier = Modifier.padding(24.dp)) {
                Text(localTrasa.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Dystans: ${localTrasa.length} km",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                StopwatchDisplay(viewModel)
                RecordsList(viewModel, localTrasa.title)
            }
        }
    }
}
