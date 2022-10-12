package com.zee.suhaatechadslibmodule.mediation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.zee.suhaatechadslibmodule.facebook.FaceBookAdManager
import com.zee.suhaatechadslibmodule.mediation.callbacks.TrueAdCallbacks
import com.zee.suhaatechadslibmodule.mediation.callbacks.TrueInterCallbacks
import com.zee.suhaatechadslibmodule.mediation.customadview.TrueZBannerView
import com.zee.suhaatechadslibmodule.mediation.customadview.TrueZNativeAdvancedView
import com.zee.suhaatechadslibmodule.mediation.customadview.TrueZNativeBannerFlippingView
import com.zee.suhaatechadslibmodule.mediation.customadview.TrueZNativeBannerSimpleView
import com.zee.suhaatechadslibmodule.mediation.fallbackstrategies.TrueAdMobFallbackTrueStrategy
import com.zee.suhaatechadslibmodule.mediation.interfaces.TrueAdCallBackInterface
import com.zee.suhaatechadslibmodule.mediation.types.TrueAdPriorityType
import com.zee.suhaatechadslibmodule.mediation.types.TrueAdPriorityType.*
import com.zee.suhaatechadslibmodule.mediation.types.TrueAdsType
import com.zee.suhaatechadslibmodule.mediation.types.TrueWhatAd

@SuppressLint("StaticFieldLeak")
object TrueAdManager : TrueAdCallBackInterface {

    private var zAdMobManager: TrueAdMobManager? = null
    private var TAG = "AdManagerClass"
    lateinit var context: Context
    private var zBannerPriorityType: TrueAdPriorityType = Z_AD_MOB
    private var zNativeBannerSimplePriorityType: TrueAdPriorityType = Z_AD_MOB
    private var zNativeBannerFlippingPriorityType: TrueAdPriorityType = Z_AD_MOB
    private var zNativeAdvancedPriorityType: TrueAdPriorityType = Z_AD_MOB
    private var zInterstitialPriorityType: TrueAdPriorityType = Z_AD_MOB
    var hFacebookManger: FaceBookAdManager? = null
    private var zTimeOut: Long = TrueConstants.h3SecTimeOut

    private var zAdManagerInterCallbacks: TrueInterCallbacks? = null
    private var zAdManagerAdCallbacks: TrueAdCallbacks? = null
    private var interstitialAdId: String? = null
    var nativeAdvanceAdId: String? = null
//    var trueAdMobManager: TrueAdMobManager? = null

    fun zInitializeAds(
        zContext: Context,
    ) {
        context = zContext
        zAdMobManager = TrueAdMobManager(context)
        hFacebookManger = FaceBookAdManager(context)
        TrueAdMobManager(zContext).zSetInterCallbacks(zInterCallbacks)
        TrueAdMobManager(zContext).zSetNativeCallbacks(zAdCallbacks)
    }

    fun zhSetInterCallbacks(
        interAdId: String,
        interCallbacks: TrueInterCallbacks
    ) {

        interstitialAdId = interAdId
        zAdManagerInterCallbacks = interCallbacks
    }

    fun zSetNativeCallbacks(nativeAdId: String, adCallbacks: TrueAdCallbacks) {
        nativeAdvanceAdId = nativeAdId
        zAdManagerAdCallbacks = adCallbacks
    }

