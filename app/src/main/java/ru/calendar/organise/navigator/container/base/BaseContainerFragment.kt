package ru.calendar.organise.navigator.container.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.koin.android.ext.android.inject
import ru.calendar.navigator.Navigator
import ru.calendar.navigator.NavigatorScreen
import ru.calendar.organise.navigator.NavigatorRouter
import ru.calendar.organise.navigator.NavigatorTab
import ru.calendar.organise.navigator.container.NavigatorContainer
import ru.calendar.core.tools.Keys
import ru.calendar.organise.navigator.container.holder.NavigatorContainerHolder

abstract class BaseContainerFragment : Fragment(), NavigatorContainerHolder.Provider {

    private val navigatorTab: NavigatorTab
        get() {
            val tabItemEntryName =
                arguments?.getString(Keys.TAB_ITEM_KEY) ?: NavigatorTab.HOME.name
            return NavigatorTab.valueOf(tabItemEntryName)
        }

    private val containerId: Int
        get() = navigatorTab.idRes

    private val navigator: com.github.terrakok.cicerone.Navigator by lazy {
        AppNavigator(
            requireActivity(),
            containerId,
            childFragmentManager
        )
    }

    private val navigatorContainerHolder by inject<NavigatorContainerHolder>()

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val containerFragment = childFragmentManager.findFragmentById(containerId)
        val tabContainer = FragmentContainerView(requireContext())
        tabContainer.layoutParams = ViewGroup.LayoutParams(
            MATCH_PARENT,
            MATCH_PARENT
        )
        tabContainer.id = containerId
        if (containerFragment == null) {
            val fragment = getStartTabFragment(navigatorTab)
            getNavigatorRouter().router?.replaceScreen(fragment)
        }
        return tabContainer
    }

    override fun getNavigatorContainer(): NavigatorContainer {
        return navigatorContainerHolder.getContainer(navigatorTab)
    }

    override fun getNavigatorRouter(): NavigatorRouter {
        return NavigatorRouter(getNavigatorContainer().cicerone.router)
    }

    override fun getNavigatorScreen(): NavigatorScreen? {
        val lastNavigatorScreen = childFragmentManager.fragments.lastOrNull()
        return if (lastNavigatorScreen is Navigator.Provider) {
            lastNavigatorScreen.getNavigatorScreen()
        } else {
            null
        }
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        getNavigatorContainer().cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    @CallSuper
    override fun onPause() {
        getNavigatorContainer().cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    abstract fun getStartTabFragment(navigatorTab: NavigatorTab): FragmentScreen
}