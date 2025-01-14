package ru.calendar.organise.presentation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.observe
import ru.calendar.organise.presentation.base.BaseMainActivity
import ru.calendar.organise.navigator.NavigatorTab
import ru.calendar.organise.navigator.container.NavigatorContainerFragment
import ru.calendar.core.tools.Keys
import ru.calendar.core.tools.round.RoundModeEntity
import ru.calendar.core.tools.round.RoundValue

class MainActivity : BaseMainActivity() {

    override val viewModel by viewModel<MainViewModel>()

    override val bottomNavigationRound: RoundValue = RoundValue(
        mode = RoundModeEntity.TOP,
        radius = DimensionValue.Dp(12),
    )

    override fun onCreated(savedInstanceState: Bundle?) {

    }

    override fun setObservable() = with(viewModel) {
        isVisibilityBottomNavFlow.observe(this@MainActivity, ::onChangeVisibilityBottomNav)
    }

    private fun onChangeVisibilityBottomNav(isVisible: Boolean) {
        binding.mainBottomNav.isVisible = isVisible
    }

    override fun getNavigatorContainerFragment(navigatorTab: NavigatorTab) = FragmentScreen {
        NavigatorContainerFragment().apply {
            arguments = bundleOf(Keys.TAB_ITEM_KEY to navigatorTab.name)
        }
    }
}