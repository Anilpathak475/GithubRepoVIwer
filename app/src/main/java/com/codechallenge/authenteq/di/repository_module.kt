package com.codechallenge.authenteq.di

import com.codechallenge.authenteq.repository.UserRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    factory { UserRepository(get()) }
}