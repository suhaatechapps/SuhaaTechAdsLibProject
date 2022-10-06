package com.zee.suhaatechadslibmodule.mediation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.zee.suhaatechadslibmodule.R
import com.zee.suhaatechadslibmodule.databinding.*
import com.zee.suhaatechadslibmodule.mediation.TrueAdsCalBackObject.interstitialAdnValue
import com.zee.suhaatechadslibmodule.mediation.callbacks.TrueAdCallbacks
import com.zee.suhaatechadslibmodule.mediation.callbacks.TrueInterCallbacks
import com.zee.suhaatechadslibmodule.mediation.customadview.TrueZBannerView
import com.zee.suhaatechadslibmodule.mediation.customadview.TrueZNativeAdvancedView
import com.zee.suhaatechadslibmodule.mediation.customadview.TrueZNativeBannerFlippingView
import com.zee.suhaatechadslibmodule.mediation.customadview.TrueZNativeBannerSimpleView
import com.zee.suhaatechadslibmodule.mediation.database.TrueZSPRepository
import com.zee.suhaatechadslibmodule.mediation.interfaces.TrueAdCallBackInterface
import com.zee.suhaatechadslibmodule.mediation.templates.TrueBannerTemplateStyle
import com.zee.suhaatechadslibmodule.mediation.templates.TrueNativeTemplateStyle
import com.zee.suhaatechadslibmodule.mediation.types.TrueAdsType
import com.zee.suhaatechadslibmodule.mediation.types.TrueWhatAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TrueAdMobManager(
    private val zContext: Context?
) {
    var zInterstitialAd: InterstitialAd? = null
        private set
    private var zBannerAdView: AdView? = null
    private var zInterCallbacks: TrueInterCallbacks? = null
    private var zAdCallbacks: TrueAdCallbacks? = null
    private var prefName: String? = null
    private var prefNameInter: String? = null
    private var prefNameNative: String? = null
    private var prefNameNativeBanner: String? = null
    private var prefNameFlippingNativeBanner: String? = null
    private var simpleNativeBooleanValue = false
    private var floatingNativeBooleanValue = false
    private var nativeAdvanceBooleanValue = false
    var TAG = "TrueAdMobClass"
    lateinit var dialog: Dialog
    lateinit var targetDestination: Class<*>

    @Suppress("DEPRECATION")
    companion object {
        var mShowInterstitialAds = false
        var zInterstitialAdInAdvance: InterstitialAd? = null
        var mAdmobNative: NativeAd? = null
        var mFlippingAdmobNative: NativeAd? = null
        var mSimpleAdmobNative: NativeAd? = null
        var prefNameInterInAdvance: String? = null
        var admobNativeAdLoader: AdLoader? = null
        var mSimpleAdmobNativeAdLoader: AdLoader? = null
        var mFlippingAdmobNativeAdLoader: AdLoader? = null
        private var zInterCallbacksInAdvance: TrueInterCallbacks? = null

        private var prefNameFlippingNativeInAdvanced: String? = null
        private var prefNameSimpleNativeInAdvanced: String? = null
        private var prefNameNativeInAdvanced: String? = null

        fun zGetPixelFromDp(application: Context?, dp: Int): Int {
            val display =
                (application!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val scale = outMetrics.density
            return (dp * scale + 0.5f).toInt()
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    fun zLoadInterstitialAd(
        context: Activity,
        interId: String
    ) {
        dialog = Dialog(context)
        loadAds(context)
        if (interId.contains("/")) {
            prefNameInter = interId.substring(interId.lastIndexOf("/") + 1)
        }
        CoroutineScope(Dispatchers.Main).launch {
            var zCallBackCalled = false
            dialog.show()
            /** it will be executed when its true*/
            val adRequest = AdRequest.Builder().build()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    InterstitialAd.load(
                        zContext!!,
                        interId,
                        adRequest,
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                dialog.dismiss()
                                this@TrueAdMobManager.zInterstitialAd = interstitialAd

                                interstitialAd.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {
                                        override fun onAdDismissedFullScreenContent() {
                                            zInterCallbacks?.zOnAddDismissed(TrueAdsType.Z_ADMOB)
                                            zCallBackCalled = true
                                        }

                                        override fun onAdFailedToShowFullScreenContent(
                                            adError: AdError
                                        ) {

                                            zInterCallbacks?.zOnAdFailedToShowFullContent(
                                                zAdType = TrueAdsType.Z_ADMOB,
                                                zError = TrueError(
                                                    zMessage = adError.message,
                                                    zCode = adError.code,
                                                    zDomain = adError.domain,
                                                )
                                            )
                                            zCallBackCalled = true
                                        }

                                        override fun onAdShowedFullScreenContent() {
                                            zInterCallbacks?.zOnAddShowed(TrueAdsType.Z_ADMOB)
                                            zCallBackCalled = true
                                            zInterstitialAd = null
                                        }

                                        override fun onAdClicked() {
                                            super.onAdClicked()

                                        }
                                    }

                                zInterCallbacks?.zOnAddLoaded(zAdType = TrueAdsType.Z_ADMOB)
                                zCallBackCalled = true
                                interstitialAd.show(context)
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                zInterCallbacks?.zOnAdFailedToLoad(
                                    zAdType = TrueAdsType.Z_ADMOB,
                                    zError = TrueError(
                                        zMessage = loadAdError.message,
                                        zCode = loadAdError.code,
                                        zDomain = loadAdError.domain,
                                    )
                                )
                                dialog.dismiss()
                                zCallBackCalled = true
                            }
                        }
                    )
                    if (zCallBackCalled.not()) {
                        zInterCallbacks?.zOnAdTimedOut(TrueAdsType.Z_ADMOB)
                    }
                },
                0
            )
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    fun zLoadInterstitialAdWithCallBacks(
        context: Activity,
        interId: String,
        trueAdCallBackInterface: TrueAdCallBackInterface
    ) {
        dialog = Dialog(context)
        loadAds(context)
        if (interId.contains("/")) {
            prefNameInter = interId.substring(interId.lastIndexOf("/") + 1)
        }
        CoroutineScope(Dispatchers.Main).launch {
            var zCallBackCalled = false
            dialog.show()
            /** it will be executed when its true*/
            val adRequest = AdRequest.Builder().build()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    TrueZSPRepository.setAdAvailableValue(context, true)
                    InterstitialAd.load(
                        zContext!!,
                        interId,
                        adRequest,
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                this@TrueAdMobManager.zInterstitialAd = interstitialAd
                                TrueZSPRepository.setAdAvailableValue(context, true)
                                interstitialAd.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {
                                        override fun onAdDismissedFullScreenContent() {
                                            TrueZSPRepository.setAdAvailableValue(
                                                context,
                                                true
                                            )
                                            zInterCallbacks?.zOnAddDismissed(TrueAdsType.Z_ADMOB)
                                            zCallBackCalled = true
                                            trueAdCallBackInterface.onShowAdComplete()
                                        }

                                        override fun onAdFailedToShowFullScreenContent(
                                            adError: AdError
                                        ) {
                                            TrueZSPRepository.setAdAvailableValue(
                                                context,
                                                true
                                            )
                                            trueAdCallBackInterface.onShowAdComplete()
                                            zInterCallbacks?.zOnAdFailedToShowFullContent(
                                                zAdType = TrueAdsType.Z_ADMOB,
                                                zError = TrueError(
                                                    zMessage = adError.message,
                                                    zCode = adError.code,
                                                    zDomain = adError.domain,
                                                )
                                            )
                                            zCallBackCalled = true
                                        }

                                        override fun onAdShowedFullScreenContent() {
                                            zInterCallbacks?.zOnAddShowed(TrueAdsType.Z_ADMOB)
                                            zCallBackCalled = true
                                            zInterstitialAd = null
                                            TrueZSPRepository.setAdAvailableValue(
                                                context,
                                                true
                                            )
                                        }

                                        override fun onAdClicked() {
                                            super.onAdClicked()
                                            TrueZSPRepository.setAdAvailableValue(
                                                context,
                                                true
                                            )
                                        }
                                    }
                                zInterCallbacks?.zOnAddLoaded(zAdType = TrueAdsType.Z_ADMOB)
                                zCallBackCalled = true
                                interstitialAd.show(context)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    dialog.dismiss()
                                }, 500)
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                trueAdCallBackInterface.onShowAdComplete()
                                zInterCallbacks?.zOnAdFailedToLoad(
                                    zAdType = TrueAdsType.Z_ADMOB,
                                    zError = TrueError(
                                        zMessage = loadAdError.message,
                                        zCode = loadAdError.code,
                                        zDomain = loadAdError.domain,
                                    )
                                )
                                TrueZSPRepository.setAdMobAvailable(context, true)
                                TrueZSPRepository.setAdAvailableValue(context, false)
                                dialog.dismiss()
                                zCallBackCalled = true
                            }
                        }
                    )
                    if (zCallBackCalled.not()) {
                        zInterCallbacks?.zOnAdTimedOut(TrueAdsType.Z_ADMOB)
                    }
                },
                0
            )

        }
    }

    fun zLoadInterstitialInAdvance(
        context: Activity,
        interId: String
    ) {
        if (interId.contains("/")) {
            prefNameInterInAdvance = interId.substring(interId.lastIndexOf("/") + 1)
        }
        CoroutineScope(Dispatchers.Main).launch {
            var zCallBackCalled = false

            /** it will be executed when its true*/
            val adRequest = AdRequest.Builder().build()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    TrueZSPRepository.setAdAvailableValue(context, false)
                    InterstitialAd.load(
                        zContext!!,
                        interId,
                        adRequest,
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                zInterstitialAdInAdvance = interstitialAd
                                interstitialAd.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {
                                        override fun onAdDismissedFullScreenContent() {
                                            zInterCallbacksInAdvance?.zOnAddDismissed(
                                                TrueAdsType.Z_ADMOB
                                            )
                                            zCallBackCalled = true
                                            TrueConstants.mShowInterstitialAds = false
                                            context.startActivity(
                                                Intent(
                                                    context,
                                                    targetDestination
                                                )
                                            )
                                        }

                                        override fun onAdFailedToShowFullScreenContent(
                                            adError: AdError
                                        ) {
                                            TrueConstants.mShowInterstitialAds = true
                                            zInterCallbacksInAdvance?.zOnAdFailedToShowFullContent(
                                                zAdType = TrueAdsType.Z_ADMOB,
                                                zError = TrueError(
                                                    zMessage = adError.message,
                                                    zCode = adError.code,
                                                    zDomain = adError.domain,
                                                )
                                            )
                                            zCallBackCalled = true
                                        }

                                        override fun onAdShowedFullScreenContent() {
                                            zInterCallbacksInAdvance?.zOnAddShowed(TrueAdsType.Z_ADMOB)
                                            zCallBackCalled = true
                                            zInterstitialAdInAdvance = null
                                            TrueConstants.mShowInterstitialAds = true
                                        }

                                        override fun onAdClicked() {
                                            super.onAdClicked()
                                        }

                                    }
                                zInterCallbacksInAdvance?.zOnAddLoaded(zAdType = TrueAdsType.Z_ADMOB)
                                zCallBackCalled = true
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                zInterCallbacksInAdvance?.zOnAdFailedToLoad(
                                    zAdType = TrueAdsType.Z_ADMOB,
                                    zError = TrueError(
                                        zMessage = loadAdError.message,
                                        zCode = loadAdError.code,
                                        zDomain = loadAdError.domain,
                                    )
                                )
                                TrueConstants.mShowInterstitialAds = false
                                zCallBackCalled = true
                            }
                        }
                    )
                    if (zCallBackCalled.not()) {
                        zInterCallbacksInAdvance?.zOnAdTimedOut(TrueAdsType.Z_ADMOB)
                    }
                },
                0
            )
        }
    }

    fun zShowInterstitialAdInAdvance(
        context: Activity,
        destination: Class<*>
    ) {
        dialog = Dialog(context)
        loadAds(context)
        targetDestination = destination
        if (zInterstitialAdInAdvance == null) {
            dialog.dismiss()
        } else {
            dialog.show()
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
                interstitialAdnValue = if (zInterstitialAdInAdvance != null) {
                    zInterstitialAdInAdvance?.show(context)
                    false
                } else {
                    true
                }
            }, 1000)
        }
    }

    /**Load Interstitial Ad Without Intent*/
    fun zLoadInterstitialInAdvanceWithOutIntent(
        context: Activity,
        interId: String
    ) {
        if (interId.contains("/")) {
            prefNameInterInAdvance = interId.substring(interId.lastIndexOf("/") + 1)
        }
        CoroutineScope(Dispatchers.Main).launch {
            var zCallBackCalled = false

            /** it will be executed when its true*/
            val adRequest = AdRequest.Builder().build()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    TrueZSPRepository.setAdAvailableValue(context, false)
                    InterstitialAd.load(
                        zContext!!,
                        interId,
                        adRequest,
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                zInterstitialAdInAdvance = interstitialAd
                                interstitialAd.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {
                                        override fun onAdDismissedFullScreenContent() {
                                            zInterCallbacksInAdvance?.zOnAddDismissed(
                                                TrueAdsType.Z_ADMOB
                                            )
                                            zCallBackCalled = true
                                            TrueConstants.mShowInterstitialAds = false
                                        }

                                        override fun onAdFailedToShowFullScreenContent(
                                            adError: AdError
                                        ) {
                                            TrueConstants.mShowInterstitialAds = true
                                            zInterCallbacksInAdvance?.zOnAdFailedToShowFullContent(
                                                zAdType = TrueAdsType.Z_ADMOB,
                                                zError = TrueError(
                                                    zMessage = adError.message,
                                                    zCode = adError.code,
                                                    zDomain = adError.domain,
                                                )
                                            )
                                            zCallBackCalled = true
                                        }

                                        override fun onAdShowedFullScreenContent() {
                                            zInterCallbacksInAdvance?.zOnAddShowed(TrueAdsType.Z_ADMOB)
                                            zCallBackCalled = true
                                            zInterstitialAdInAdvance = null
                                            TrueConstants.mShowInterstitialAds = true
                                        }

                                        override fun onAdClicked() {
                                            super.onAdClicked()

                                        }

                                    }

                                zInterCallbacksInAdvance?.zOnAddLoaded(zAdType = TrueAdsType.Z_ADMOB)
                                zCallBackCalled = true
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                zInterCallbacksInAdvance?.zOnAdFailedToLoad(
                                    zAdType = TrueAdsType.Z_ADMOB,
                                    zError = TrueError(
                                        zMessage = loadAdError.message,
                                        zCode = loadAdError.code,
                                        zDomain = loadAdError.domain,
                                    )
                                )
                                TrueConstants.mShowInterstitialAds = false
                                zCallBackCalled = true
                            }
                        }
                    )
                    if (zCallBackCalled.not()) {
                        zInterCallbacksInAdvance?.zOnAdTimedOut(TrueAdsType.Z_ADMOB)
                    }
                },
                0
            )

        }
    }

    /**Show Interstitial Ad WithOut Intent*/
    fun zShowInterstitialAdInAdvanceWithOutIntent(
        context: Activity,
    ) {
        dialog = Dialog(context)
        loadAds(context)
        if (zInterstitialAdInAdvance == null) {
            dialog.dismiss()
        } else {
            dialog.show()
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
                interstitialAdnValue = if (zInterstitialAdInAdvance != null) {
                    zInterstitialAdInAdvance?.show(context)
                    false
                } else {
                    true
                }
            }, 1000)
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    fun zLoadNativeBannerFlipping(
        zNativeBannerFlippingView: TrueZNativeBannerFlippingView,
        nativeAdvancedId: String,
        zIsWithFallback: Boolean = true
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            if (nativeAdvancedId.contains("/")) {
                prefNameFlippingNativeBanner =
                    nativeAdvancedId.substring(nativeAdvancedId.lastIndexOf("/") + 1)
            }
            val adLoader = AdLoader.Builder(
                zContext!!,
                nativeAdvancedId
            )
                .forNativeAd { NativeAd: NativeAd? ->
                    val cd = ColorDrawable()
                    val styles =
                        TrueNativeTemplateStyle.Builder().withMainBackgroundColor(cd).build()
                    AdmobNativeBannerLayoutBinding.inflate(
                        LayoutInflater.from(zContext),
                        null,
                        false
                    ).apply {
                        myTemplate.visibility = View.VISIBLE
                        myTemplate.setStyles(styles)
                        NativeAd?.let {
                            myTemplate.setNativeAd(it, true)
                        }
                        zNativeBannerFlippingView.zShowAdView(viewGroup = root)
                    }
                }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        zNativeBannerFlippingView.zShowHideAdLoader(true)
                        zAdCallbacks?.zAdFailedToLoad(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_FLIPPING,
                            zError = TrueError(
                                zMessage = loadAdError.message,
                                zCode = loadAdError.code,
                                zDomain = loadAdError.domain,
                            ),
                            zNativeView = zNativeBannerFlippingView,
                            zIsWithFallback = zIsWithFallback
                        )
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        zAdCallbacks?.zAdClosed(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_FLIPPING
                        )
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        zAdCallbacks?.zNativeAdOpened(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_FLIPPING
                        )
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        zAdCallbacks?.zAdLoaded(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_FLIPPING
                        )

                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        zAdCallbacks?.zAdClicked(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_FLIPPING
                        )

                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        zAdCallbacks?.zAdImpression(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_FLIPPING
                        )
                    }
                })
                .build()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    Log.d(TAG, "Ad Id Is: : $prefNameFlippingNativeBanner")
                    adLoader.loadAd(AdRequest.Builder().build())
                },
                0
            )
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    fun zShowNativeBannerSimple(
        zNativeBannerFlippingView: TrueZNativeBannerSimpleView,
        nativeAdvancedId: String,
        zIsWithFallback: Boolean = true
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            if (nativeAdvancedId.contains("/")) {
                prefNameNativeBanner =
                    nativeAdvancedId.substring(nativeAdvancedId.lastIndexOf("/") + 1)
            }
            val adLoader = AdLoader.Builder(
                zContext!!,
                nativeAdvancedId
            )
                .forNativeAd { NativeAd: NativeAd? ->
                    val cd = ColorDrawable()
                    val styles =
                        TrueNativeTemplateStyle.Builder().withMainBackgroundColor(cd).build()
                    AdmobNativeBannerLayoutSimpleBinding.inflate(
                        LayoutInflater.from(zContext),
                        null,
                        false
                    ).apply {
                        myTemplate.visibility = View.VISIBLE
                        myTemplate.setStyles(styles)
                        NativeAd?.let {
                            myTemplate.setNativeAd(it, false)
                        }
                        zNativeBannerFlippingView.zShowAdView(viewGroup = root)
                    }
                }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        zNativeBannerFlippingView.zShowHideAdLoader(true)
                        zAdCallbacks?.zAdFailedToLoad(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_SIMPLE,
                            zError = TrueError(
                                zMessage = loadAdError.message,
                                zCode = loadAdError.code,
                                zDomain = loadAdError.domain,
                            ),
                            zNativeView = zNativeBannerFlippingView,
                            zIsWithFallback = zIsWithFallback
                        )
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        zAdCallbacks?.zAdClosed(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_SIMPLE
                        )
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        zAdCallbacks?.zNativeAdOpened(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_SIMPLE
                        )
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        zAdCallbacks?.zAdLoaded(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_SIMPLE
                        )

                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        zAdCallbacks?.zAdClicked(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_SIMPLE
                        )

                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        zAdCallbacks?.zAdImpression(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_BANNER_SIMPLE
                        )
                    }
                })
                .build()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    adLoader.loadAd(AdRequest.Builder().build())
                }, 0
            )
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    fun zShowBanner(
        context: Context,
        zBannerView: TrueZBannerView,
        bannerId: String,
        zIsWithFallback: Boolean = true
    ) {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                if (bannerId.contains("/")) {
                    prefName = bannerId.substring(bannerId.lastIndexOf("/") + 1)
                }
                if (zContext != null) {
                    zBannerAdView = AdView(zContext)
                    zBannerAdView!!.adUnitId = bannerId
                    val adSize = adaptiveBannerAdSize
                    zBannerView.layoutParams.height =
                        zGetPixelFromDp(zContext, 60)
                    zAddPlaceHolderTextView(zBannerView)
                    /*zBannerAdView?.ad = adSize*/
                    zBannerAdView?.adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            zAdCallbacks?.zAdLoaded(
                                zAdType = TrueAdsType.Z_ADMOB,
                                zWhatAd = TrueWhatAd.Z_BANNER
                            )
                            if (zBannerAdView!!.parent != null) {
                                (zBannerAdView!!.parent as ViewGroup).removeView(zBannerAdView)
                            }
                            val cd = ColorDrawable()
                            val styles =
                                TrueBannerTemplateStyle.Builder().withMainBackgroundColor(cd)
                                    .build()
                            AdmobBannerLayoutBinding.inflate(
                                LayoutInflater.from(zContext),
                                null,
                                false
                            ).apply {
                                myTemplate.visibility = View.VISIBLE
                                myTemplate.setStyles(styles)

                                zBannerView.zShowAdView(viewGroup = root)
                            }
                            zBannerView.addView(zBannerAdView)
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            super.onAdFailedToLoad(loadAdError)
                            zBannerView.zShowHideAdLoader(true)
                            zAdCallbacks?.zAdFailedToLoad(
                                zAdType = TrueAdsType.Z_ADMOB,
                                zError = TrueError(
                                    zMessage = loadAdError.message,
                                    zCode = loadAdError.code,
                                    zDomain = loadAdError.domain,
                                ),
                                zNativeView = zBannerView,
                                zWhatAd = TrueWhatAd.Z_BANNER,
                                zIsWithFallback = zIsWithFallback,
                            )

                        }

                        override fun onAdClosed() {
                            zAdCallbacks?.zAdClosed(
                                zAdType = TrueAdsType.Z_ADMOB,
                                zWhatAd = TrueWhatAd.Z_BANNER
                            )
                        }

                        override fun onAdOpened() {
                            zAdCallbacks?.zNativeAdOpened(
                                zAdType = TrueAdsType.Z_ADMOB,
                                zWhatAd = TrueWhatAd.Z_BANNER
                            )

                        }

                        override fun onAdClicked() {
                            zAdCallbacks?.zAdClicked(
                                zAdType = TrueAdsType.Z_ADMOB,
                                zWhatAd = TrueWhatAd.Z_BANNER
                            )
                        }

                        override fun onAdImpression() {
                            zAdCallbacks?.zAdImpression(
                                zAdType = TrueAdsType.Z_ADMOB,
                                zWhatAd = TrueWhatAd.Z_BANNER
                            )
                        }
                    }
                    val adRequest = AdRequest.Builder().build()
                    /** Check if Ad is Banned*/
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            zBannerAdView!!.loadAd(adRequest)
                        },
                        0
                    )
                }
            }
        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
    }

    private fun zAddPlaceHolderTextView(adContainerView: ViewGroup?) {
        val valueTV = TextView(zContext)
        valueTV.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        valueTV.gravity = Gravity.CENTER
        adContainerView!!.addView(valueTV)
    }


    @SuppressLint("BinaryOperationInTimber")
    fun zShowNativeAdvanced(
        context: Context,
        zNativeAdvancedView: TrueZNativeAdvancedView,
        nativeAdvancedId: String,
        zIsWithFallback: Boolean = true
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            if (nativeAdvancedId.contains("/")) {
                prefNameNative =
                    nativeAdvancedId.substring(nativeAdvancedId.lastIndexOf("/") + 1)
            }
            val adLoader = AdLoader.Builder(
                zContext!!,
                nativeAdvancedId
            )
                .forNativeAd { unifiedNativeAd: NativeAd? ->
                    val cd = ColorDrawable()
                    val styles =
                        TrueNativeTemplateStyle.Builder().withMainBackgroundColor(cd).build()
                    AdmobNativeAdvancedLayoutBinding.inflate(
                        LayoutInflater.from(zContext),
                        null,
                        false
                    ).apply {
                        myTemplate.visibility = View.VISIBLE
                        myTemplate.setStyles(styles)
                        unifiedNativeAd?.let {
                            myTemplate.setNativeAd(it, false)
                        }
                        zNativeAdvancedView.zShowAdView(viewGroup = root)
                    }

                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        zNativeAdvancedView.zShowHideAdLoader(true)
                        zAdCallbacks?.zAdFailedToLoad(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED,
                            zError = TrueError(
                                zMessage = loadAdError.message,
                                zCode = loadAdError.code,
                                zDomain = loadAdError.domain,
                            ),
                            zNativeView = zNativeAdvancedView,
                            zIsWithFallback = zIsWithFallback
                        )
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        zAdCallbacks?.zAdClosed(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                        )
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        zAdCallbacks?.zNativeAdOpened(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                        )
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        zAdCallbacks?.zAdLoaded(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                        )

                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        zAdCallbacks?.zAdClicked(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                        )

                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        zAdCallbacks?.zAdImpression(
                            zAdType = TrueAdsType.Z_ADMOB,
                            zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                        )
                    }
                })
                .build()
            /** It will be executed when its true*/
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    adLoader.loadAd(AdRequest.Builder().build())
                    /**delay(TruePrefUtils.getInstance().init(context, prefNameNative).delayMs)*/
                },
                0
            )
        }
    }

    fun zSetInterCallbacks(interCallbacks: TrueInterCallbacks) {
        zInterCallbacks = interCallbacks
    }

    fun zSetNativeCallbacks(adCallbacks: TrueAdCallbacks) {
        zAdCallbacks = adCallbacks
    }


    init {
        zContext?.let { context ->
            MobileAds.initialize(context) { initializationStatus ->
                Timber.d("Ad Mob Initialization status ${initializationStatus.adapterStatusMap}")
            }
        }
    }

    /**Adaptive Banner Size*/
    @Suppress("DEPRECATION")
    private val adaptiveBannerAdSize: AdSize
        get() {
            val display =
                (zContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val widthPixels = outMetrics.widthPixels.toFloat()
            val density = outMetrics.density
            val adWidth = (widthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(zContext, adWidth)
        }


    /**Load Ads Dialogue*/
    private fun loadAds(activity: Activity) {
        val loadAdsLayoutBinding: LoadAdsLayoutBinding =
            LoadAdsLayoutBinding.inflate(activity.layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(loadAdsLayoutBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        loadAdsLayoutBinding.tvMessage.text =
            TrueAdManager.context.resources.getString(R.string.loading_ad)
    }

    /**Load And Show Native In Advance*/
    fun loadAdmobNativeInAdvance(
        context: Context,
        nativeAdvancedId: String,
    ) {
        val builder = AdLoader.Builder(
            context, nativeAdvancedId
        )
        builder.forNativeAd { nativeAd ->
            if (mAdmobNative != null) {
                mAdmobNative!!.destroy()
            }
            mAdmobNative = nativeAd
        }

        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()
        val adOptions: NativeAdOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()
        builder.withNativeAdOptions(adOptions)
        admobNativeAdLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
            }

            override fun onAdClosed() {
                super.onAdClosed()
                zAdCallbacks?.zAdClosed(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }

            override fun onAdOpened() {
                super.onAdOpened()
                zAdCallbacks?.zNativeAdOpened(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                zAdCallbacks?.zAdLoaded(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )

            }

            override fun onAdClicked() {
                super.onAdClicked()
                zAdCallbacks?.zAdClicked(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )

            }

            override fun onAdImpression() {
                super.onAdImpression()
                zAdCallbacks?.zAdImpression(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }
        }).build()
        /** It will be executed when its true*/
        Handler(Looper.getMainLooper()).postDelayed(
            {
                admobNativeAdLoader!!.loadAd(AdRequest.Builder().build())
            }, 0
        )
    }

    private fun inflateAdNativeAdInAdvance(
        context: Activity,
        nativeAd: NativeAd,
        zNativeAdvancedView: TrueZNativeAdvancedView
    ) {
        val cd = ColorDrawable()
        val styles =
            TrueNativeTemplateStyle.Builder().withMainBackgroundColor(cd).build()
        AdmobNativeAdvancedLayoutBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        ).apply {
            myTemplate.visibility = View.VISIBLE
            myTemplate.setStyles(styles)
            nativeAd.let {
                myTemplate.setNativeAd(it, false)
            }
            zNativeAdvancedView.zShowAdView(viewGroup = root)
        }
    }

    fun showAdmobNativeInAdvance(
        context: Activity,
        nativeAdId: String,
        zNativeAdvancedView: TrueZNativeAdvancedView,
    ) {
        if (admobNativeAdLoader != null && !admobNativeAdLoader!!.isLoading) {
            if (nativeAdvanceBooleanValue) {
                zNativeAdvancedView.visibility = View.GONE
            }
            if (mAdmobNative != null) {
                inflateAdNativeAdInAdvance(context, mAdmobNative!!, zNativeAdvancedView)
            }
        } else {
            zNativeAdvancedView.visibility = View.GONE
        }
    }

    /**Load And Show Flipping Native In Advance*/
    fun loadAdmobFlippingNativeInAdvance(
        context: Context,
        nativeAdvancedId: String,
    ) {
        if (nativeAdvancedId.contains("/")) {
            prefNameFlippingNativeInAdvanced =
                nativeAdvancedId.substring(nativeAdvancedId.lastIndexOf("/") + 1)
        }
        val builder = AdLoader.Builder(
            context, nativeAdvancedId
        )
        builder.forNativeAd { nativeAd ->
            if (mFlippingAdmobNative != null) {
                mFlippingAdmobNative!!.destroy()
            }
            mFlippingAdmobNative = nativeAd
        }

        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()
        val adOptions: NativeAdOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()
        builder.withNativeAdOptions(adOptions)
        mFlippingAdmobNativeAdLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
            }

            override fun onAdClosed() {
                super.onAdClosed()
                zAdCallbacks?.zAdClosed(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }

            override fun onAdOpened() {
                super.onAdOpened()
                zAdCallbacks?.zNativeAdOpened(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                zAdCallbacks?.zAdLoaded(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )

            }

            override fun onAdClicked() {
                super.onAdClicked()
                zAdCallbacks?.zAdClicked(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )

            }

            override fun onAdImpression() {
                super.onAdImpression()
                zAdCallbacks?.zAdImpression(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }
        }).build()
        /** It will be executed when its true*/
        Handler(Looper.getMainLooper()).postDelayed(
            {
                mFlippingAdmobNativeAdLoader!!.loadAd(AdRequest.Builder().build())
            },
            0
        )
    }

    private fun inflateFlippingNativeAdInAdvance(
        nativeAd: NativeAd,
        trueZNativeBannerSimpleView: TrueZNativeBannerFlippingView
    ) {
        val cd = ColorDrawable()
        val styles =
            TrueNativeTemplateStyle.Builder().withMainBackgroundColor(cd).build()
        AdmobNativeBannerLayoutBinding.inflate(
            LayoutInflater.from(zContext),
            null,
            false
        ).apply {
            myTemplate.visibility = View.VISIBLE
            myTemplate.setStyles(styles)
            nativeAd.let {
                myTemplate.setNativeAd(it, true)
            }
            trueZNativeBannerSimpleView.zShowAdView(viewGroup = root)
        }
    }

    fun showAdmobFlippingNativeInAdvance(
        context: Activity,
        nativeAdId: String,
        trueZNativeBannerSimpleView: TrueZNativeBannerFlippingView,
    ) {
        if (mFlippingAdmobNativeAdLoader != null && !mFlippingAdmobNativeAdLoader!!.isLoading) {
            if (floatingNativeBooleanValue) {
                trueZNativeBannerSimpleView.visibility = View.GONE
            }
            if (mFlippingAdmobNative != null) {
                inflateFlippingNativeAdInAdvance(
                    mFlippingAdmobNative!!,
                    trueZNativeBannerSimpleView
                )
            }
        } else {
            trueZNativeBannerSimpleView.visibility = View.GONE

        }
    }

    /**Load And Show Simple Native In Advance*/
    fun loadAdmobSimpleNativeInAdvance(
        context: Context,
        nativeAdvancedId: String,
    ) {
        if (nativeAdvancedId.contains("/")) {
            prefNameSimpleNativeInAdvanced =
                nativeAdvancedId.substring(nativeAdvancedId.lastIndexOf("/") + 1)
        }
        val builder = AdLoader.Builder(
            context, nativeAdvancedId
        )
        builder.forNativeAd { nativeAd ->
            if (mSimpleAdmobNative != null) {
                mSimpleAdmobNative!!.destroy()
            }
            mSimpleAdmobNative = nativeAd
        }
        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()
        val adOptions: NativeAdOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()
        builder.withNativeAdOptions(adOptions)
        mSimpleAdmobNativeAdLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
            }

            override fun onAdClosed() {
                super.onAdClosed()
                zAdCallbacks?.zAdClosed(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }

            override fun onAdOpened() {
                super.onAdOpened()
                zAdCallbacks?.zNativeAdOpened(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                zAdCallbacks?.zAdLoaded(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }

            override fun onAdClicked() {
                super.onAdClicked()
                zAdCallbacks?.zAdClicked(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }

            override fun onAdImpression() {
                super.onAdImpression()
                zAdCallbacks?.zAdImpression(
                    zAdType = TrueAdsType.Z_ADMOB,
                    zWhatAd = TrueWhatAd.Z_NATIVE_ADVANCED
                )
            }
        }).build()
        /** It will be executed when its true*/
        Handler(Looper.getMainLooper()).postDelayed(
            {
                mSimpleAdmobNativeAdLoader!!.loadAd(AdRequest.Builder().build())
            },
            0
        )
    }

    private fun inflateSimpleNativeAdInAdvance(
        nativeAd: NativeAd,
        trueZNativeBannerSimpleView: TrueZNativeBannerSimpleView
    ) {
        val cd = ColorDrawable()
        val styles =
            TrueNativeTemplateStyle.Builder().withMainBackgroundColor(cd).build()
        AdmobNativeBannerLayoutSimpleBinding.inflate(
            LayoutInflater.from(zContext),
            null,
            false
        ).apply {
            myTemplate.visibility = View.VISIBLE
            myTemplate.setStyles(styles)
            nativeAd.let {
                myTemplate.setNativeAd(it, false)
            }
            trueZNativeBannerSimpleView.zShowAdView(viewGroup = root)
        }
    }

    fun showAdmobSimpleNativeInAdvance(
        context: Activity,
        nativeAdId: String,
        trueZNativeBannerSimpleView: TrueZNativeBannerSimpleView,
    ) {
        if (mSimpleAdmobNativeAdLoader != null && !mSimpleAdmobNativeAdLoader!!.isLoading) {
            if (simpleNativeBooleanValue) {
                trueZNativeBannerSimpleView.visibility = View.GONE
            }
            if (mSimpleAdmobNative != null) {
                inflateSimpleNativeAdInAdvance(
                    mSimpleAdmobNative!!,
                    trueZNativeBannerSimpleView
                )
            }
        } else {
            trueZNativeBannerSimpleView.visibility = View.GONE
        }
    }

}