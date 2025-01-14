package ru.calendar.organise.navigator.holder

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.calendar.organise.navigator.container.holder.NavigatorContainerHolder

interface NavigatorHolder {

    fun initProvider(provider: Provider)
    fun removeProvider()
    fun pop()
    fun navigate(screen: FragmentScreen)

    interface Provider : NavigatorContainerHolder.Provider {
        fun onResetHandleBackPressedOnce()
        fun onChangeScreen()
    }
}