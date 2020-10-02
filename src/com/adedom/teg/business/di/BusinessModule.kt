package com.adedom.teg.business.di

import com.adedom.teg.business.service.account.AccountService
import com.adedom.teg.business.service.account.AccountServiceImpl
import com.adedom.teg.business.service.application.ApplicationService
import com.adedom.teg.business.service.application.ApplicationServiceImpl
import com.adedom.teg.business.service.auth.AuthService
import com.adedom.teg.business.service.auth.AuthServiceImpl
import com.adedom.teg.business.jwtconfig.JwtConfigImpl
import com.adedom.teg.business.service.single.SingleService
import com.adedom.teg.business.service.single.SingleServiceImpl
import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.business.TegBusinessImpl
import com.adedom.teg.business.jwtconfig.JwtConfig
import io.ktor.locations.*
import org.koin.dsl.module

@KtorExperimentalLocationsAPI
private val businessModule = module {

    // jwt
    single<JwtConfig> { JwtConfigImpl() }

    // logic
    single<TegBusiness> { TegBusinessImpl() }

    // service
    single<AuthService> { AuthServiceImpl(get(), get(), get()) }
    single<AccountService> { AccountServiceImpl(get(), get()) }
    single<ApplicationService> { ApplicationServiceImpl(get(), get()) }
    single<SingleService> { SingleServiceImpl(get(), get()) }

}

@KtorExperimentalLocationsAPI
val getBusinessModule = businessModule
