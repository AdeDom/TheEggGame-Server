package com.adedom.teg.di

import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.repositories.TegRepositoryImpl
import com.adedom.teg.service.TegService
import com.adedom.teg.service.TegServiceImpl
import org.koin.dsl.module

val tegAppModule = module {
    single<TegService> { TegServiceImpl(get()) }
    single<TegRepository> { TegRepositoryImpl() }
}
