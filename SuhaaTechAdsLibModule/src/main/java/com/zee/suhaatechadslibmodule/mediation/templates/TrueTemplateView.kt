package com.zee.suhaatechadslibmodule.mediation.templates

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.zee.suhaatechadslibmodule.R


class TrueTemplateView : FrameLayout {
    private var templateType = 0
    private var styles: TrueNativeTemplateStyle? = null
    private var nativeAd: NativeAd? = null
    private var nativeAdView: NativeAdView? = null
        private set
    private var primaryView: TextView? = null
    private var secondaryView: TextView? = null
    private var tertiaryView: TextView? = null
    private var iconView: ImageView? = null
    private var mediaView: MediaView? = null
    private var callToActionView: Button? = null
    private var background: ConstraintLayout? = null
    private var adsL1: LinearLayout? = null
    private var adsL2: LinearLayout? = null
    private var i: Int = 0

    constructor(context: Context?) : super(context!!)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView(context, attrs)
    }

    fun setStyles(styles: TrueNativeTemplateStyle?) {
        this.styles = styles
        applyStyles()
    }

    private fun applyStyles() {
        val mainBackground: Drawable? = styles!!.mainBackgroundColor
        if (mainBackground != null) {
            background!!.background = mainBackground
            if (primaryView != null) {
                primaryView!!.background = mainBackground
            }
            if (secondaryView != null) {
                secondaryView!!.background = mainBackground
            }
            if (tertiaryView != null) {
                tertiaryView!!.background = mainBackground
            }
        }
        val primary = styles!!.primaryTextTypeface
        if (primary != null && primaryView != null) {
            primaryView!!.typeface = primary
        }
        val secondary = styles!!.secondaryTextTypeface
        if (secondary != null && secondaryView != null) {
            secondaryView!!.typeface = secondary
        }
        val tertiary = styles!!.tertiaryTextTypeface
        if (tertiary != null && tertiaryView != null) {
            tertiaryView!!.typeface = tertiary
        }
        val ctaTypeface = styles!!.callToActionTextTypeface
        if (ctaTypeface != null && callToActionView != null) {
            callToActionView!!.typeface = ctaTypeface
        }
        val primaryTypefaceColor = styles!!.primaryTextTypefaceColor
        if (primaryTypefaceColor > 0 && primaryView != null) {
            primaryView!!.setTextColor(primaryTypefaceColor)
        }
        val secondaryTypefaceColor = styles!!.secondaryTextTypefaceColor
        if (secondaryTypefaceColor > 0 && secondaryView != null) {
            secondaryView!!.setTextColor(secondaryTypefaceColor)
        }
        val tertiaryTypefaceColor = styles!!.tertiaryTextTypefaceColor
        if (tertiaryTypefaceColor > 0 && tertiaryView != null) {
            tertiaryView!!.setTextColor(tertiaryTypefaceColor)
        }
        val ctaTypefaceColor = styles!!.callToActionTypefaceColor
        if (ctaTypefaceColor > 0 && callToActionView != null) {
            callToActionView!!.setTextColor(ctaTypefaceColor)
        }
        val ctaTextSize = styles!!.callToActionTextSize
        if (ctaTextSize > 0 && callToActionView != null) {
            callToActionView!!.textSize = ctaTextSize
        }
        val primaryTextSize = styles!!.primaryTextSize
        if (primaryTextSize > 0 && primaryView != null) {
            primaryView!!.textSize = primaryTextSize
        }
        val secondaryTextSize = styles!!.secondaryTextSize
        if (secondaryTextSize > 0 && secondaryView != null) {
            secondaryView!!.textSize = secondaryTextSize
        }
        val tertiaryTextSize = styles!!.tertiaryTextSize
        if (tertiaryTextSize > 0 && tertiaryView != null) {
            tertiaryView!!.textSize = tertiaryTextSize
        }
        val ctaBackground: Drawable? = styles!!.callToActionBackgroundColor
        if (ctaBackground != null && callToActionView != null) {
            callToActionView!!.background = ctaBackground
        }
        val primaryBackground: Drawable? = styles!!.primaryTextBackgroundColor
        if (primaryBackground != null && primaryView != null) {
            primaryView!!.background = primaryBackground
        }
        val secondaryBackground: Drawable? = styles!!.secondaryTextBackgroundColor
        if (secondaryBackground != null && secondaryView != null) {
            secondaryView!!.background = secondaryBackground
        }
        val tertiaryBackground: Drawable? = styles!!.tertiaryTextBackgroundColor
        if (tertiaryBackground != null && tertiaryView != null) {
            tertiaryView!!.background = tertiaryBackground
        }
        invalidate()
        requestLayout()
    }

    private fun adHasOnlyStore(nativeAd: NativeAd): Boolean {
        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
    }

    fun setNativeAd(nativeAd: NativeAd, adsValue: Boolean) {
        this.nativeAd = nativeAd
        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        val headline = nativeAd.headline
        val body = nativeAd.body
        val cta = nativeAd.callToAction
        nativeAd.starRating
        val icon = nativeAd.icon
        val secondaryText: String?
        nativeAdView!!.callToActionView = callToActionView
        nativeAdView!!.headlineView = primaryView
        nativeAdView!!.mediaView = mediaView
        secondaryView!!.visibility = VISIBLE
        if (adHasOnlyStore(nativeAd)) {
            nativeAdView!!.storeView = secondaryView
            secondaryText = store
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView!!.advertiserView = secondaryView
            secondaryText = advertiser
        } else {
            secondaryText = ""
        }
        primaryView!!.text = headline
        callToActionView!!.text = cta

        /**Set the secondary view to be the star rating if available.*/
        if (secondaryText == null) {
            secondaryView!!.visibility = GONE
        } else {
            secondaryView!!.text = secondaryText
            secondaryView!!.visibility = VISIBLE
        }
        if (icon != null) {
            iconView!!.visibility = VISIBLE
            iconView!!.setImageDrawable(icon.drawable)
        } else {
            iconView!!.visibility = GONE
        }
        if (tertiaryView != null) {
            tertiaryView!!.text = body
            nativeAdView!!.bodyView = tertiaryView
        }
        nativeAdView!!.setNativeAd(nativeAd)
        if (adsValue) {
            setFlipping()
            scrollMediaView(mediaView!!)
        }

    }

    /**
     * To prevent memory leaks, make sure to destroy your ad when you don't need it anymore. This
     * method does not destroy the template view.
     * https://developers.google.com/admob/android/native-unified#destroy_ad
     */
    fun destroyNativeAd() {
        nativeAd!!.destroy()
    }

    val templateTypeName: String
        get() {
            if (templateType == R.layout.gnt_medium_template_view) {
                return MEDIUM_TEMPLATE
            } else if (templateType == R.layout.gnt_small_template_view) {
                return SMALL_TEMPLATE
            }
            return ""
        }

    private fun initView(context: Context, attributeSet: AttributeSet?) {
        val attributes =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.TemplateView, 0, 0)
        templateType = try {
            attributes.getResourceId(
                R.styleable.TemplateView_gnt_template_type, R.layout.gnt_medium_template_view
            )
        } finally {
            attributes.recycle()
        }
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(templateType, this)
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()
        nativeAdView = findViewById<View>(R.id.native_ad_view) as NativeAdView
        primaryView = findViewById<View>(R.id.primary) as TextView
        secondaryView = findViewById<View>(R.id.secondary) as TextView
        tertiaryView = findViewById<View>(R.id.body) as TextView
        callToActionView = findViewById<View>(R.id.cta) as Button
        iconView = findViewById<View>(R.id.icon) as ImageView
        mediaView = findViewById<View>(R.id.media_view) as MediaView
        background = findViewById<View>(R.id.background) as ConstraintLayout
        adsL1 = findViewById<View>(R.id.adsL1) as LinearLayout
        adsL2 = findViewById<View>(R.id.adsL2) as LinearLayout
    }

    companion object {
        private const val MEDIUM_TEMPLATE = "medium_template"
        private const val SMALL_TEMPLATE = "small_template"
    }

    fun setFlipping() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (i % 2 == 0) {
                    flipCard(context, adsL2!!, adsL1!!)
                    i++
                    handler.postDelayed(this, 6000)
                } else {
                    flipCard(context, adsL1!!, adsL2!!)
                    i++
                    handler.postDelayed(this, 8000)
                }
            }
        }
        handler.postDelayed(runnable, 8000)
    }

    fun flipCard(context: Context, visibleView: View, inVisibleView: View) {
        try {
            visibleView.visibility = VISIBLE
            val flipOutAnimatorSet =
                AnimatorInflater.loadAnimator(
                    context,
                    R.animator.flip_out
                ) as AnimatorSet
            flipOutAnimatorSet.setTarget(inVisibleView)
            val flipInAnimationSet =
                AnimatorInflater.loadAnimator(
                    context,
                    R.animator.flip_in
                ) as AnimatorSet
            flipInAnimationSet.setTarget(visibleView)
            flipOutAnimatorSet.start()
            flipInAnimationSet.start()
            flipInAnimationSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationEnd(animation: Animator) {
                    inVisibleView.visibility = GONE
                }

                override fun onAnimationCancel(animation: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    TODO("Not yet implemented")
                }
            })
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun scrollMediaView(mediaView: MediaView) {
        val mediaViewAnimation: Animation
        mediaViewAnimation = TranslateAnimation(
            TranslateAnimation.ABSOLUTE,
            0f,
            TranslateAnimation.ABSOLUTE,
            0f,
            TranslateAnimation.ABSOLUTE,
            0f,
            TranslateAnimation.ABSOLUTE,
            -100f
        )
        mediaViewAnimation.setDuration(4000)
        mediaViewAnimation.setRepeatCount(-1)
        mediaViewAnimation.setRepeatMode(Animation.REVERSE) // REVERSE

        mediaViewAnimation.setInterpolator(LinearInterpolator())
        mediaView.startAnimation(mediaViewAnimation)
    }
}