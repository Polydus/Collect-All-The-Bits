package com.polydus.threecolor3button.io

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.polydus.threecolor3button.Main

class InputManager(val main: Main) : InputAdapter() {

    private val pointerIds: IntArray = intArrayOf(-1, -1)

    var lastPointer: Int = 0
        private set

    var lastPos = Vector2()
        private set

    init {
        Gdx.input.inputProcessor = this
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (pointerIds[0] == pointer) {
            pointerIds[0] = -1
        } else if (pointerIds[1] == pointer) {
            pointerIds[1] = -1
        } else {
            return false
        }
        lastPointer = pointer

        if(main.res.isLandscape){
            main.screen.onTouchUp(
                    screenX.toFloat(),
                    (Gdx.graphics.height - screenY).toFloat())

        } else {
            main.screen.onTouchUp(
                    screenX.toFloat(),
                    (Gdx.graphics.height - screenY).toFloat())
        }

        lastPos.set(screenX.toFloat(),
                (Gdx.graphics.height - screenY).toFloat())
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (pointerIds[0] == pointer || pointerIds[1] == pointer) {

            lastPointer = pointer


            if (main.res.isLandscape) {
                main.screen.onTouchDragged(
                        screenX.toFloat(),
                        (Gdx.graphics.height - screenY).toFloat())
            } else {
                main.screen.onTouchDragged(
                        screenX.toFloat(),
                        (Gdx.graphics.height - screenY).toFloat())
            }


            lastPos.set(screenX.toFloat(),
                    (Gdx.graphics.height - screenY).toFloat())
            return true

        }


        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        if (!allActive()) { //at least 1 inactive
            if (pointerIds[0] == -1) {
                pointerIds[0] = pointer
            } else {
                pointerIds[1] = pointer
            }
            lastPointer = pointer



            if(main.res.isLandscape){
                main.screen.onTouchDown(
                        screenX.toFloat(),
                        (Gdx.graphics.height - screenY).toFloat())
            } else {
                main.screen.onTouchDown(
                        screenX.toFloat(),
                        (Gdx.graphics.height - screenY).toFloat())
            }

            lastPos.set(screenX.toFloat(),
                    (Gdx.graphics.height - screenY).toFloat())
            return true
        }


        return false
    }

    private fun allActive(): Boolean {
        return pointerIds[0] != -1 && pointerIds[1] != -1
    }

    private fun allInActive(): Boolean {
        return pointerIds[0] == -1 && pointerIds[1] == -1
    }

}