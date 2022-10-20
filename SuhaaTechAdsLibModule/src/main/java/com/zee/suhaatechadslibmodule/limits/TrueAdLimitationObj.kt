package com.zee.suhaatechadslibmodule.limits

import android.content.Context

object TrueAdLimitationObj {
    lateinit var unitIdMain: String

    /**WorkAround for creating pref xml file as it doesn't support slash symbol .. so we get the after slash only */
    fun limitationFun(
        context: Context,
        unitId: String,
        limitActivated: Boolean,
        adActivated: Boolean,
        clicks: Int,
        impressions: Int,
        delayMs: Long,
        banHours: Int,
        hideOnClick: Boolean
    ) {
        unitIdMain = unitId
        if (unitIdMain.contains("/")) {
            unitIdMain = unitIdMain.substring(unitIdMain.lastIndexOf("/") + 1)
        } else if (unitIdMain.contains("_")) {
            unitIdMain = unitIdMain.substring(unitIdMain.lastIndexOf("_") + 1)
        }
        TruePrefUtils.getInstance().init(context, TruePrefUtils.PREF_NAME)
            .zUpdateNetworksData()
        TruePrefUtils.getInstance().init(context, unitIdMain).zUpdateUnitsData(
            limitActivated,
            adActivated,
            clicks,
            impressions,
            delayMs,
            banHours,
            hideOnClick
        )
    }
}