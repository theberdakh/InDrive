package com.aralhub.indrive.core.data.di

import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import com.aralhub.indrive.core.data.repository.client.impl.ClientAuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsTopicRepository(
        topicsRepository: ClientAuthRepositoryImpl,
    ): ClientAuthRepository

}
