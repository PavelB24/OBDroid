package ru.barinov.obdroid.activity

import android.os.Bundle
import android.view.MenuItem

import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.R
import ru.barinov.obdroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<ActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.container)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        //Set Version HERE
        navView.menu.findItem(R.id.terminalFragment).isVisible = false
        navView.getHeaderView(0).findViewById<TextView>(R.id.title).text =
            resources.getString(R.string.app_name)
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
        navView.setupWithNavController(navController)
    }

    fun onExit(item : MenuItem){
        viewModel.stopService()
        finish()
    }

    fun unlockDrawer(){
        viewModel.startService()
        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.container)
        return if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else navController.navigateUp() || super.onSupportNavigateUp()

    }
}