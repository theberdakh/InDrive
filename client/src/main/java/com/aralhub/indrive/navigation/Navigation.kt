package com.aralhub.indrive.navigation

import com.aralhub.auth.navigation.FeatureAuthNavigation
import com.aralhub.home.navigation.FeatureHomeNavigation
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
    abstract fun bindFeatureHomeNavigation(navigatorImpl: NavigatorImpl): FeatureHomeNavigation

    @Binds
    abstract fun bindNavigator(navigatorImpl: NavigatorImpl): Navigator
}