package com.aralhub.araltaxi.di

import com.aralhub.client.client_auth.navigation.FeatureClientAuthNavigation
import com.aralhub.araltaxi.navigation.Navigator
import com.aralhub.araltaxi.navigation.NavigatorImpl
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.offers.navigation.FeatureOffersNavigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    abstract fun bindFeatureAuthNavigation(navigatorImpl: NavigatorImpl): FeatureClientAuthNavigation

    @Binds
    abstract fun bindFeatureRequestNavigation(navigatorImpl: NavigatorImpl): FeatureRequestNavigation

    @Binds
    abstract fun bindFeatureOffersNavigation(navigatorImpl: NavigatorImpl): FeatureOffersNavigation

    @Binds
    abstract fun bindNavigator(navigatorImpl: NavigatorImpl): Navigator
}