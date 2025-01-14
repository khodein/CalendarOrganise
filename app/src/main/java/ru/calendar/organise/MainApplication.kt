package ru.calendar.organise

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import ru.calendar.feature.calendar.calendarModule
import ru.calendar.feature.clock.clockModule
import ru.calendar.feature.home.homeModule
import ru.calendar.feature.profile.profileModule
import ru.calendar.organise.di.appModule
import ru.calendar.organise.di.mainModule
import ru.calendar.organise.di.navigatorModule

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            fragmentFactory()
            androidContext(this@MainApplication)
            modules(
                appModule,
                navigatorModule,
                mainModule,
                homeModule,
                calendarModule,
                clockModule,
                profileModule,
            )
        }
    }
}