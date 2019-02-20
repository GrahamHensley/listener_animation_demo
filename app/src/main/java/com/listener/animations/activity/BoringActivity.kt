package com.listener.animations.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.listener.animations.R
import com.listener.animations.view.SegmentedColorBar

class BoringActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boring)

        findViewById<Button>(R.id.next).setOnClickListener {
            val nextView = Intent(applicationContext, ValueAnimatorActivity::class.java)
            nextView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(nextView)
        }
    }
}
