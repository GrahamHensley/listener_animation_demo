package com.listener.animations.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.PathInterpolator
import android.widget.Button
import android.widget.Toast
import com.listener.animations.R
import com.listener.animations.view.DismissableCard

class SwipeActivity : Activity(), DismissableCard.CardFlingedListener {

    var dismissableCard: DismissableCard? = null
    var button: Button? = null
    val set:AnimatorSet = AnimatorSet()
    var fadeAnim: ObjectAnimator? = null

    var orginalX: Float = 0F
    var orginalY: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)
        dismissableCard = findViewById(R.id.card_view)
        button = findViewById(R.id.reset)

        dismissableCard?.cardFlingListener = this

        button?.setOnClickListener {
            dismissableCard?.alpha = 1f
            dismissableCard?.x = orginalX
            dismissableCard?.y = orginalY

        }

        fadeAnim = ObjectAnimator.ofFloat(dismissableCard, "alpha", 1f, 0f).apply {
            duration = 320
        }
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float) {
        orginalX = dismissableCard?.x!!
        orginalY = dismissableCard?.y!!

        val vectorPath = Path().apply {
            moveTo(dismissableCard?.x!!, dismissableCard?.y!!)
            lineTo(e2?.x!!, e2?.y!!)
        }

        val pathAnimation = ObjectAnimator.ofFloat(dismissableCard, View.X, View.Y, vectorPath).apply {
            duration = 350
            start()
        }



        set.playTogether(pathAnimation, fadeAnim)
        set.start()
    }
}
