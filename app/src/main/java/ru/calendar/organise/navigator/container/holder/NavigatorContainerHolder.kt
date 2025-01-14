package ru.calendar.organise.navigator.container.holder

import ru.calendar.navigator.Navigator
import ru.calendar.organise.navigator.NavigatorRouter
import ru.calendar.organise.navigator.NavigatorTab
import ru.calendar.organise.navigator.container.NavigatorContainer

interface NavigatorContainerHolder {

    fun getContainer(tabItemEntry: NavigatorTab): NavigatorContainer

    interface Provider : Navigator.Provider {
        fun getNavigatorContainer(): NavigatorContainer?
        fun getNavigatorRouter(): NavigatorRouter?
    }
}