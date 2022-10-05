package com.zee.suhaatechadslibmodule.mediation.customadview

import android.content.Context
import androidx.core.content.ContextCompat
import com.zee.suhaatechadslibmodule.R

import com.zee.suhaatechadslibmodule.mediation.hDp


data class TrueStroke(
    private val zContext: Context,
    var zColor: Int = ContextCompat.getColor(
        zContext,
        R.color.colorPrimaryDark
    ),
    var zWidth: Int = 1,
) {
    init {
        zWidth = hDp(zContext, zWidth).toInt()
    }
}

