package ru.wearemad.fadeview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout

class FadeView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle) {

    //paint
    private val mTopPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }
    private val mBottomPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }
    //rect
    private val mRectTop = Rect()
    private val mRectBottom = Rect()
    //colors
    private val mColorsTop = intArrayOf(Color.TRANSPARENT, Color.BLACK)
    private val mColorsBottom = intArrayOf(Color.BLACK, Color.TRANSPARENT)
    //default size
    private val FADE_SIZE = 50.dpToPx
    //draw booleans
    private var mDrawTopGradient = true
    private var mDrawBottomGradient = true
    //size
    private var mTopSize = FADE_SIZE
    private var mBottomSize = FADE_SIZE
    //redraw flags
    private val TOP_FLAG = 1
    private val BOTTOM_FLAG = 2
    private var mDrawFlag = 0

    init {
        mDrawFlag = mDrawFlag or TOP_FLAG
        mDrawFlag = mDrawFlag or BOTTOM_FLAG
    }

    override fun dispatchDraw(canvas: Canvas?) {
        val newWidth = width
        val newHeight = height
        val drawFade = mDrawBottomGradient || mDrawTopGradient
        if (visibility == GONE || newWidth == 0 || newHeight == 0 || !drawFade) {
            if (!drawFade) {
                mDrawFlag = 0
            }
            super.dispatchDraw(canvas)
            return
        }
        if ((mDrawFlag and TOP_FLAG) == TOP_FLAG) {
            mDrawFlag = mDrawFlag and TOP_FLAG.inv()
            initTopGradient()
        }
        if ((mDrawFlag and BOTTOM_FLAG) == BOTTOM_FLAG) {
            mDrawFlag = mDrawFlag and BOTTOM_FLAG.inv()
            initBottomGradient()
        }
        val count = canvas?.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
            ?: 0
        super.dispatchDraw(canvas)
        if (mDrawTopGradient) {
            canvas?.drawRect(mRectTop, mTopPaint)
        }
        if (mDrawBottomGradient) {
            canvas?.drawRect(mRectBottom, mBottomPaint)
        }
        canvas?.restoreToCount(count)
    }

    private fun initTopGradient() {
        val actualHeight = height - paddingTop - paddingBottom
        val size = Math.min(mTopSize, actualHeight)
        val l = paddingLeft
        val t = paddingTop
        val r = width - paddingRight
        val b = t + size
        mRectTop.set(l, t, r, b)
        val gradient = LinearGradient(l.toFloat(), t.toFloat(), l.toFloat(), b.toFloat(), mColorsTop,
            null, Shader.TileMode.CLAMP)
        mTopPaint.shader = gradient
    }

    private fun initBottomGradient() {
        val actualHeight = height - paddingTop - paddingBottom
        val size = Math.min(mBottomSize, actualHeight)
        val l = paddingLeft
        val t = paddingTop + actualHeight - size
        val r = width - paddingRight
        val b = t + size
        mRectBottom.set(l, t, r, b)
        val gradient = LinearGradient(l.toFloat(), t.toFloat(), l.toFloat(), b.toFloat(), mColorsBottom,
            null, Shader.TileMode.CLAMP)
        mBottomPaint.shader = gradient
    }

    /**
     * @param top - flag to show top fade
     * @param bottom - flag to show bottom fade
     */
    fun showFade(top: Boolean, bottom: Boolean) {
        if (top != mDrawTopGradient) {
            mDrawTopGradient = top
            mDrawFlag = mDrawFlag or TOP_FLAG
        }
        if (bottom != mDrawBottomGradient) {
            mDrawBottomGradient = bottom
            mDrawFlag = mDrawFlag or BOTTOM_FLAG
        }
        if (mDrawFlag != 0) {
            invalidate()
        }
    }

    /**
     * @param top - top fade size in px
     * @param bottom - bottom fade size in px
     */
    fun fadeSize(top: Int, bottom: Int) {
        if (top != mTopSize) {
            mTopSize = top
            mDrawFlag = mDrawFlag or TOP_FLAG
        }
        if (bottom != mBottomSize) {
            mBottomSize = bottom
            mDrawFlag = mDrawFlag or BOTTOM_FLAG
        }
        if (mDrawFlag != 0) {
            invalidate()
        }
    }
}