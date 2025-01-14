package ru.calendar.organise.navigator.container

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.calendar.organise.navigator.container.base.BaseContainerFragment
import ru.calendar.organise.navigator.NavigatorTab
import ru.calendar.core.tools.Keys
import ru.calendar.feature.calendar.presentation.CalendarFragment
import ru.calendar.feature.clock.ClockFragment
import ru.calendar.feature.home.HomeFragment
import ru.calendar.feature.profile.ProfileFragment

class NavigatorContainerFragment : BaseContainerFragment() {

    override fun getStartTabFragment(navigatorTab: NavigatorTab): FragmentScreen {
        return FragmentScreen(key = Keys.TAB_ITEM_KEY) {
            when(navigatorTab) {
                NavigatorTab.HOME -> HomeFragment()
                NavigatorTab.CALENDAR -> CalendarFragment()
                NavigatorTab.CLOCK -> ClockFragment()
                NavigatorTab.PROFILE -> ProfileFragment()
            }
        }
    }
}