package com.zee.suhaatechadproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.zee.suhaatechadproject.databinding.ActivityMainBinding
import com.zee.suhaatechadslibmodule.mediation.TrueAdManager
import com.zee.suhaatechadslibmodule.mediation.TrueAdManager.zInitializeAds
import com.zee.suhaatechadslibmodule.mediation.TrueConstants.isNetworkSpeedHigh

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        zInitializeAds(this)
        MobileAds.initialize(
            this
        ) { initializationStatus: InitializationStatus? -> }
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            TrueAdManager.zLoadInterstitialInAdvance(this, "ca-app-pub-3940256099942544/1033173712")
//            zLoadAndShowInterstitialAdWithIntent(this,"ca-app-pub-3940256099942544/1033173712", SecondActivity::class.java)
            Toast.makeText(this, "Network Speed: " + isNetworkSpeedHigh(), Toast.LENGTH_SHORT)
                .show()
            /* TrueAdManager.zLoadAndShowInterstitialAdWithIntent(
                 this,
                 "ca-app-pub-3940256099942544/1033173712",
                 SecondActivity::class.java
             )*/
            binding.updateBtn.visibility = View.VISIBLE
            binding.nextBtn.visibility = View.VISIBLE
            binding.mainPB.visibility = View.GONE
        }, 4000)
        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
        binding.updateBtn.setOnClickListener {
            startActivity(Intent(this, SetPrefValueAct::class.java))
        }
    }
}