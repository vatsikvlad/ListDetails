package com.example.listasczegoly.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.listasczegoly.data.AppDatabase
import com.example.listasczegoly.data.Trasa
import com.example.listasczegoly.data.allTrasy
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RouteRecord(
    val time: Long,
    val date: String
)

class ElementsViewModel(application: Application) : AndroidViewModel(application) {
    private val trasaDao = AppDatabase.getDatabase(application).trasaDao()

    var isRower by mutableStateOf(true)
    var selected by mutableStateOf<Trasa?>(null)
    var isDarkTheme by mutableStateOf(false)
    var searchQuery by mutableStateOf("")
    private val _searchQueryFlow = MutableStateFlow("")
    var tiltX by mutableFloatStateOf(0f)
    var tiltY by mutableFloatStateOf(0f)
    var timeElapsed by mutableLongStateOf(0L)
    var isRunning by mutableStateOf(false)
    private var timerJob: Job? = null
    var showSplash by mutableStateOf(true)

    var savedRecords by mutableStateOf(mapOf<String, List<RouteRecord>>())
        private set

    init {
        viewModelScope.launch {
            if (trasaDao.getCount() == 0) {
                trasaDao.insertAll(allTrasy)
            }
        }
    }

    fun toggleTheme() { isDarkTheme = !isDarkTheme }

    fun startTimer() {
        if (isRunning) return
        isRunning = true
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                timeElapsed++
            }
        }
    }

    fun stopTimer() {
        isRunning = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        stopTimer()
        timeElapsed = 0L
    }

    fun saveCurrentRecord() {
        val currentTrasa = selected ?: return
        if (timeElapsed <= 0) return

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val newRecord = RouteRecord(timeElapsed, currentDate)
        val currentList = savedRecords[currentTrasa.title] ?: emptyList()
        savedRecords = savedRecords + (currentTrasa.title to (currentList + newRecord))
        resetTimer()
    }

    fun deleteRecord(routeTitle: String, record: RouteRecord) {
        val currentList = savedRecords[routeTitle] ?: return
        val newList = currentList.filter { it != record }
        savedRecords = if (newList.isEmpty()) {
            savedRecords - routeTitle
        } else {
            savedRecords + (routeTitle to newList)
        }
    }

    fun getFilteredTrasy(allTrasy: List<Trasa>): List<Trasa> {
        return allTrasy.filter { trasa ->
            trasa.isRower == isRower &&
            (trasa.title.contains(searchQuery, ignoreCase = true) || 
             trasa.description.contains(searchQuery, ignoreCase = true))
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
