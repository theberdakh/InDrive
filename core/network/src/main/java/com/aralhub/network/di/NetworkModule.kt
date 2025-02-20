package com.aralhub.network.di

import com.aralhub.network.api.DriverNetworkApi
import com.aralhub.network.api.UserNetworkApi
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

//    @[Provides Singleton]
//    fun provideDriverOkHttpClient(
//        httpLoggingInterceptor: HttpLoggingInterceptor,
//        tokenManager: TokenManager
//    ): OkHttpClient = OkHttpClient.Builder()
//        .addInterceptor(AuthInterceptor(tokenManager))
//        .addInterceptor(httpLoggingInterceptor)
//        .build()

    @[Provides Singleton]
    fun provideMainOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
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

}