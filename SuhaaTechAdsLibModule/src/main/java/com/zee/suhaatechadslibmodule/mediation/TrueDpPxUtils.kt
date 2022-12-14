package com.zee.suhaatechadslibmodule.mediation

import android.content.Context
import android.util.TypedValue

fun hDp(context: Context, zCornerRadius: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        zCornerRadius.toFloat(),
        context.resources.displayMetrics
    )
}