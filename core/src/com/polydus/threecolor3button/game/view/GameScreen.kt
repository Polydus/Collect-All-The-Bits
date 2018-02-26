package com.polydus.threecolor3button.game.view

import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.render.layout.Layout
import com.polydus.threecolor3button.render.screen.Screen

class GameScreen(main: Main) : Screen(main) {

    val worldLayout: WorldLayout
    val uiLayout: UILayout

    init {
        worldLayout = WorldLayout(main, main.render.LAYER_WORLD, this)
        uiLayout = UILayout(main, main.render.LAYER_UI, this)

        setLayout(worldLayout)
        setLayout(uiLayout)

        uiLayout.devString.visible = false
        uiLayout.devBg.visible = false
    }

    override fun nextScreen() {
    }


    override fun updateScreen() {
    }


    override fun setLayout(layout: Layout){
        layouts[layout.layer]?.removeViews()
        layouts[layout.layer] = layout
        layouts[layout.layer]?.addViews()
        layouts[layout.layer]?.show()
    }

    override fun transitionLayout(l1: Layout, l2: Layout){
        if(l1 == l2){
            return
        }
        //layout.transitioning = true
        l1.transitioning = true
        l1.nextLayout = l2
        l1.hide()
    }

    override fun onTouchDown(x: Float, y: Float): Boolean {
        for(l in layouts){
            if(l != null && l.onTouchDown(x, y)){
                return true
            }
        }

        return false
    }

    override fun onTouchUp(x: Float, y: Float): Boolean {
        for(l in layouts){
            if(l != null && l.onTouchUp(x, y)){
                forceUpAllViews()
                return true
            }
        }
        forceUpAllViews()

        return false
    }

    override fun onTouchDragged(x: Float, y: Float): Boolean {
        for(l in layouts){
            if(l != null && l.onTouchDragged(x, y)){
                return true
            }
        }

        return false
    }

}