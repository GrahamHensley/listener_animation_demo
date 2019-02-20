package com.listener.animations.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.listener.animations.R
import com.listener.animations.view.SegmentedColorBar

class IntoActivity : Activity() {

    var listPos = 0
    var listMax = 4

    var addButton:Button? = null
    var removeButton:Button? = null
    var reorderButton:Button? = null


    var animateContainer:LinearLayout? = null
    var nonAnimateContainer:LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        addButton = findViewById(R.id.add)
        removeButton = findViewById(R.id.remove)
        reorderButton = findViewById(R.id.reorder)

        animateContainer = findViewById(R.id.animate_container)
        nonAnimateContainer = findViewById(R.id.non_animate_container)

        findViewById<Button>(R.id.next).setOnClickListener {
            val nextView = Intent(applicationContext, BoringActivity::class.java)
            nextView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(nextView)
        }

        addButton?.setOnClickListener {
            if (listPos < listMax) {
                addView(createRandomTextView(), nonAnimateContainer)
                addView(createRandomTextView(), animateContainer)
                listPos++
            }
            updateButtonState()
        }

        reorderButton?.setOnClickListener {
            if (listPos >= listMax) {
                val aView2 = animateContainer?.getChildAt(1)
                val naView2 = nonAnimateContainer?.getChildAt(1)

                animateContainer?.removeViewAt(1)
                nonAnimateContainer?.removeViewAt(1)
                listPos --
                addView(naView2, nonAnimateContainer)
                addView(aView2, animateContainer)
            }

            updateButtonState()
        }

        removeButton?.setOnClickListener {
            if (listPos > 0) {
                removeView(nonAnimateContainer)
                removeView(animateContainer)
                listPos--
            }
            updateButtonState()
        }

        updateButtonState()
    }

    fun updateButtonState() {
        val count = animateContainer?.childCount ?: 0

        addButton?.isEnabled = count < listMax
        reorderButton?.isEnabled = count >= listMax
        removeButton?.isEnabled = count > 0
    }


    fun createRandomTextView(): TextView {
        val params:LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        val randomText:TextView = TextView(applicationContext)
        randomText.text = "test"
        randomText.layoutParams =params
        randomText.textSize = 32F



        val color:Int = when(listPos) {
            0 ->Color.MAGENTA
            1 -> Color.GREEN
            2 -> Color.YELLOW
            3 -> Color.CYAN
            else -> Color.GRAY
        }

        randomText.background = ColorDrawable(color)

        return randomText
    }

    fun addView(nView: View?, parent: ViewGroup?) {
        parent?.addView(nView, listPos)

    }

    fun removeView(parent: ViewGroup?) {
        parent?.removeViewAt(parent?.childCount -1)
    }
}
