package com.adedom.teg.di

import com.adedom.teg.repositories.AuthRepository
import com.adedom.teg.service.AuthService
import com.adedom.teg.service.AuthServiceImpl
import org.koin.dsl.module

val authAppModule = module {
    single<AuthService> { AuthServiceImpl(get()) }
    single { AuthRepository() }
}
