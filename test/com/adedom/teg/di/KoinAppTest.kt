package com.adedom.teg.di

import com.adedom.teg.business.di.getBusinessModule
import com.adedom.teg.data.di.getDataModule
import io.ktor.locations.*
import org.junit.Test
import org.junit.experimental.categories.Category
import org.koin.test.AutoCloseKoinTest
import org.koin.test.category.CheckModuleTest
import org.koin.test.check.checkModules

@KtorExperimentalLocationsAPI
@Category(CheckModuleTest::class)
class KoinAppTest : AutoCloseKoinTest() {

    @Test
    fun koin_checkModules() {
        checkModules {
            modules(getDataModule, getBusinessModule)
        }
    }

}
