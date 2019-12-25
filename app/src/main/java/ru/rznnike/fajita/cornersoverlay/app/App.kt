package ru.rznnike.fajita.cornersoverlay.app

import android.app.Application
import appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.rznnike.fajita.cornersoverlay.BuildConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG)
                androidLogger()

            androidContext(this@App)

            modules(appComponent)
        }
    }
}
