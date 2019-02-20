package com.listener.animations.view

import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class DismissableCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {
    private var detector: GestureDetectorCompat = GestureDetectorCompat(context, this)

    var cardFlingListener:CardFlingedListener? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (detector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        cardFlingListener?.onFling(e1,e2,velocityX,velocityY)
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
    }

    interface CardFlingedListener {
        fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float)
    }


}