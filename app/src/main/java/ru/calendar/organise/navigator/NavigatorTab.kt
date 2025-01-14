package ru.calendar.organise.navigator

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import ru.calendar.organise.R

enum class NavigatorTab(
    @IdRes val idRes: Int,
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int,
) {
    HOME(
        idRes = R.id.tab_entry_home_id,
        nameRes = R.string.tab_entry_home_name,
        iconRes = R.drawable.ic_home,
    ),
    CALENDAR(
        idRes = R.id.tab_entry_calendar_id,
        nameRes = R.string.tab_entry_calendar_name,
        iconRes = R.drawable.ic_calendar
    ),
    CLOCK(
        idRes = R.id.tab_entry_clock_id,
        nameRes = R.string.tab_entry_clock_name,
        iconRes = R.drawable.ic_clock
    ),
    PROFILE(
        idRes = R.id.tab_entry_profile_id,
        nameRes = R.string.tab_entry_profile_name,
        iconRes = R.drawable.ic_profile
    )
}