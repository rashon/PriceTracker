package com.example.pricetracker.di

import com.example.pricetracker.presentation.detail.DetailVM
import com.example.pricetracker.presentation.feed.FeedVM
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {

    viewModelOf(::FeedVM)

    viewModelOf(::DetailVM)
}
