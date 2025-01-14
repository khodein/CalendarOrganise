package ru.calendar.feature.calendar

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.calendar.feature.calendar.mapper.CalendarMapper
import ru.calendar.feature.calendar.mapper.CalendarMapperImpl
import ru.calendar.feature.calendar.presentation.CalendarViewModel

val calendarModule = module {
    singleOf<CalendarMapper>(::CalendarMapperImpl)
    viewModelOf(::CalendarViewModel)
}