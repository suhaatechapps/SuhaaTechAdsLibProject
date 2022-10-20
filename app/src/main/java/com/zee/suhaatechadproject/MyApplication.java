package com.zee.suhaatechadproject;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.zee.suhaatechadslibmodule.limits.TrueAdLimitationObj;
import com.zee.suhaatechadslibmodule.mediation.TrueAdManager;
import com.zee.suhaatechadslibmodule.mediation.TrueConstants;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TrueAdManager.INSTANCE.zInitializeAds(this);
        MobileAds.initialize(this, initializationStatus -> {

        });
        try {
            if (TrueConstants.INSTANCE.isNetworkAvailable(TrueAdManager.context) &&
                    TrueConstants.INSTANCE.isNetworkSpeedHigh()) {
                TrueAdLimitationObj.INSTANCE.limitationFun(this, "ca-app-pub-3940256099942544/1033173712", true, true, 4, 3, 0, 1, true);
            }
        } catch (Exception ignored) {
        }
    }
}