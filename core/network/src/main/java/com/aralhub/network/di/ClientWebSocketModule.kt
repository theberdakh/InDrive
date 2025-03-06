package com.aralhub.network.di

import com.aralhub.network.ClientOffersNetworkDataSource
import com.aralhub.network.WebSocketClientNetworkDataSource
import com.aralhub.network.WebSocketDriverNetworkDataSource
import com.aralhub.network.impl.ClientOffersNetworkDataSourceImpl
import com.aralhub.network.impl.WebSocketClientNetworkDataSourceImpl
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
object ClientWebSocketModule {

    @Singleton
    @Provides
    fun provideHttpClient(localStorage: LocalStorage): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
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
    fun provideRealtimeMessagingClient(httpClient: HttpClient): ClientOffersNetworkDataSource {
        return ClientOffersNetworkDataSourceImpl(httpClient)
    }
}