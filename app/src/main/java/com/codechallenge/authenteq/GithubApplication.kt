package com.codechallenge.authenteq

import android.app.Application
import com.codechallenge.authenteq.di.appComponent
import org.koin.android.ext.android.startKoin

open class GithubApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    // CONFIGURATION ---
    open fun configureDi() =
        startKoin(this, provideComponent())

    // PUBLIC API ---
    open fun provideComponent() = appComponent
}