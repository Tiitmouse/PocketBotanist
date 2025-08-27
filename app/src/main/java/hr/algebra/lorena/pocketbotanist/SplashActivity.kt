package hr.algebra.lorena.pocketbotanist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashIcon = findViewById<ImageView>(R.id.iv_splash_icon)
        val appNameText = findViewById<TextView>(R.id.tv_app_name)

        val fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade)

        splashIcon.startAnimation(fadeAnimation)
        appNameText.startAnimation(fadeAnimation)

        Handler(Looper.getMainLooper()).postDelayed({

            val sharedPref = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            val onboardingFinished = sharedPref.getBoolean("finished", false)

            val intent = Intent(this, MainActivity::class.java)

            if (!onboardingFinished) {
            } else {
                intent.putExtra("NAVIGATE_TO", R.id.nav_plant_list)
            }

            startActivity(intent)
            finish()
        }, 2000)
    }
}