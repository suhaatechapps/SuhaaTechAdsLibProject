package com.zee.suhaatechadslibmodule.fallbackstrategies

import com.zee.suhaatechadslibmodule.types.TrueAdPriorityType
import com.zee.suhaatechadslibmodule.types.TrueAdsType

interface TrueStrategy {
    fun zBannerStrategy(
        zGlobalPriority: TrueAdPriorityType,
        zAdsType: TrueAdsType,
    ): TrueAdPriorityType

    fun zNativeAdvancedStrategy(
        zGlobalPriority: TrueAdPriorityType,
        zAdsType: TrueAdsType,
    ): TrueAdPriorityType

    fun zInterstitialStrategy(
        zGlobalPriority: TrueAdPriorityType,
        zAdsType: TrueAdsType,
    ): TrueAdPriorityType

    fun zNativeBannerStrategy(
        zGlobalPriority: TrueAdPriorityType,
        zAdsType: TrueAdsType,
    ): TrueAdPriorityType
}