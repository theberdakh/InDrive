package com.aralhub.offers.di

import com.aralhub.offers.navigation.sheet.FeatureOffersBottomSheetNavigation
import com.aralhub.offers.navigation.sheet.SheetNavigator
import com.aralhub.offers.navigation.sheet.SheetNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BottomSheetNavigationModule {

    @Binds
    abstract fun bindSheetNavigator(bottomSheetNavigator: SheetNavigatorImpl): SheetNavigator

    @Binds
    abstract fun bindFeatureOffersBottomNavigation(bottomSheetSheetNavigatorImplImpl: SheetNavigatorImpl): FeatureOffersBottomSheetNavigation
}