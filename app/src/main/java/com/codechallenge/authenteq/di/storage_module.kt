package com.codechallenge.authenteq.di

import com.codechallenge.authenteq.storage.SharedPrefsManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val storageModule = module {
    single { SharedPrefsManager(androidContext()) }
}