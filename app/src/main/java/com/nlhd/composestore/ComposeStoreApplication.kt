package com.nlhd.composestore

import android.app.Application
import com.nlhd.composestore.di.appModule
import com.nlhd.data.di.dataModule
import com.nlhd.domain.di.domainModule
import com.nlhd.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ComposeStoreApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ComposeStoreApplication)
            modules(
                listOf(
                    networkModule,
                    dataModule,
                    domainModule,
                    appModule
                )
            )
        }
    }
}