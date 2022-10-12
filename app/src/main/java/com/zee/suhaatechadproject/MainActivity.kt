package com.zee.suhaatechadproject

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.zee.suhaatechadslibmodule.mediation.TrueAdManager
import com.zee.suhaatechadslibmodule.mediation.TrueAdManager.zInitializeAds

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        zInitializeAds(this)
        MobileAds.initialize(
            this
        ) { initializationStatus: InitializationStatus? -> }
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            TrueAdManager.zLoadAndShowInterstitialAdWithIntent(
                this,
                "ca-app-pub-3940256099942544/1033173712",
                SecondActivity::class.java
            )
        }, 3000)
    }
}