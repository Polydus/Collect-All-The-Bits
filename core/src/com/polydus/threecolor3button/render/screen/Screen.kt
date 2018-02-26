package com.polydus.threecolor3button.render.screen

import com.badlogic.gdx.Gdx
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.io.InputReceiver
import com.polydus.threecolor3button.render.layout.Layout

abstract class Screen(main: Main): InputReceiver{

    val layouts: Array<Layout?> = Array(4){null}

    private var transitioningScreen = false
        set(value) {
            transitioningCounter = 0f
            field = value
        }
    private var transitioningCounter = 0f

    abstract fun nextScreen()

    fun update(){
        updateLayouts()
        updateScreen()
    }

    abstract fun setLayout(layout: Layout)

    abstract fun transitionLayout(l1: Layout, l2: Layout)

    abstract fun updateScreen()

    private fun updateLayouts(){
        for(l in layouts){
            l?.update()
        }
        if(transitioningScreen){
            transitioningCounter += (Gdx.graphics.deltaTime * 60)
            if(transitioningCounter >= 22){
                nextScreen()
            }
        }
    }

    fun hide() {
        for(l in layouts){
            l?.removeViews()
        }
    }

    fun show() {
        for(l in layouts){
            l?.addViews()
        }
    }

    fun initTransitionToNextScreen(){
        for(l in layouts){
            l?.hide()
        }
        transitioningScreen = true
    }

    fun forceUpAllViews(){
        for(l in layouts){
            //l?.forceUpAllViews()
        }
    }

}