package ru.barinov.obdroid.base

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.widget.AppCompatImageView
import ru.barinov.obdroid.R
import ru.barinov.obdroid.core.ObdBus

interface CommonMenuInflater {


    fun inflateAndManageAnimation(menu: Menu, menuInflater: MenuInflater, baseMenuId: Int?){
        baseMenuId?.let {
            menuInflater.inflate(it, menu)
        }
        menuInflater.inflate(R.menu.connection_state_menu, menu)
        menu.findItem(R.id.connection_state_anim_item).actionView?.let {
            val animationHost = it.findViewById<AppCompatImageView>(R.id.icon)
            val animation = animationHost.drawable as AnimatedVectorDrawable
            if(ObdBus.lastEvent == ObdBus.ObdEvents.SuccessConnect) {
                animation.start()
            }
            animation.registerAnimationCallback(object : Animatable2.AnimationCallback(){
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    if(ObdBus.lastEvent == ObdBus.ObdEvents.SuccessConnect) {
                        animationHost.post { animation.start() }
                    }
                }
            })
        }
    }

}