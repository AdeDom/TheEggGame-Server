package com.adedom.teg.di

import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.repositories.TegRepositoryImpl
import io.ktor.locations.*
import org.koin.dsl.module

@KtorExperimentalLocationsAPI
private val dataModule = module {

    single<TegRepository> { TegRepositoryImpl() }

}

@KtorExperimentalLocationsAPI
val getDataModule = dataModule
