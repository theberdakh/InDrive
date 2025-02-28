package com.aralhub.network.di

import com.aralhub.network.WebSocketDriverNetworkDataSource
import com.aralhub.network.impl.WebSocketDriverNetworkDataSourceImpl
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
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            defaultRequest {
                header(
                    "Authorization",
                    "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXBlIjoiYWNjZXNzIiwic3ViIjoiNiIsInR5cGVfdXNlciI6ImRyaXZlciIsImV4cCI6MTc0Nzg0NDU5NSwiaWF0IjoxNzQwNjQ0NTk1LCJqdGkiOiJlNGNkOTUyNy1hOTU4LTQwMjQtYWEwOC1mMjE4NWM3ZWE1MDYifQ.eE_2ZujjSA4UUAcPHt_2DLsuU6zPvuu6Byrsq5lqTOPOFS2KBVMMskkv39BJy7kyC66M2rBqNWd9dNHdySPZAQ7smorx0Gw8mMUgN7ExJwBhCY4kqTaDqHUx8wSSiL0QPhCl_z6Z2p1iP7SrpeZLTpJP8rztZZASPNKKRAG3-Mi19_QSbP5Pea7gk50LUvXXBh-gjMhb4BzjdYZuhFiokX3xl1b46CcDWOXnD_A0-AxS4eP4fL3f55CdqAnMT1PzB-b3kpBpUFWQZNXwESMTn3jueGybJ0meYS82PFoOJ4bbIoNCTXJbZaeLtxDozKysQNFFfX0NpMTeb0zhlhgu-w"
                )
            }
        }
    }

    @Singleton
    @Provides
    fun provideRealtimeMessagingClient(httpClient: HttpClient): WebSocketDriverNetworkDataSource {
        return WebSocketDriverNetworkDataSourceImpl(httpClient)
    }
}