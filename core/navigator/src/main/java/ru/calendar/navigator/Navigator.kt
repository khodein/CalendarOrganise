package ru.calendar.navigator

interface Navigator {

    interface Provider {
        fun getNavigatorScreen(): NavigatorScreen?
    }
}