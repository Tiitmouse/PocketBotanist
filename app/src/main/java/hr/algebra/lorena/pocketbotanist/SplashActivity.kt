package hr.algebra.lorena.pocketbotanist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import hr.algebra.lorena.pocketbotanist.utils.getOnboardingPrefs
import hr.algebra.lorena.pocketbotanist.utils.isNetworkAvailable
import hr.algebra.lorena.pocketbotanist.utils.isOnboardingFinished

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
            if (!isNetworkAvailable()) {
                startActivity(Intent(this, NoInternetActivity::class.java))
                finish()
            } else {
                navigateToApp(false)
            }
        }, 2000)
    }

    fun navigateToApp(force: Boolean) {
        val onboardingFinished = getOnboardingPrefs().isOnboardingFinished()
        val intent = Intent(this, MainActivity::class.java)

        if (onboardingFinished || force) {
            intent.putExtra("NAVIGATE_TO", R.id.nav_plant_list)
        }

        startActivity(intent)
        finish()
    }
}