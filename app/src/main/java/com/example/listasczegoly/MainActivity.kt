package com.example.listasczegoly

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listasczegoly.ui.theme.ListaSczegolyTheme
import com.example.listasczegoly.viewmodel.ElementsViewModel
import kotlinx.coroutines.launch
import com.example.listasczegoly.ui.phone.PhoneLayout
import com.example.listasczegoly.ui.tablet.TabletLayout
import com.example.listasczegoly.ui.components.SplashScreen

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var viewModel: ElementsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            val vm: ElementsViewModel = viewModel()
            viewModel = vm

            ListaSczegolyTheme(darkTheme = vm.isDarkTheme) {
                if (vm.showSplash) {
                    SplashScreen(
                        tiltX = vm.tiltX,
                        tiltY = vm.tiltY,
                        onAnimationFinished = { vm.showSplash = false }
                    )
                } else {
                    MainView(vm)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            viewModel?.tiltX = -event.values[0] / 9.81f
            viewModel?.tiltY = event.values[1] / 9.81f
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.smallestScreenWidthDp >= 600
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MainView(viewModel: ElementsViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu Nawigacyjne", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Strona Główna") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text(if (viewModel.isDarkTheme) "Tryb Jasny" else "Tryb Ciemny") },
                    icon = { Icon(if (viewModel.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode, null) },
                    selected = false,
                    onClick = {
                        viewModel.toggleTheme()
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        BoxWithConstraints {
            if (isTablet()) {
                TabletLayout(viewModel, drawerState)
            } else {
                PhoneLayout(viewModel, drawerState)
            }
        }
    }
}
