package com.codechallenge.authenteq.di

import com.codechallenge.authenteq.ui.user.search.SearchUserViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { SearchUserViewModel(get(), get()) }
}
