package ru.calendar.organise.navigator.container.holder

import com.github.terrakok.cicerone.Cicerone.Companion.create
import ru.calendar.organise.navigator.NavigatorTab
import ru.calendar.organise.navigator.container.NavigatorContainer
import java.util.concurrent.ConcurrentHashMap

class NavigatorContainerHolderImpl : NavigatorContainerHolder {

    private val containers = ConcurrentHashMap<NavigatorTab, NavigatorContainer>()

    override fun getContainer(tabItemEntry: NavigatorTab): NavigatorContainer {
        val container = containers.getOrPut(tabItemEntry) { NavigatorContainer(create()) }
        return container
    }
}