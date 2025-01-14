package ru.calendar.organise.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.calendar.navigator.NavigatorScreen

class MainViewModel : ViewModel() {

    private val _isVisibilityBottomNavFlow = MutableStateFlow<Boolean>(true)
    val isVisibilityBottomNavFlow = _isVisibilityBottomNavFlow.asStateFlow()

    fun onChangeVisibilityBottomNavigation(navigatorScreen: NavigatorScreen?) {
        _isVisibilityBottomNavFlow.value = true
    }
}