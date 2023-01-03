package ru.barinov.obdroid.ui.activity

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView

import android.widget.TextView
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
        //Set Version HERE
        navView.menu.findItem(R.id.terminalFragment).isVisible = viewModel.shouldShowTerminal()
        navView.getHeaderView(0).findViewById<TextView>(R.id.terminal_switch).text =
            resources.getString(R.string.app_name)
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
        navView.setupWithNavController(navController)
        drawerLayout.addDrawerListener(
            AnimateIconDrawerListener(
                navView.getHeaderView(0)
                    .findViewById<ImageView>(R.id.imageView)
                    .drawable as AnimatedVectorDrawable
            ))
    }


    fun onExit(item : MenuItem){
        viewModel.stopService()
        finish()
    }

    fun onAboutPressed(item : MenuItem) {

    }

    fun unlockDrawer(){
        viewModel.startService()
        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
    }

    fun changeTerminalVisibility(){
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