package com.listener.animations.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.listener.animations.R
import com.listener.animations.view.SegmentedColorBar

class ValueAnimatorActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_value_animator)
        findViewById<Button>(R.id.redraw).setOnClickListener {
            findViewById<SegmentedColorBar>(R.id.colorbar2).reset()
            findViewById<SegmentedColorBar>(R.id.colorbar3).reset()
            findViewById<SegmentedColorBar>(R.id.colorbar4).reset()
            findViewById<SegmentedColorBar>(R.id.colorbar5).reset()
            findViewById<SegmentedColorBar>(R.id.colorbar6).reset()
            findViewById<SegmentedColorBar>(R.id.colorbar7).reset()
            findViewById<SegmentedColorBar>(R.id.colorbar8).reset()
            findViewById<SegmentedColorBar>(R.id.colorbar9).reset()

        }
        findViewById<Button>(R.id.next).setOnClickListener {
            val nextView = Intent(applicationContext, SwipeActivity::class.java)
            nextView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(nextView)
        }

        findViewById<TextView>(R.id.label_bar2).setOnClickListener {
            findViewById<SegmentedColorBar>(R.id.colorbar2).reset()
        }
        findViewById<TextView>(R.id.label_bar3).setOnClickListener {
            findViewById<SegmentedColorBar>(R.id.colorbar3).reset()
        }
        findViewById<TextView>(R.id.label_bar4).setOnClickListener {
            findViewById<SegmentedColorBar>(R.id.colorbar4).reset()
        }
        findViewById<TextView>(R.id.label_bar5).setOnClickListener {
            findViewById<SegmentedColorBar>(R.id.colorbar5).reset()
        }
        findViewById<TextView>(R.id.label_bar6).setOnClickListener {
            findViewById<SegmentedColorBar>(R.id.colorbar6).reset()
        }
        findViewById<TextView>(R.id.label_bar7).setOnClickListener {
            findViewById<SegmentedColorBar>(R.id.colorbar7).reset()
        }
        findViewById<TextView>(R.id.label_bar8).setOnClickListener {
            findViewById<SegmentedColorBar>(R.id.colorbar8).reset()
        }
        findViewById<TextView>(R.id.label_bar9).setOnClickListener {
            findViewById<SegmentedColorBar>(R.id.colorbar9).reset()
        }
    }
}
