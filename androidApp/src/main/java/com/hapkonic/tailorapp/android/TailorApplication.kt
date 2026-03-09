package com.hapkonic.tailorapp.android

import android.app.Application
import com.hapkonic.tailorapp.di.appModule
import com.hapkonic.tailorapp.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TailorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TailorApplication)
            modules(platformModule, appModule)
        }
    }
}
