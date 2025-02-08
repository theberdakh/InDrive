package com.aralhub.indrive.request.di

import com.aralhub.indrive.request.navigation.sheet.FeatureRequestBottomSheetNavigation
import com.aralhub.indrive.request.navigation.sheet.SheetNavigator
import com.aralhub.indrive.request.navigation.sheet.SheetNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SheetNavigationModule {

    @Binds
    abstract fun bindSheetNavigator(bottomSheetNavigator: SheetNavigatorImpl): SheetNavigator

    @Binds
    abstract fun bindFeatureRequestNavigation(bottomSheetNavigator: SheetNavigatorImpl): FeatureRequestBottomSheetNavigation

}