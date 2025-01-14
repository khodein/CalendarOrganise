package ru.calendar.organise.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.calendar.navigator.Navigator
import ru.calendar.organise.navigator.holder.NavigatorHolderImpl
import ru.calendar.organise.navigator.NavigatorImpl
import ru.calendar.organise.navigator.container.holder.NavigatorContainerHolder
import ru.calendar.organise.navigator.container.holder.NavigatorContainerHolderImpl
import ru.calendar.organise.navigator.holder.NavigatorHolder
import ru.calendar.organise.presentation.MainViewModel

val appModule = module {

}

val navigatorModule = module {
    single { NavigatorHolderImpl() } bind NavigatorHolder::class
    single { NavigatorContainerHolderImpl() } bind NavigatorContainerHolder::class
    single { NavigatorImpl() } bind Navigator::class
}

val mainModule = module {
    viewModelOf(::MainViewModel)
}