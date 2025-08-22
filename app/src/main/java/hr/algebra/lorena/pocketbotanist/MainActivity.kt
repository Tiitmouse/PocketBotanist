package hr.algebra.lorena.pocketbotanist

import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import hr.algebra.lorena.pocketbotanist.databinding.ActivityMainBinding
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Your existing keyboard listener - we are keeping it.
    private val keyboardLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private val r = Rect()
        override fun onGlobalLayout() {
            binding.root.getWindowVisibleDisplayFrame(r)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is visible
            } else {
                // Keyboard is hidden
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val useDarkMode = sharedPreferences.getBoolean("dark_theme", false)
        AppCompatDelegate.setDefaultNightMode(
            if (useDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_plant_list, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun setFabIcon(resourceId: Int) {
        binding.appBarMain.fab.setImageDrawable(
            ContextCompat.getDrawable(this, resourceId)
        )
    }

    fun setFabClickListener(listener: View.OnClickListener) {
        binding.appBarMain.fab.setOnClickListener(listener)
    }

    fun showFab() {
        binding.appBarMain.fab.show()
    }

    fun hideFab() {
        binding.appBarMain.fab.hide()
    }

    override fun onResume() {
        super.onResume()
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)
    }

    override fun onPause() {
        super.onPause()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(keyboardLayoutListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}