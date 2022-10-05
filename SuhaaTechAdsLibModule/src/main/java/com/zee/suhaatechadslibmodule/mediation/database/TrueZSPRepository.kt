package com.zee.suhaatechadslibmodule.mediation.database

import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences

object TrueZSPRepository {
    private const val INDEX_VALUE = "INDEX_VALUE"
    private const val AD_INTER_COUNTING_VALUE = "AD_INTER_COUNTING_VALUE"
    private const val FB_AD_INTER_COUNTING_VALUE = "FB_AD_INTER_COUNTING_VALUE"
    private const val ADMOB_AVAILABLE_VALUE = "ADMOB_AVAILABLE_VALUE"
    private const val SUBSCRIPTION_PLAN_VALUE = "SUBSCRIPTION_PLAN_VALUE"
    private const val OPEN_APP_AD_KEY = "OPEN_APP_AD_KEY"
    private const val INTER_AD_VALUE = "INTER_AD_VALUE"

    /**Get Interstitial Ad Counter Value*/
    fun setAdInterCountValue(context: Context, adCountValue: Int) {
        getDefaultSharedPreferences(context).edit().putInt(AD_INTER_COUNTING_VALUE, adCountValue)
            .apply()
    }

    fun getAdInterCountValue(context: Context): Int {
        return getDefaultSharedPreferences(context).getInt(AD_INTER_COUNTING_VALUE, 0)
    }

    /**Get Subscription Value*/
    fun setSubscription(context: Context, value: Boolean) {
        getDefaultSharedPreferences(context).edit()
            .putBoolean(SUBSCRIPTION_PLAN_VALUE, value)
            .apply()
    }

    fun getSubscription(context: Context): Boolean {
        return getDefaultSharedPreferences(context).getBoolean(SUBSCRIPTION_PLAN_VALUE, false)
    }

    /**Get Open Ad Value*/
    fun getOpenAdValue(context: Context): Boolean {
        return getDefaultSharedPreferences(context).getBoolean(OPEN_APP_AD_KEY, false)
    }


    fun saveOpenAdValue(context: Context, openAdValue: Boolean) {
        getDefaultSharedPreferences(context).edit().putBoolean(
            OPEN_APP_AD_KEY, openAdValue
        ).apply()
    }

    /**Get Inter Ad Value*/
    fun getInterAdValue(context: Context): Int {
        return getDefaultSharedPreferences(context).getInt(INTER_AD_VALUE, 0)
    }

    fun saveInterAdValue(context: Context, interCounterValue: Int) {
        getDefaultSharedPreferences(context).edit().putInt(
            INTER_AD_VALUE, interCounterValue
        ).apply()
    }

    /**Get Interstitial Ad Counter Value*/
    fun setAdAvailableValue(context: Context, AdAvailableValue: Boolean) {
        getDefaultSharedPreferences(context).edit()
            .putBoolean(AD_INTER_COUNTING_VALUE, AdAvailableValue)
            .apply()
    }

    fun getIfAdAvailable(context: Context): Boolean {
        return getDefaultSharedPreferences(context).getBoolean(AD_INTER_COUNTING_VALUE, false)
    }

    /**Get Interstitial Ad Counter Value*/
    fun setFBAdAvailableValue(context: Context, AdAvailableValue: Boolean) {
        getDefaultSharedPreferences(context).edit()
            .putBoolean(FB_AD_INTER_COUNTING_VALUE, AdAvailableValue)
            .apply()
    }

    fun getIfFBAdAvailable(context: Context): Boolean {
        return getDefaultSharedPreferences(context).getBoolean(FB_AD_INTER_COUNTING_VALUE, false)
    }


    /**Get Interstitial Ad Counter Value*/
    fun setAdMobAvailable(context: Context, AdAvailableValue: Boolean) {
        getDefaultSharedPreferences(context).edit()
            .putBoolean(ADMOB_AVAILABLE_VALUE, AdAvailableValue)
            .apply()
    }

    fun getIfAdmobAvailable(context: Context): Boolean {
        return getDefaultSharedPreferences(context).getBoolean(ADMOB_AVAILABLE_VALUE, false)
    }
}