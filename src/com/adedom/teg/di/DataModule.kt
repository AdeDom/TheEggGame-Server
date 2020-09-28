package com.adedom.teg.di

import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.repositories.TegRepositoryImpl
import org.koin.dsl.module

private val dataModule = module {

    single<TegRepository> { TegRepositoryImpl() }

}

val getDataModule = dataModule
