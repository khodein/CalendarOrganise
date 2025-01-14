package ru.calendar.navigator

sealed interface NavigatorScreen {
    data object CalendarScreen: NavigatorScreen
}