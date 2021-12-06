package com.example.a19012021091_assignment2_mad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var splash_text = findViewById<TextView>(R.id.splash_textView)
        val animationScale = AnimationUtils.loadAnimation(this, R.anim.animation)
        splash_text.startAnimation(animationScale)

        Handler().postDelayed({
            Intent(this, Login::class.java).apply {
                startActivity(this)
            }
            finish()
        }, 2500)
    }


}