package com.aralhub.indrivedriver.di

import com.aralhub.auth.navigation.FeatureAuthNavigation
import com.aralhub.indrivedriver.navigation.Navigator
import com.aralhub.indrivedriver.navigation.NavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    abstract fun bindFeatureAuthNavigation(navigatorImpl: NavigatorImpl): FeatureAuthNavigation

    @Binds
    abstract fun bindNavigator(navigatorImpl: NavigatorImpl): Navigator
}