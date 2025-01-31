package ru.calendar.feature.calendar.presentation

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.flow.filterNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.calendar.core.base.BaseFragment
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.observe
import ru.calendar.feature.calendar.R
import ru.calendar.feature.calendar.databinding.FragmentCalendarBinding
import ru.calendar.feature.calendar.ui.date_carousel.DateCarouselPickerItem
import ru.calendar.feature.calendar.ui.date_carousel.DateCarouselPickerItemView
import ru.calendar.navigator.NavigatorScreen

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

        showAlertChangeDateFlow.filterNotNull()
            .observe(viewLifecycleOwner, ::showAlertChangeDate)
    }

    private fun showAlertChangeDate(state: DateCarouselPickerItem.State) {
        val dateCarouselPickerView = DateCarouselPickerItemView(requireContext()).apply {
            bindState(state)
        }
        val builder = AlertDialog.Builder(requireContext())
            .setView(dateCarouselPickerView)

        val dialog = builder.create()

        dialog.window?.setLayout(
            DimensionValue.Dp(200).value,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.show()
    }
}