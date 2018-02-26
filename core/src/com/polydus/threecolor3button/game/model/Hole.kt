package com.polydus.threecolor3button.game.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle

class Hole(val world: World){

    var type = TYPE_RED

    companion object {
        val TYPE_RED = 0
        val TYPE_GREEN = 1
        val TYPE_BLUE = 2
    }

    val bounds = Rectangle(0f, 0f, 24f, 24f)

    fun setPos(x: Float, y: Float){
        bounds.setPosition(x, y)
    }

    fun isInBounds(bit: Bit): Boolean{
        return (bounds.overlaps(bit.bounds))
    }


    fun getColor(): Color {
        if(type == Object.TYPE_RED){
            return world.main.render.red
        } else if(type == Object.TYPE_GREEN){
            return world.main.render.green
        } else if(type == Object.TYPE_BLUE){
            return world.main.render.blue
        }
        return world.main.render.red
    }

    fun resetType(){
        var type = 0
        do{
            type = MathUtils.random(0, 2)
        } while(type == this.type)
        this.type = type
    }

}