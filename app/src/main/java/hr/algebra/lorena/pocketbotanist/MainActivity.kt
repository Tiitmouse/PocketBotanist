package hr.algebra.lorena.pocketbotanist

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import hr.algebra.lorena.pocketbotanist.databinding.ActivityMainBinding
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var plantRepository: PlantRepository
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    private val keyboardLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private val r = Rect()
        override fun onGlobalLayout() {
            binding.root.getWindowVisibleDisplayFrame(r)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
            } else {
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
        plantRepository = PlantRepository(this)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_plant_list, R.id.nav_settings, R.id.nav_notification_center
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        askForNotificationPermission()
    }

    private fun setupNotificationBadge() {
        val unreadCount = plantRepository.getUnreadNotificationCount()
        val notificationMenuItem = binding.navView.menu.findItem(R.id.nav_notification_center)

        // Find the TextView within the menu item's action view
        val badgeTextView = notificationMenuItem?.actionView?.findViewById<TextView>(R.id.notification_badge_text_view)

        badgeTextView?.let {
            if (unreadCount > 0) {
                it.text = unreadCount.toString()
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
            }
        }
    }


    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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
        setupNotificationBadge()
    }

    override fun onPause() {
        super.onPause()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(keyboardLayoutListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}