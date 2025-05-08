package com.ps212544_ml211022_cr200574_al202809.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashScreemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screem)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val splashImage: ImageView = findViewById(R.id.splashImage)
        splashImage.alpha = 0f

        Handler().postDelayed({
            val fadeIn = ObjectAnimator.ofFloat(splashImage, "alpha", 0f, 1f)
            fadeIn.duration = 3000

            val rotate = ObjectAnimator.ofFloat(splashImage, "rotation", 0f, 360f)
            rotate.duration = 4000

            val fadeOut = ObjectAnimator.ofFloat(splashImage, "alpha", 1f, 0f)
            fadeOut.duration = 2000

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(fadeIn, rotate, fadeOut)
            animatorSet.start()

        }, 2000)

        Handler().postDelayed({
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }, 10000)
    }
}
