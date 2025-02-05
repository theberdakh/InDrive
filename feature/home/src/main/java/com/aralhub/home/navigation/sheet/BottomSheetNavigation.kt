package com.aralhub.home.navigation.sheet

import com.aralhub.indrive.request.navigation.FeatureRequestBottomSheetNavigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BottomSheetNavigation {

    @Binds
    abstract fun bindFeatureRequestNavigation(bottomSheetNavigator: SheetNavigator): FeatureRequestBottomSheetNavigation

}