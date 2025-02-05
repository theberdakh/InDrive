package com.aralhub.offers.navigation.sheet

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BottomSheetNavigation {

    @Binds
    abstract fun bindFeatureOffersBottomNavigation(bottomSheetNavigator: SheetNavigator): FeatureOffersBottomSheetNavigation
}