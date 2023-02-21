package ru.barinov.obdroid.ui.activity

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView

import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.R
import ru.barinov.obdroid.databinding.ActivityMainBinding
import ru.barinov.obdroid.utils.PermissionsChecker

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<ActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @SuppressLint("SetTextI18n")
        binding.navView.getHeaderView(0)
            .findViewById<TextView>(R.id.version_tv).text = "ver. ${BuildConfig.VERSION_NAME}"

        val navController = findNavController(R.id.container)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        navView.menu.findItem(R.id.shellFragment).isVisible = viewModel.shouldShowTerminal()
        navView.getHeaderView(0).findViewById<TextView>(R.id.terminal_switch).text =
            resources.getString(R.string.app_name)
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
        navView.setupWithNavController(navController)

        drawerLayout.addDrawerListener(
            AnimateIconDrawerListener(
                navView.getHeaderView(0)
                    .findViewById<ImageView>(R.id.imageView)
                    .drawable as AnimatedVectorDrawable
            )
        )
        setSupportActionBar(binding.mainToolbar)
        setupToolbar()
    }

    fun showAbout(){
        binding.navView.menu.getItem(4).isVisible = true
    }

    fun hideAbout(){
        binding.navView.menu.getItem(4).isVisible = false
    }

    fun hideToolbar(){
        binding.mainToolbar.visibility = View.GONE
    }

    fun showToolbar(){
        binding.mainToolbar.visibility = View.VISIBLE
    }

    fun getToolbar() = binding.mainToolbar

    fun setupToolbar(){
        if(PermissionsChecker.hasNecessaryPermissions(this)) {
            val navController = findNavController(R.id.container)
            val graph = navController.navInflater.inflate(R.navigation.root_navigation)
            graph.setStartDestination(
                if (viewModel.isObdConnected())
                    R.id.homeFragment
                else R.id.connectionsFragment
            )
            val current = navController.currentDestination?.id
            navController.graph = graph
            binding.mainToolbar.setupWithNavController(navController, binding.drawerLayout)
            unlockDrawer()
            current?.let {
                if(it != R.id.permissionsFragment) {
                    navController.navigate(it)
//                    if(it == R.id.homeFragment){
//                        val innerController = findNavController(R.id.container2)
//                       val currentInner = innerController.currentDestination?.id
//                        Log.d("@@@", innerController.currentDestination?.displayName.toString())
//                        currentInner?.let {
//                            Log.d("@@@", "navigateInner ${innerController.currentDestination}")
//                            innerController.navigate(it)
//                        }
//
//                    }
                }
            }
        }
    }


    fun onExit(item: MenuItem) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.exit_message))
            .setPositiveButton(android.R.string.ok) { di, i ->
                viewModel.stopService()
                di.dismiss()
                finish()
            }
            .setNegativeButton(android.R.string.cancel) { di, i ->
                di.dismiss()
            }
            .show()
    }

    fun onAboutPressed(item: MenuItem) {
        findNavController(R.id.container).apply {
            if (currentDestination?.id != R.id.aboutDialogFragment) {
                navigate(R.id.aboutDialogFragment)
            }
        }
    }

    fun startService(){
        viewModel.startService()
    }

    fun unlockDrawer() {
        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
    }

    fun changeShellVisibility() {
        binding.navView.menu.getItem(3).isVisible = viewModel.shouldShowTerminal()
    }

    fun getDrawer() = binding.drawerLayout


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.container)
        return if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else navController.navigateUp() || super.onSupportNavigateUp()

    }
}