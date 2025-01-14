package ru.calendar.organise.navigator.holder

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.calendar.organise.navigator.NavigatorRouter
import ru.calendar.organise.navigator.container.NavigatorContainer
import ru.calendar.organise.navigator.container.holder.NavigatorContainerHolder

class NavigatorHolderImpl : NavigatorHolder {

    private var provider: NavigatorHolder.Provider? = null

    override fun initProvider(provider: NavigatorHolder.Provider) {
        this.provider = provider
    }

    override fun removeProvider() {
        this.provider = null
    }

    private fun getNavigatorRouter(): NavigatorRouter {
        return NavigatorRouter(getNavigatorContainer()?.cicerone?.router)
    }

    private fun getNavigatorContainer(): NavigatorContainer? {
        return getContainerProvider()?.getNavigatorContainer()
    }

    override fun pop() {
        provider?.onChangeScreen()
        getNavigatorRouter().router?.exit()
    }

    override fun navigate(screen: FragmentScreen) {
        onResetHandleBackPressedOnce()
        provider?.onChangeScreen()
        getNavigatorRouter().router?.navigateTo(screen)
    }

    private fun onResetHandleBackPressedOnce() {
        provider?.onResetHandleBackPressedOnce()
    }

    private fun getContainerProvider(): NavigatorContainerHolder.Provider? {
        val provider = this.provider
        return if (provider is NavigatorContainerHolder.Provider) {
            provider
        } else {
            null
        }
    }
}