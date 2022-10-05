package com.zee.suhaatechadslibmodule.mediation.callbacks

import android.view.ViewGroup
import com.zee.suhaatechadslibmodule.mediation.TrueError
import com.zee.suhaatechadslibmodule.mediation.types.TrueAdsType
import com.zee.suhaatechadslibmodule.mediation.types.TrueWhatAd


abstract class TrueAdCallbacks {

    open fun zAdLoaded(
        zAdType: TrueAdsType,
        zWhatAd: TrueWhatAd,
    ) {
    }

    open fun zAdClicked(
        zAdType: TrueAdsType,
        zWhatAd: TrueWhatAd
    ) {
    }

    open fun zAdImpression(
        zAdType: TrueAdsType,
        zWhatAd: TrueWhatAd
    ) {
    }

    open fun zAdClosed(
        zAdType: TrueAdsType,
        zWhatAd: TrueWhatAd
    ) {
    }

    open fun zAdFailedToLoad(
        zAdType: TrueAdsType,
        zWhatAd: TrueWhatAd,
        zError: TrueError,
        zNativeView: ViewGroup,
        zIsWithFallback: Boolean
    ) {
    }

    open fun zNativeAdOpened(
        zAdType: TrueAdsType,
        zWhatAd: TrueWhatAd
    ) {
    }

}