    fun zLoadInterstitial(
        activity: Activity,
        zPriorityType: TrueAdPriorityType = zInterstitialPriorityType,
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> {
                    zAdMobManager?.zLoadInterstitialAd(
                        activity, interstitialAdId!!
                    )
                }
                Z_NONE -> Unit
            }
        }
    }

    fun zLoadInterstitialWithCallBacks(
        activity: Activity,
        zPriorityType: TrueAdPriorityType = zInterstitialPriorityType,
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> {
                    zAdMobManager?.zLoadInterstitialAdWithCallBacks(
                        activity, interstitialAdId!!,
                        this
                    )
                }
                Z_NONE -> Unit
            }
        }
    }

    fun zShowInterstitial(
        activity: Activity,
        interNewAdID: String,
        priority: TrueAdPriorityType = zInterstitialPriorityType
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (priority) {
                Z_AD_MOB -> {
                    if (zAdMobManager?.zInterstitialAd != null) {
                        zAdMobManager?.zInterstitialAd?.show(activity)
                        return
                    } else {
                        zAdMobManager?.zLoadInterstitialAd(
                            activity, interNewAdID
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun zShowFbInterstitial(
        activity: Activity,
        adsId: String
    ) {
        if (hFacebookManger?.hGetFbInterstitialAd() != null &&
            hFacebookManger?.hGetFbInterstitialAd()?.isAdLoaded == true
        ) {
            hFacebookManger?.hGetFbInterstitialAd()?.show()
            return
        } else
            hFacebookManger?.loadFbInterstitialAds(activity, adsId)
    }

    fun zShowInterstitialWithOutCallBacks(
        activity: Activity,
        interNewAdID: String,
        trueAdCallBackInterface: TrueAdCallBackInterface,
        priority: TrueAdPriorityType = zInterstitialPriorityType
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (priority) {
                Z_AD_MOB -> {
                    if (zAdMobManager?.zInterstitialAd != null) {
                        zAdMobManager?.zInterstitialAd?.show(activity)
                        return
                    } else {
                        zAdMobManager?.zLoadInterstitialAdWithCallBacks(
                            activity, interNewAdID,
                            trueAdCallBackInterface
                        )
                    }
                }
                Z_FACE_BOOK -> {

                }
                else -> Unit
            }
        }
    }

    fun zShowFBInterstitialWithOutCallBacks(
        activity: Activity,
        adsId: String,
        trueAdCallBackInterface: TrueAdCallBackInterface,
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            if (hFacebookManger?.hGetFbInterstitialAd() != null &&
                hFacebookManger?.hGetFbInterstitialAd()?.isAdLoaded == true
            ) {
                hFacebookManger?.hGetFbInterstitialAd()?.show()
                return
            } else
                hFacebookManger?.zLoadFBInterstitialAdWithCallBacks(
                    activity,
                    adsId,
                    trueAdCallBackInterface
                )
        }
    }

    /**Load Interstitial Ads With Intent*/
    fun zLoadAndShowInterstitialAdWithIntent(
        activity: Activity,
        adsId: String,
        destination: Class<*>
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            zAdMobManager?.zLoadAndShowInterstitialAdWithIntent(
                activity, adsId, destination
            )
        }
    }

    /**Load Interstitial Ad In Advance*/
    fun zLoadInterstitialInAdvance(
        context: Activity,
        adId: String
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            zAdMobManager?.zLoadInterstitialInAdvance(
                context, adId
            )
        }
    }

    /**Show Interstitial Ad In Advance*/
    fun zShowInterstitialInAdvance(
        context: Activity,
        destination: Class<*>
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            zAdMobManager?.zShowInterstitialAdInAdvance(
                context, destination
            )
        }
    }

    /**Load Interstitial Ad In Advance*/
    fun zLoadInterstitialInAdvanceWithOutIntent(
        context: Activity,
        adId: String
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            zAdMobManager?.zLoadInterstitialInAdvanceWithOutIntent(
                context, adId
            )
        }
    }

    /**Show Interstitial Ad In Advance*/
    fun zShowInterstitialInAdvanceWithOutIntent(
        context: Activity
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            zAdMobManager?.zShowInterstitialAdInAdvanceWithOutIntent(
                context
            )
        }
    }

    /**Load Interstitial Ad In Advance*/
    fun zLoadInterstitialInAdvanceWithIntent(
        context: Activity,
        adId: String
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            zAdMobManager?.zLoadInterstitialInAdvanceWithOutIntent(
                context, adId
            )
        }
    }

    /**Show Interstitial Ad In Advance*/
    fun zShowInterstitialInAdvanceWithIntent(
        context: Activity
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            zAdMobManager?.zShowInterstitialAdInAdvanceWithOutIntent(
                context
            )
        }
    }


