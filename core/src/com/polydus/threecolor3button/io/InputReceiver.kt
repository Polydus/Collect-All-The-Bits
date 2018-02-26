package com.polydus.threecolor3button.io

interface InputReceiver {

    fun onTouchDown(x: Float, y: Float): Boolean

    fun onTouchUp(x: Float, y: Float): Boolean

    fun onTouchDragged(x: Float, y: Float): Boolean

}