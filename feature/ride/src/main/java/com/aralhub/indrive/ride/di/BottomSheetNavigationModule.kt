package com.aralhub.indrive.ride.di

import com.aralhub.indrive.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.indrive.ride.navigation.sheet.SheetNavigator
import com.aralhub.indrive.ride.navigation.sheet.SheetNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BottomSheetNavigationModule {

    @Binds
    abstract fun bindBottomSheetNavigator(bottomSheetNavigator: SheetNavigatorImpl): SheetNavigator

    @Binds
    abstract fun bindFeatureRideBottomNavigation(bottomSheetNavigatorImpl: SheetNavigatorImpl): FeatureRideBottomSheetNavigation
}