/*fun hIsInterstitialAvailable(activity: Activity): Boolean {
    val hPriorityType: TrueAdPriorityType =
        hInterstitialPriorityType
    when (hPriorityType) {
        H_AD_MOB -> {
            if (hAdMobManager?.hInterstitialAd != null) {
                return true
            } else {
                hAdMobManager?.hLoadInterstitialAd(context,
                    "ca-app-pub-3940256099942544/1033173712")
            }
        }
        H_FACE_BOOK -> {

            if (hFacebookManger?.hGetFbInterstitialAd() != null &&
                hFacebookManger!!.hGetFbInterstitialAd()?.isAdLoaded == true
            ) {
                return true
            } else {
                hFacebookManger?.hLoadFbInterstitial()
            }
        }
        else -> Unit
    }
    return false
}*/

    private fun zGetInterFallBackPriority(zAdsType: TrueAdsType): TrueAdPriorityType {
        return when (zInterstitialPriorityType) {
            Z_AD_MOB -> TrueAdMobFallbackTrueStrategy.zInterstitialStrategy(
                zGlobalPriority = zInterstitialPriorityType,
                zAdsType = zAdsType
            )
            else -> Z_NONE
        }
    }

    fun zShowBanner(
        zBannerView: TrueZBannerView,
        bannerAdId: String,
        zPriorityType: TrueAdPriorityType = zBannerPriorityType,
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> zAdMobManager?.zShowBanner(
                    context,
                    zBannerView,
                    bannerAdId
                )
                else -> Unit
            }
        } else {
            zBannerView.visibility = View.GONE
        }
    }

    fun zShowFlippingNativeBanner(
        zNativeBannerFlippingView: TrueZNativeBannerFlippingView,
        nativeAdvanceAdId: String,
        zPriorityType: TrueAdPriorityType = zNativeBannerSimplePriorityType,
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> zAdMobManager?.zLoadNativeBannerFlipping(
                    zNativeBannerFlippingView, nativeAdvanceAdId
                )

                else -> Unit
            }
        } else {
            zNativeBannerFlippingView.visibility = View.GONE
        }
    }

    fun zShowSimpleNativeBanner(
        zNativeBannerSimpleView: TrueZNativeBannerSimpleView,
        nativeAdId: String,
        zPriorityType: TrueAdPriorityType = zNativeBannerFlippingPriorityType,
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> zAdMobManager?.zShowNativeBannerSimple(
                    zNativeBannerSimpleView,
                    nativeAdId
                )
                else -> Unit
            }
        } else {
            zNativeBannerSimpleView.visibility = View.GONE
        }
    }

    private fun zGetFallBackPriorityForNativeBannerFlipping(
        zAdsType: TrueAdsType,
    ): TrueAdPriorityType {
        return when (zNativeBannerSimplePriorityType) {
            Z_AD_MOB ->
                TrueAdMobFallbackTrueStrategy.zNativeBannerStrategy(
                    zGlobalPriority = zNativeBannerSimplePriorityType,
                    zAdsType = zAdsType
                )
            else -> Z_NONE
        }
    }

    private fun zGetFallBackPriorityForNativeBannerSimple(
        zAdsType: TrueAdsType,
    ): TrueAdPriorityType {
        return when (zNativeBannerFlippingPriorityType) {
            Z_AD_MOB ->
                TrueAdMobFallbackTrueStrategy.zNativeBannerStrategy(
                    zGlobalPriority = zNativeBannerFlippingPriorityType,
                    zAdsType = zAdsType
                )
            else -> Z_NONE
        }
    }

    /**Load Native Ad In Advance*/
    fun zLoadNativeAdInAdvance(
        context: Activity,
        nativeAdsId: String
    ) {
        /*trueAdMobManager = TrueAdMobManager(context)*/
        zAdMobManager!!.loadAdmobNativeInAdvance(context, nativeAdsId)
    }

    /**Show Native Ad in Advance*/
    fun zShowNativeAdInAdvance(
        context: Activity,
        nativeAdId: String,
        zNativeAdvancedView: TrueZNativeAdvancedView,
        zPriorityType: TrueAdPriorityType = zNativeAdvancedPriorityType
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> zAdMobManager!!.showAdmobNativeInAdvance(
                    context,
                    nativeAdId,
                    zNativeAdvancedView
                )
                Z_NONE -> Unit
            }
        } else {
            zNativeAdvancedView.visibility = View.GONE
        }

    }

    /**Load Flipping Ad In Advance*/
    fun zLoadFlippingNativeAdInAdvance(
        context: Activity,
        flippingNativeAdsId: String
    ) {
        zAdMobManager!!.loadAdmobFlippingNativeInAdvance(context, flippingNativeAdsId)
    }

    fun zShowFlippingNativeAdInAdvance(
        context: Activity,
        flippingNativeAdId: String,
        zNativeBannerFlippingView: TrueZNativeBannerFlippingView,
        zPriorityType: TrueAdPriorityType = zNativeBannerFlippingPriorityType
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> zAdMobManager!!.showAdmobFlippingNativeInAdvance(
                    context,
                    flippingNativeAdId,
                    zNativeBannerFlippingView
                )
                Z_NONE -> Unit
            }
        } else {
            zNativeBannerFlippingView.visibility = View.GONE
        }
    }

    /**Load Simple Ad In Advance*/
    fun zLoadSimpleNativeAdInAdvance(
        context: Activity,
        simpleNativeAdsId: String
    ) {
        zAdMobManager!!.loadAdmobSimpleNativeInAdvance(context, simpleNativeAdsId)
    }

    fun zShowSimpleNativeAdInAdvance(
        context: Activity,
        simpleNativeAdsIdNativeAdId: String,
        simpleNativeAdsIdBannerSimpleView: TrueZNativeBannerSimpleView,
        zPriorityType: TrueAdPriorityType = zNativeBannerSimplePriorityType
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> zAdMobManager!!.showAdmobSimpleNativeInAdvance(
                    context,
                    simpleNativeAdsIdNativeAdId,
                    simpleNativeAdsIdBannerSimpleView
                )
                Z_NONE -> Unit
            }
        } else {
            simpleNativeAdsIdBannerSimpleView.visibility = View.GONE
        }
    }


    fun zShowNativeAdvanced(
        zNativeAdvancedView: TrueZNativeAdvancedView,
        nativeAdvanceAdId: String,
        zPriorityType: TrueAdPriorityType = zNativeAdvancedPriorityType,
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> zAdMobManager?.zShowNativeAdvanced(
                    context,
                    zNativeAdvancedView,
                    nativeAdvanceAdId
                )
                Z_NONE -> Unit
            }
        } else {
            zNativeAdvancedView.visibility = View.GONE
        }
    }

    private fun zGetFallbackPriorityForNativeAdvanced(
        zAdsType: TrueAdsType,
    ): TrueAdPriorityType {
        return when (zNativeAdvancedPriorityType) {
            Z_AD_MOB -> TrueAdMobFallbackTrueStrategy.zNativeAdvancedStrategy(
                zGlobalPriority = zNativeAdvancedPriorityType,
                zAdsType = zAdsType
            )
            else -> Z_NONE
        }
    }

    private fun zGetFallbackPriorityForBanner(
        zAdsType: TrueAdsType,
    ): TrueAdPriorityType {
        return when (zBannerPriorityType) {
            Z_AD_MOB -> TrueAdMobFallbackTrueStrategy.zBannerStrategy(
                zGlobalPriority = zBannerPriorityType,
                zAdsType = zAdsType
            )
            else -> Z_NONE
        }
    }


    fun zShowBannerWithOutFallback(
        bannerAdContainer: TrueZBannerView,
        adId: String,
        zPriorityType: TrueAdPriorityType = zBannerPriorityType,
    ) {
        if (TrueConstants.isNetworkSpeedHigh()) {
            when (zPriorityType) {
                Z_AD_MOB -> zAdMobManager?.zShowBanner(
                    context,
                    bannerAdContainer,
                    adId,
                    zIsWithFallback = false,
                )
                else -> Unit
            }
        } else {
            bannerAdContainer.visibility = View.GONE
        }
    }

    /**For Manually changing the priorities*/
    fun zSetNativeBannerPriorityFlipping(
        nativeBannerPriorityType: TrueAdPriorityType,
    ) {
        zNativeBannerSimplePriorityType = nativeBannerPriorityType
    }

    fun zSetNativeBannerPrioritySimple(
        nativeBannerPriorityType: TrueAdPriorityType,
    ) {
        zNativeBannerFlippingPriorityType = nativeBannerPriorityType
    }

    fun zSetNativeAdvancedPriority(
        nativeAdvancedPriorityType: TrueAdPriorityType,
    ) {
        zNativeAdvancedPriorityType = nativeAdvancedPriorityType
    }

    fun zSetInterstitialPriority(
        interstitialPriorityType: TrueAdPriorityType,
    ) {
        zInterstitialPriorityType = interstitialPriorityType
    }

    fun zSetBannerPriority(
        bannerPriorityType: TrueAdPriorityType,
    ) {
        zBannerPriorityType = bannerPriorityType
    }

    fun zSetTimeout(timeOut: Long) {
        zTimeOut = timeOut
    }

    /**Interstitial Call Back*/
    private var zInterCallbacks: TrueInterCallbacks = object : TrueInterCallbacks() {
        override fun zOnAdFailedToLoad(
            zAdType: TrueAdsType,
            zError: TrueError,
            zActivity: Activity?,
        ) {
            zAdManagerInterCallbacks?.zOnAdFailedToLoad(zAdType, zError)
            zLoadInterstitial(
                zActivity!!,
                zPriorityType = zGetInterFallBackPriority(
                    zAdType
                )
            )
            zLoadInterstitialWithCallBacks(
                zActivity,
                zPriorityType = zGetInterFallBackPriority(
                    zAdType
                )
            )
        }

        override fun zOnAddLoaded(zAdType: TrueAdsType) {
            zAdManagerInterCallbacks?.zOnAddLoaded(zAdType)
        }

        override fun zOnAdFailedToShowFullContent(
            zAdType: TrueAdsType,
            zError: TrueError,
        ) {
            zAdManagerInterCallbacks?.zOnAdFailedToShowFullContent(zAdType, zError)
        }

        override fun zOnAddShowed(zAdType: TrueAdsType) {
            zAdManagerInterCallbacks?.zOnAddShowed(zAdType)
        }

        override fun zOnAddDismissed(zAdType: TrueAdsType) {
            zAdManagerInterCallbacks?.zOnAddDismissed(zAdType)
        }

        override fun zOnAdTimedOut(zAdType: TrueAdsType) {
            zAdManagerInterCallbacks?.zOnAdTimedOut(zAdType)
        }
    }

    /**Native Call Back*/
    private var zAdCallbacks: TrueAdCallbacks = object : TrueAdCallbacks() {
        override fun zAdLoaded(
            zAdType: TrueAdsType,
            zWhatAd: TrueWhatAd,
        ) {
            zAdManagerAdCallbacks?.zAdLoaded(
                zAdType = zAdType,
                zWhatAd = zWhatAd
            )
        }

        override fun zAdClicked(
            zAdType: TrueAdsType,
            zWhatAd: TrueWhatAd,
        ) {
            zAdManagerAdCallbacks?.zAdClicked(
                zAdType = zAdType,
                zWhatAd = zWhatAd
            )
        }

        override fun zAdImpression(
            zAdType: TrueAdsType,
            zWhatAd: TrueWhatAd,
        ) {
            zAdManagerAdCallbacks?.zAdImpression(
                zAdType = zAdType,
                zWhatAd = zWhatAd
            )
        }

        override fun zAdClosed(
            zAdType: TrueAdsType,
            zWhatAd: TrueWhatAd,
        ) {
            zAdManagerAdCallbacks?.zAdClosed(
                zAdType = zAdType,
                zWhatAd = zWhatAd
            )
        }

        override fun zAdFailedToLoad(
            zAdType: TrueAdsType,
            zWhatAd: TrueWhatAd,
            zError: TrueError,
            zNativeView: ViewGroup,
            zIsWithFallback: Boolean
        ) {
            zAdManagerAdCallbacks?.zAdFailedToLoad(
                zAdType = zAdType,
                zWhatAd = zWhatAd,
                zError = zError,
                zNativeView = zNativeView,
                zIsWithFallback = zIsWithFallback,

                )
            when (zIsWithFallback) {
                true -> when (zWhatAd) {
                    TrueWhatAd.Z_NATIVE_BANNER_FLIPPING -> zShowFlippingNativeBanner(
                        zNativeBannerFlippingView = zNativeView as TrueZNativeBannerFlippingView,
                        zPriorityType = zGetFallBackPriorityForNativeBannerFlipping(
                            zAdsType = zAdType
                        ),
                        nativeAdvanceAdId = nativeAdvanceAdId!!
                    )
                    TrueWhatAd.Z_NATIVE_BANNER_SIMPLE -> zShowSimpleNativeBanner(
                        zNativeBannerSimpleView = zNativeView as TrueZNativeBannerSimpleView,
                        zPriorityType = zGetFallBackPriorityForNativeBannerSimple(
                            zAdsType = zAdType
                        ),
                        nativeAdId = nativeAdvanceAdId!!
                    )
                    TrueWhatAd.Z_NATIVE_ADVANCED -> zShowNativeAdvanced(
                        zNativeAdvancedView = zNativeView as TrueZNativeAdvancedView,
                        zPriorityType = zGetFallbackPriorityForNativeAdvanced(
                            zAdsType = zAdType
                        ),
                        nativeAdvanceAdId = nativeAdvanceAdId!!
                    )
                    /*TrueWhatAd.Z_BANNER -> zShowBanner(
                        zBannerView = zNativeView as TrueZBannerView,
                        "ca-app-pub-3940256099942544/6300978111",
                        zPriorityType = zGetFallbackPriorityForBanner(
                            zAdsType = zAdType
                        )
                    )*/
                    TrueWhatAd.Z_INTER -> Unit
                    else -> {
                    }
                }
                false -> Unit
            }

        }

        override fun zNativeAdOpened(
            zAdType: TrueAdsType,
            zWhatAd: TrueWhatAd,
        ) {
            zAdManagerAdCallbacks?.zNativeAdOpened(
                zAdType = zAdType,
                zWhatAd = zWhatAd
            )
        }
    }

    override fun onShowAdComplete() {
        TODO("Not yet implemented")
    }
}