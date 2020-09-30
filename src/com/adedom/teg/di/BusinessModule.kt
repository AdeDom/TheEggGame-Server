package com.adedom.teg.di

import com.adedom.teg.service.account.AccountService
import com.adedom.teg.service.account.AccountServiceImpl
import com.adedom.teg.service.application.ApplicationService
import com.adedom.teg.service.application.ApplicationServiceImpl
import com.adedom.teg.service.auth.AuthService
import com.adedom.teg.service.auth.AuthServiceImpl
import com.adedom.teg.service.single.SingleService
import com.adedom.teg.service.single.SingleServiceImpl
import io.ktor.locations.*
import org.koin.dsl.module

@KtorExperimentalLocationsAPI
private val businessModule = module {

    single<AuthService> { AuthServiceImpl(get()) }
    single<AccountService> { AccountServiceImpl(get()) }
    single<ApplicationService> { ApplicationServiceImpl(get()) }
    single<SingleService> { SingleServiceImpl(get()) }

}

@KtorExperimentalLocationsAPI
val getBusinessModule = businessModule
