package com.aralhub.network.di

import android.content.Context
import com.aralhub.network.api.AddressNetworkApi
import com.aralhub.network.api.CancelCauseNetworkApi
import com.aralhub.network.api.DriverNetworkApi
import com.aralhub.network.api.PaymentMethodsNetworkApi
import com.aralhub.network.api.RideOptionNetworkApi
import com.aralhub.network.api.UserNetworkApi
import com.aralhub.network.api.WebSocketClientNetworkApi
import com.aralhub.network.utils.AuthInterceptor
import com.aralhub.network.utils.NetworkErrorInterceptor
import com.aralhub.network.utils.TokenAuthenticator
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @[Provides Singleton]
    fun provideChucker(
        @ApplicationContext context: Context,
    ): ChuckerInterceptor = ChuckerInterceptor.Builder(context).build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @[Provides Singleton]
    fun provideMainOkHttpClient(
        chucker: ChuckerInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        networkErrorInterceptor: NetworkErrorInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(chucker)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(networkErrorInterceptor)
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .connectTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @[Provides Singleton]
    fun provideMainRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://araltaxi.aralhub.uz/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @[Provides Singleton]
    fun provideUserNetworkApi(retrofit: Retrofit): UserNetworkApi =
        retrofit.create(UserNetworkApi::class.java)

    @[Provides Singleton]
    fun provideDriverNetworkApi(retrofit: Retrofit): DriverNetworkApi =
        retrofit.create(DriverNetworkApi::class.java)

    @[Provides Singleton]
    fun provideWebsocketClientNetworkApi(retrofit: Retrofit): WebSocketClientNetworkApi =
        retrofit.create(WebSocketClientNetworkApi::class.java)

    @[Provides Singleton]
    fun providePaymentMethodNetworkApi(retrofit: Retrofit): PaymentMethodsNetworkApi =
        retrofit.create(PaymentMethodsNetworkApi::class.java)

    @[Provides Singleton]
    fun provideRideOptionNetworkApi(retrofit: Retrofit): RideOptionNetworkApi =
        retrofit.create(RideOptionNetworkApi::class.java)

    @[Provides Singleton]
    fun provideCancelCauseNetworkApi(retrofit: Retrofit): CancelCauseNetworkApi =
        retrofit.create(CancelCauseNetworkApi::class.java)

    @[Provides Singleton]
    fun provideAddressNetworkApi(retrofit: Retrofit): AddressNetworkApi =
        retrofit.create(AddressNetworkApi::class.java)

}