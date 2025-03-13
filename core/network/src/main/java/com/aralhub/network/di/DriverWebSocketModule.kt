package com.aralhub.network.di

import android.util.Log
import com.aralhub.network.ClientRideNetworkDataSource
import com.aralhub.network.WebSocketClientOffersNetworkDataSource
import com.aralhub.network.WebSocketDriverNetworkDataSource
import com.aralhub.network.impl.ClientRideNetworkDataSourceImpl
import com.aralhub.network.impl.WebSocketClientOffersNetworkDataSourceImpl
import com.aralhub.network.impl.WebSocketDriverNetworkDataSourceImpl
import com.aralhub.network.local.LocalStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DriverWebSocketModule {

    @Singleton
    @Provides
    fun provideHttpClient(localStorage: LocalStorage): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets) {
                pingInterval = 20_000
            }
            defaultRequest {
                header(
                    "Authorization",
                    "Bearer ${localStorage.access}"
                )
            }
        }
    }


    @Singleton
    @Provides
    fun provideRealtimeMessagingClient(httpClient: HttpClient): WebSocketDriverNetworkDataSource {
        return WebSocketDriverNetworkDataSourceImpl(httpClient)
    }

    @Singleton
    @Provides
    fun provideClientOffersNetworkDataSource(httpClient: HttpClient): WebSocketClientOffersNetworkDataSource {
        return WebSocketClientOffersNetworkDataSourceImpl(httpClient)
    }

    @Singleton
    @Provides
    fun provideClientRideNetworkDataSource(httpClient: HttpClient): ClientRideNetworkDataSource {
        return ClientRideNetworkDataSourceImpl(httpClient)
    }
}