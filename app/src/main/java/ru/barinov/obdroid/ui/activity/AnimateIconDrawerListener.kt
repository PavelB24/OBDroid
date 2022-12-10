package ru.barinov.obdroid.ui.activity

import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout

class AnimateIconDrawerListener(
    private val head: AnimatedVectorDrawable
) : DrawerLayout.DrawerListener {

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

    }

    override fun onDrawerOpened(drawerView: View) {
        head.start()
    }

    override fun onDrawerClosed(drawerView: View) {
        head.stop()
    }

    override fun onDrawerStateChanged(newState: Int) {

    }
}