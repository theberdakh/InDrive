package com.aralhub.indrive.ride.navigation.sheet

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BottomSheetNavigation {

    @Binds
    abstract fun bindFeatureRideBottomNavigation(bottomSheetNavigator: SheetNavigator): FeatureRideBottomSheetNavigation
}