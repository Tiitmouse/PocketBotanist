package hr.algebra.lorena.pocketbotanist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Find both the icon and the text
        val splashIcon = findViewById<ImageView>(R.id.iv_splash_icon)
        val appNameText = findViewById<TextView>(R.id.tv_app_name)

        // Load the single animation
        val fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade)

        // Apply the animation to both views
        splashIcon.startAnimation(fadeAnimation)
        appNameText.startAnimation(fadeAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000) // 1500ms delay + 500ms fade-out = 2000ms total
    }
}