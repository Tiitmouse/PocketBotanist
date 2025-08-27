package hr.algebra.lorena.pocketbotanist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.lorena.pocketbotanist.utils.ContextUtils

abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ContextUtils.updateLocale(newBase))
    }
}