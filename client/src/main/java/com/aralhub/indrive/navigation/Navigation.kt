package com.aralhub.indrive.navigation

import com.aralhub.auth.navigation.FeatureAuthNavigation
import com.aralhub.indrive.request.navigation.FeatureRequestNavigation
import com.aralhub.indrive.waiting.navigation.FeatureWaitingNavigation
import com.aralhub.offers.navigation.FeatureOffersNavigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class Navigation {

    @Binds
    abstract fun bindFeatureAuthNavigation(navigatorImpl: NavigatorImpl): FeatureAuthNavigation

    @Binds
    abstract fun bindFeatureRequestNavigation(navigatorImpl: NavigatorImpl): FeatureRequestNavigation

    @Binds
    abstract fun bindFeatureOffersNavigation(navigatorImpl: NavigatorImpl): FeatureOffersNavigation

    @Binds
    abstract fun bindFeatureWaitingNavigation(navigatorImpl: NavigatorImpl): FeatureWaitingNavigation

    @Binds
    abstract fun bindNavigator(navigatorImpl: NavigatorImpl): Navigator
}