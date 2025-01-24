package ru.calendar.feature.calendar.presentation

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.flow.filterNotNull
import ru.calendar.core.base.BaseFragment
import ru.calendar.feature.calendar.databinding.FragmentCalendarBinding
import ru.calendar.navigator.NavigatorScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.calendar.core.tools.ext.observe
import ru.calendar.feature.calendar.R

class CalendarFragment : BaseFragment<FragmentCalendarBinding, NavigatorScreen.CalendarScreen>(
    contentLayoutId = R.layout.fragment_calendar,
    navigatorScreen = NavigatorScreen.CalendarScreen,
    vbInflate = FragmentCalendarBinding::inflate
) {
    override val viewModel by viewModel<CalendarViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservable()
    }

    private fun setObservable() = with(viewModel) {
        headerCalendarFlow.filterNotNull()
            .observe(viewLifecycleOwner, binding.calendarHeader::bindState)

        calendarFlow.filterNotNull()
            .observe(viewLifecycleOwner, binding.calendarItem::bindState)
    }
}