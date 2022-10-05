package com.zee.suhaatechadslibmodule.facebook

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.facebook.ads.*
import com.zee.suhaatechadslibmodule.R
import com.zee.suhaatechadslibmodule.databinding.LoadAdsLayoutBinding
import com.zee.suhaatechadslibmodule.mediation.TrueAdManager
import com.zee.suhaatechadslibmodule.mediation.TrueError
import com.zee.suhaatechadslibmodule.mediation.callbacks.TrueAdCallbacks
import com.zee.suhaatechadslibmodule.mediation.callbacks.TrueInterCallbacks
import com.zee.suhaatechadslibmodule.mediation.database.TrueZSPRepository
import com.zee.suhaatechadslibmodule.mediation.interfaces.TrueAdCallBackInterface
import com.zee.suhaatechadslibmodule.mediation.types.TrueAdsType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FaceBookAdManager(private var context: Context) {
    private var TAG = "FaceBookAdManagerClass"
    private var hNativeBanner: NativeBannerAd? = null
    private var zFbInterstitialAd: InterstitialAd? = null
    private var hFbBanner: AdView? = null
    private var nativeAd: NativeAd? = null
    private var zInterCallbacks: TrueInterCallbacks? = null
    private var hAdCallbacks: TrueAdCallbacks? = null
    lateinit var dialog: Dialog
    private var prefNameFBInter: String? = null

    fun hGetFbInterstitialAd(): InterstitialAd? {
        return zFbInterstitialAd
    }

    fun loadFbInterstitialAds(
        context: Activity,
        interId: String
    ) {
        if (zFbInterstitialAd != null) {
            zFbInterstitialAd?.destroy()
            zFbInterstitialAd = null
        }
        dialog = Dialog(context)
        loadAds(context)
        Log.d(TAG, "Ads Id: " + interId)
        if (interId.contains("_")) {
            prefNameFBInter = interId.substring(interId.lastIndexOf("_") + 1)
        }
        Log.d(TAG, "Prefer Name Inter: " + prefNameFBInter)
        CoroutineScope(Dispatchers.Main).launch {
            var zCallBackCalled = false
                dialog.show()
                /** it will be executed when its true*/
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        object : InterstitialAdExtendedListener {
                            override fun onInterstitialActivityDestroyed() {}

                            override fun onInterstitialDisplayed(ad: Ad?) {
                                zInterCallbacks?.zOnAddShowed(TrueAdsType.Z_FACEBOOK)
                                Log.d(TAG, "onInterstitialDisplayed: " + ad)
                            }

                            override fun onInterstitialDismissed(ad: Ad?) {
                                zInterCallbacks?.zOnAddDismissed(TrueAdsType.Z_FACEBOOK)
                                Log.d(TAG, "onInterstitialDismissed: " + ad)
                            }

                            override fun onError(ad: Ad?, adError: AdError?) {
                                zInterCallbacks?.zOnAdFailedToLoad(
                                    TrueAdsType.Z_FACEBOOK,
                                    zError = TrueError(
                                        zMessage = adError?.errorMessage,
                                        zCode = adError?.errorCode,
                                    ),
                                )
                                Toast.makeText(
                                    context,
                                    "Error: " + adError!!.errorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(TAG, "onError: " + adError!!.errorMessage)
                            }

                            override fun onAdLoaded(ad: Ad?) {
                                dialog.dismiss()
                                zInterCallbacks?.zOnAddLoaded(TrueAdsType.Z_FACEBOOK)
                                zInterCallbacks?.zOnAddLoaded(zAdType = TrueAdsType.Z_ADMOB)
                                zCallBackCalled = true
                                zFbInterstitialAd!!.show()
                                Log.d(TAG, "onAdLoaded: " + ad)
                            }

                            override fun onAdClicked(ad: Ad?) {
                                Log.d(TAG, "onAdClicked: " + ad)
                            }

                            override fun onLoggingImpression(ad: Ad?) {
                                Log.d(TAG, "onLoggingImpression: " + ad)
                            }

                            override fun onRewardedAdCompleted() {}
                            override fun onRewardedAdServerSucceeded() {}
                            override fun onRewardedAdServerFailed() {}
                        }.also { listener ->
                            zFbInterstitialAd = InterstitialAd(
                                context,
                                interId
                            )
                            val loadAdConfig = zFbInterstitialAd!!
                                .buildLoadAdConfig()
                                .withAdListener(listener)
                                .build()
                            zFbInterstitialAd!!.loadAd(loadAdConfig)
                        }
                    }, 0
                )
        }
    }

    fun zLoadFBInterstitialAdWithCallBacks(
        context: Activity,
        interId: String,
        trueAdCallBackInterface: TrueAdCallBackInterface
    ) {
        dialog = Dialog(context)
        loadAds(context)
        if (interId.contains("_")) {
            prefNameFBInter = interId.substring(interId.lastIndexOf("_") + 1)
        }
        CoroutineScope(Dispatchers.Main).launch {
            var zCallBackCalled = false
                dialog.show()
                /** it will be executed when its true*/
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        object : InterstitialAdExtendedListener {
                            override fun onInterstitialActivityDestroyed() {}

                            override fun onInterstitialDisplayed(ad: Ad?) {
                                zInterCallbacks?.zOnAddShowed(TrueAdsType.Z_FACEBOOK)
                                Log.d(TAG, "onInterstitialDisplayed: " + ad)
                                TrueZSPRepository.setFBAdAvailableValue(
                                    context,
                                    true
                                )
                            }

                            override fun onInterstitialDismissed(ad: Ad?) {

                                zInterCallbacks?.zOnAddDismissed(TrueAdsType.Z_FACEBOOK)
                                trueAdCallBackInterface.onShowAdComplete()
                                TrueZSPRepository.setFBAdAvailableValue(
                                    context,
                                    true
                                )
                                Log.d(TAG, "onInterstitialDismissed: " + ad)
                            }

                            override fun onError(ad: Ad?, adError: AdError?) {
                                TrueZSPRepository.setFBAdAvailableValue(
                                    context,
                                    false
                                )
                                zInterCallbacks?.zOnAdFailedToLoad(
                                    TrueAdsType.Z_FACEBOOK,
                                    zError = TrueError(
                                        zMessage = adError?.errorMessage,
                                        zCode = adError?.errorCode,
                                    ),
                                )
                                dialog.dismiss()
                                Toast.makeText(
                                    context,
                                    "Error: " + adError!!.errorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(TAG, "onError: " + adError!!.errorMessage)
                            }

                            override fun onAdLoaded(ad: Ad?) {
                                TrueZSPRepository.setFBAdAvailableValue(context, true)
                                dialog.dismiss()
                                zInterCallbacks?.zOnAddLoaded(TrueAdsType.Z_FACEBOOK)
                                zInterCallbacks?.zOnAddLoaded(zAdType = TrueAdsType.Z_ADMOB)
                                zCallBackCalled = true
                                zFbInterstitialAd!!.show()
                                Log.d(TAG, "onAdLoaded: " + ad)
                            }

                            override fun onAdClicked(ad: Ad?) {
                                Log.d(TAG, "onAdClicked: " + ad)
                            }

                            override fun onLoggingImpression(ad: Ad?) {
                                Log.d(TAG, "onLoggingImpression: " + ad)
                            }

                            override fun onRewardedAdCompleted() {}
                            override fun onRewardedAdServerSucceeded() {}
                            override fun onRewardedAdServerFailed() {}
                        }.also { listener ->
                            zFbInterstitialAd = InterstitialAd(
                                context,
                                interId
                            )
                            val loadAdConfig = zFbInterstitialAd!!
                                .buildLoadAdConfig()
                                .withAdListener(listener)
                                .build()
                            zFbInterstitialAd!!.loadAd(loadAdConfig)
                        }
                    }, 0
                )
        }

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

}