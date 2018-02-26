package com.polydus.threecolor3button.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle

class Resolution(width: Float, height: Float) {

    //change the screen size here
    private val WIDTH = width//112f//450f//224f//400f
    private val HEIGHT = height//200f//800f//400f//224f

    val scale: Int
    val realScale: Float

    private var deviceBounds: Rectangle
    private var virtualBounds: Rectangle

    var scaledVirtualBounds: Rectangle
        private set

    var isLandscape = false
        private set

    var ratio = 0f
        private set

    init {
        if(Gdx.graphics.width < WIDTH || Gdx.graphics.height < HEIGHT){
            throw Exception(
                    "device resolution too small. Min: " + WIDTH +
                            "px by " + HEIGHT + "px. You have: " +
                            Gdx.graphics.width + "px by" +
                            Gdx.graphics.height + "px."
            )
        }

        if(Gdx.graphics.width > Gdx.graphics.height){
            isLandscape = true
            ratio = Gdx.graphics.width / Gdx.graphics.height.toFloat()
        } else {
            ratio = Gdx.graphics.height / Gdx.graphics.width.toFloat()
        }

        deviceBounds = Rectangle(
                0f, 0f,
                Gdx.graphics.width * 1f, Gdx.graphics.height * 1f
        )

        virtualBounds = Rectangle(
                0f, 0f,
                WIDTH, HEIGHT
        )

        scale = (deviceBounds.width / virtualBounds.width).toInt()
        realScale = deviceBounds.width / virtualBounds.width

        scaledVirtualBounds = Rectangle(
                0f, 0f,
                deviceBounds.width / scale, deviceBounds.height / scale
        )

    }

    fun scaleFactor(): Float{
        return realScale - scale
    }

    fun width(): Float{
        return scaledVirtualBounds.width
    }

    fun height(): Float{
        return scaledVirtualBounds.height
    }

}