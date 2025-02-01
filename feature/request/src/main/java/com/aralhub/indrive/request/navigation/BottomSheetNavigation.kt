package com.aralhub.indrive.request.navigation

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BottomSheetNavigation {

    @Binds
    abstract fun bindFeatureRequestNavigation(bottomSheetNavigator: BottomSheetNavigator): FeatureRequestNavigation

}