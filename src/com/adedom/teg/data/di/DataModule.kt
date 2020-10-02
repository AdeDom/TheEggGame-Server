package com.adedom.teg.data.di

import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.data.repositories.TegRepositoryImpl
import io.ktor.locations.*
import org.koin.dsl.module

@KtorExperimentalLocationsAPI
private val dataModule = module {

    single<TegRepository> { TegRepositoryImpl() }

}

@KtorExperimentalLocationsAPI
val getDataModule = dataModule
