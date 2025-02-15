package com.aralhub.indrive.core.data.di

import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import com.aralhub.indrive.core.data.repository.client.impl.ClientAuthRepositoryImpl
import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import com.aralhub.indrive.core.data.repository.driver.impl.DriverAuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsClientAuthRepository(
        topicsRepository: ClientAuthRepositoryImpl,
    ): ClientAuthRepository

    @Binds
    internal abstract fun bindsDriverAuthRepository(
        topicsRepository: DriverAuthRepositoryImpl,
    ): DriverAuthRepository


}
