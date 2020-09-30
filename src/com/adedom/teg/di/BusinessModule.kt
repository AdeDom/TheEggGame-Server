package com.adedom.teg.di

import com.adedom.teg.service.account.AccountService
import com.adedom.teg.service.account.AccountServiceImpl
import com.adedom.teg.service.application.ApplicationService
import com.adedom.teg.service.application.ApplicationServiceImpl
import com.adedom.teg.service.auth.AuthService
import com.adedom.teg.service.auth.AuthServiceImpl
import com.adedom.teg.service.teg.TegService
import com.adedom.teg.service.teg.TegServiceImpl
import org.koin.dsl.module

private val businessModule = module {

    single<AuthService> { AuthServiceImpl(get()) }
    single<AccountService> { AccountServiceImpl(get()) }
    single<ApplicationService> { ApplicationServiceImpl(get()) }
    single<TegService> { TegServiceImpl(get()) }

}

val getBusinessModule = businessModule
