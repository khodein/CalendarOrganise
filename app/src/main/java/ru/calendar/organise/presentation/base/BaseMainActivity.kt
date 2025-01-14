package ru.calendar.organise.presentation.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import ru.calendar.core.tools.ext.addItem
import ru.calendar.core.tools.ext.hideKeyboard
import ru.calendar.core.tools.ext.makeRound
import ru.calendar.core.tools.round.RoundValue
import ru.calendar.navigator.NavigatorScreen
import ru.calendar.organise.R
import ru.calendar.organise.databinding.ActivityMainBinding
import ru.calendar.organise.navigator.NavigatorRouter
import ru.calendar.organise.navigator.NavigatorTab
import ru.calendar.organise.navigator.container.NavigatorContainer
import ru.calendar.organise.navigator.container.holder.NavigatorContainerHolder
import ru.calendar.organise.navigator.holder.NavigatorHolder
import ru.calendar.organise.presentation.MainViewModel

abstract class BaseMainActivity : AppCompatActivity(), NavigatorHolder.Provider {

    abstract val viewModel: MainViewModel

    private var _binding: ActivityMainBinding? = null
    protected val binding: ActivityMainBinding get() = _binding!!

    abstract val bottomNavigationRound: RoundValue

    private var onBackStackChangedListener: FragmentManager.OnBackStackChangedListener? = null

    private var isHandleOnBackOnce: Boolean = false

    private val navigatorHolder by inject<NavigatorHolder>()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val container = getVisibleFragmentContainer()
            val backStackEntry = container?.childFragmentManager?.backStackEntryCount ?: 0
            when {
                backStackEntry >= 1 -> {
                    navigatorHolder.pop()
                }

                !isHandleOnBackOnce -> {
                    isHandleOnBackOnce = true
                }

                isHandleOnBackOnce -> {
                    navigatorHolder.pop()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        setBottomNavigationMenu()

        setObservable()

        if (savedInstanceState == null) {
            binding.mainBottomNav.selectedItemId = NavigatorTab.HOME.idRes
        }

        onCreated(savedInstanceState = savedInstanceState)
    }

    abstract fun setObservable()
    abstract fun onCreated(savedInstanceState: Bundle?)

    @CallSuper
    override fun onResume() {
        super.onResume()
        onResetHandleBackPressedOnce()
        navigatorHolder.initProvider(this)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    @CallSuper
    override fun onPause() {
        navigatorHolder.removeProvider()
        onBackPressedCallback.remove()
        super.onPause()
    }

    override fun onResetHandleBackPressedOnce() {
        isHandleOnBackOnce = false
    }

    override fun getNavigatorContainer(): NavigatorContainer? {
        val containerProvider = getVisibleFragmentContainer()
        return if (containerProvider is NavigatorContainerHolder.Provider) {
            containerProvider.getNavigatorContainer()
        } else {
            null
        }
    }

    override fun getNavigatorRouter(): NavigatorRouter? {
        val containerProvider = getVisibleFragmentContainer()
        return if (containerProvider is NavigatorContainerHolder.Provider) {
            containerProvider.getNavigatorRouter()
        } else {
            null
        }
    }

    override fun getNavigatorScreen(): NavigatorScreen? {
        val containerProvider = getVisibleFragmentContainer()
        return if (containerProvider is NavigatorContainerHolder.Provider) {
            containerProvider.getNavigatorScreen()
        } else {
            null
        }
    }

    private fun getVisibleFragmentContainer(): Fragment? {
        val fragments = supportFragmentManager.fragments
        return fragments.find { fragment ->
            fragment.isVisible && fragment is NavigatorContainerHolder.Provider
        }
    }

    private fun setBottomNavigationMenu() = with(binding.mainBottomNav) {
        mapMenu(NavigatorTab.entries)
        makeRound(bottomNavigationRound)
        setOnItemSelectedListener(::onItemSelected)
    }

    private fun onItemSelected(
        menuItem: MenuItem
    ): Boolean {
        onResetHandleBackPressedOnce()
        return onTabSelected(NavigatorTab.entries.find { it.idRes == menuItem.itemId })
    }

    private fun onTabSelected(
        navigatorTab: NavigatorTab?
    ): Boolean {
        if (navigatorTab == null) {
            return false
        }

        val currentFragment = getVisibleFragmentContainer()
        val newFragment = supportFragmentManager.findFragmentByTag(navigatorTab.name)

        if (currentFragment != null && newFragment != null && currentFragment === newFragment) {
            return false
        }

        val transaction = supportFragmentManager.beginTransaction()
        if (newFragment == null) {
            val container = getNavigatorContainerFragment(navigatorTab)
            transaction.add(
                R.id.mainContainer,
                container.createFragment(supportFragmentManager.fragmentFactory),
                navigatorTab.name
            )
        }

        if (currentFragment != null) {
            currentFragment.hideKeyboard()
            transaction.hide(currentFragment)
        }

        if (newFragment != null) {
            transaction.show(newFragment)
        }

        transaction.commit()
        return true
    }

    abstract fun getNavigatorContainerFragment(navigatorTab: NavigatorTab): FragmentScreen

    override fun onChangeScreen() {
        onBackStackChangedListener = null
        val container = getVisibleFragmentContainer()
        val onBackStackChangedListener = FragmentManager.OnBackStackChangedListener {
            if (container is NavigatorContainerHolder.Provider) {
                viewModel.onChangeVisibilityBottomNavigation(container.getNavigatorScreen())
            }
            onBackStackChangedListener?.let { listener ->
                container?.childFragmentManager?.removeOnBackStackChangedListener(listener)
            }
            onBackStackChangedListener = null
        }
        this.onBackStackChangedListener = onBackStackChangedListener
        container?.childFragmentManager?.addOnBackStackChangedListener(onBackStackChangedListener)
    }

    private companion object {
        fun BottomNavigationView.mapMenu(entries: List<NavigatorTab>) {
            val groupId = R.id.main_bottom_navigation_group
            entries.forEach { entry ->
                menu.addItem(
                    groupId = groupId,
                    idRes = entry.idRes,
                    nameRes = entry.nameRes,
                    iconRes = entry.iconRes
                )
            }
        }
    }
}