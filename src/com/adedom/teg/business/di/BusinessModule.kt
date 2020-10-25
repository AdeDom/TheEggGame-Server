package com.adedom.teg.business.di

import com.adedom.teg.business.account.AccountService
import com.adedom.teg.business.account.AccountServiceImpl
import com.adedom.teg.business.application.ApplicationService
import com.adedom.teg.business.application.ApplicationServiceImpl
import com.adedom.teg.business.auth.AuthService
import com.adedom.teg.business.auth.AuthServiceImpl
import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.business.TegBusinessImpl
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.business.jwtconfig.JwtConfigImpl
import com.adedom.teg.business.multi.MultiService
import com.adedom.teg.business.multi.MultiServiceImpl
import com.adedom.teg.business.single.SingleService
import com.adedom.teg.business.single.SingleServiceImpl
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
    single<MultiService> { MultiServiceImpl(get(), get()) }

}

@KtorExperimentalLocationsAPI
val getBusinessModule = businessModule
