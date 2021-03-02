package com.adedom.teg.data.di

import com.adedom.teg.data.map.Mapper
import com.adedom.teg.data.map.MapperImpl
import com.adedom.teg.data.repositories.ReportRepository
import com.adedom.teg.data.repositories.ReportRepositoryImpl
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.data.repositories.TegRepositoryImpl
import io.ktor.locations.*
import org.koin.dsl.module

@KtorExperimentalLocationsAPI
private val dataModule = module {

    single<Mapper> { MapperImpl() }

    single<TegRepository> { TegRepositoryImpl() }
    single<ReportRepository> { ReportRepositoryImpl(get()) }

}

@KtorExperimentalLocationsAPI
val getDataModule = dataModule
