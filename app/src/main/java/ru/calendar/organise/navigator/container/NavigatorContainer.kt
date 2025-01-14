package ru.calendar.organise.navigator.container

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

data class NavigatorContainer(
    val cicerone: Cicerone<Router>
)