package com.polydus.threecolor3button.render.layout

import com.badlogic.gdx.Gdx
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.io.InputReceiver
import com.polydus.threecolor3button.render.screen.Screen
import com.polydus.threecolor3button.render.view.ButtonView
import com.polydus.threecolor3button.render.view.View
import java.util.*

abstract class Layout(val main: Main,
                      val layer: Int,
                      open var screen: Screen) : InputReceiver {


    private var views = ArrayList<View>()
    private var buttonViews = ArrayList<ButtonView>()

    var isShow: Boolean = false
        private set

    var transitioning = false
        set(value) {
            transitionCounter = 0f
            field = value
        }
    var transitionCounter = 0f
    lateinit var nextLayout: Layout

    var transitionLimit = 22

    open fun update() {
        updateViews()
        updateLayout()
    }

    fun updateViews(){
        if (isShow) {
            try{
                for(v in views) {
                    v.update()
                }
            }catch (e: ConcurrentModificationException){
                //just break the loop, only happens when a view is removed
                //not a problem at all
            }

        }
        if(transitioning){
            transitionCounter += (Gdx.graphics.deltaTime * 60)
            if(transitionCounter >= transitionLimit){
                transitioning = false
                screen.setLayout(nextLayout)
            }
        }
    }

    abstract fun updateLayout()
    
    open fun addViews() {
        isShow = true
        for(v in views){
            main.render.addView(v, layer)
        }
    }

    open fun removeViews() {
        isShow = false
        for(v in views){
            main.render.removeView(v, layer)
        }
    }

    open fun show(){
        for(v in views){
            v.visible = true
        }
    }

    open fun hide(){
        for(v in views){
            v.visible = false
        }
    }

    fun addView(view: View){
        views.add(view)
        if(view is ButtonView){
            buttonViews.add(view)
        }
    }

    fun removeView(view: View){
        views.remove(view)
        if(view is ButtonView){
            buttonViews.remove(view)
        }
    }

    override fun onTouchDown(x: Float, y: Float): Boolean {
        for (b in buttonViews) {
            if (b.containsInput(x, y)) {
                b.onDown(x, y)
                return true
            }
        }

        return false
    }

    override fun onTouchUp(x: Float, y: Float): Boolean {
        for (b in buttonViews) {
            if (b.containsInput(x, y)) {
                b.onUp(x, y)
                return true
            }
        }

        return false
    }

    override fun onTouchDragged(x: Float, y: Float): Boolean {

        return false
    }


    fun forceUpAllButtonViews(){
        for(b in buttonViews){
            b.forceUp()
        }
    }
    
}