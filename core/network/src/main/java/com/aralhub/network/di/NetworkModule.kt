package com.aralhub.network.di

import com.aralhub.network.api.DriverNetworkApi
import com.aralhub.network.api.PaymentMethodsNetworkApi
import com.aralhub.network.api.UserNetworkApi
import com.aralhub.network.api.WebSocketClientNetworkApi
import com.aralhub.network.utils.AuthInterceptor
import com.aralhub.network.utils.NetworkErrorInterceptor
import com.aralhub.network.utils.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @[Provides Singleton]
    fun provideMainOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        networkErrorInterceptor: NetworkErrorInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(networkErrorInterceptor)
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
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

}