package com.aralhub.indrive.core.data.di

import com.aralhub.indrive.core.data.repository.address.AddressRepository
import com.aralhub.indrive.core.data.repository.address.AddressRepositoryImpl
import com.aralhub.indrive.core.data.repository.cancel.CancelRepository
import com.aralhub.indrive.core.data.repository.cancel.impl.CancelRepositoryImpl
import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import com.aralhub.indrive.core.data.repository.client.ClientWebSocketRepository
import com.aralhub.indrive.core.data.repository.client.impl.ClientAuthRepositoryImpl
import com.aralhub.indrive.core.data.repository.client.impl.ClientWebSocketRepositoryImpl
import com.aralhub.indrive.core.data.repository.driver.DriverAuthRepository
import com.aralhub.indrive.core.data.repository.driver.DriverWebSocketRepository
import com.aralhub.indrive.core.data.repository.driver.impl.DriverAuthRepositoryImpl
import com.aralhub.indrive.core.data.repository.driver.impl.DriverWebSocketRepositoryImpl
import com.aralhub.indrive.core.data.repository.payment.PaymentRepository
import com.aralhub.indrive.core.data.repository.payment.impl.PaymentRepositoryImpl
import com.aralhub.indrive.core.data.repository.rideoption.RideOptionRepository
import com.aralhub.indrive.core.data.repository.rideoption.impl.RideOptionRepositoryImpl
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

    @Binds
    internal abstract fun bindsClientWebSocketRepository(
        topicsRepository: ClientWebSocketRepositoryImpl,
    ): ClientWebSocketRepository

    @Binds
    internal abstract fun bindsPaymentRepository(
        topicsRepository: PaymentRepositoryImpl,
    ): PaymentRepository

    @Binds
    internal abstract fun bindsRideOptionRepository(
        topicsRepository: RideOptionRepositoryImpl,
    ): RideOptionRepository

    @Binds
    internal abstract fun bindsCancelRepository(
        topicsRepository: CancelRepositoryImpl,
    ): CancelRepository

    @Binds
    internal abstract fun bindsAddressRepository(
        topicsRepository: AddressRepositoryImpl,
    ): AddressRepository

    @Binds
    internal abstract fun bindsDriverWebSocketRepository(
        topicsRepository: DriverWebSocketRepositoryImpl,
    ): DriverWebSocketRepository
}
