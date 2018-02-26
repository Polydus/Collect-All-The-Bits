package com.polydus.threecolor3button.render.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.polydus.threecolor3button.Main

abstract class View(val main: Main) {

    val id: Long = main.render.getNewId()

    private val bounds = Rectangle(0f, 0f, 0f, 0f)

    var fixedToCamera = true

    private var a = 1f

    var clickable = false

    var visible = true
        set(value) {
            if(value == field){
                return
            }
            if(value){
                show()
            } else {
                hide()
            }
            field = value
        }

    val childViews = ArrayList<View>()

    private var inCamBounds = false
    var alwaysUpdate = true


    private var tempRectangle = Rectangle()

    init {

    }

    private fun show(){

    }

    private fun hide(){

    }



    fun render(batch: SpriteBatch){
        if(visible){
            draw(batch)
        } else {

        }
    }

    abstract fun draw(batch: SpriteBatch)

    open fun update(){
        if(!alwaysUpdate && !inCamBounds){
            return
        }
    }

    open fun containsInput(x: Float, y: Float): Boolean {
        if(!clickable || !visible){
            return false
        }
        if(fixedToCamera){
            if(main.res.isLandscape){
                return bounds.contains(
                        (x * main.res.ratio) / main.res.scale,
                        (y * main.res.ratio) / main.res.scale)
            } else {
                return bounds.contains(x / main.res.scale, y / main.res.scale)
            }
        } else {
            if(main.res.isLandscape){
                return bounds.contains(
                        ((x + (main.render.cameraX() - Gdx.graphics.width / 2)) * main.res.ratio) / main.res.scale,
                        ((y + (main.render.cameraY() - Gdx.graphics.height / 2)) * main.res.ratio) / main.res.scale)
            } else {
                return bounds.contains(
                        (x + (main.render.cameraX() - Gdx.graphics.width / 2)) / main.res.scale,
                        (y + (main.render.cameraY() - Gdx.graphics.height / 2)) / main.res.scale
                )
            }
        }
    }

    fun inCameraBounds(reCalc: Boolean): Boolean{
        if(reCalc){
            inCamBounds = calcInCameraBounds()
        }
        return inCamBounds
    }

    private fun calcInCameraBounds(): Boolean{
        if(fixedToCamera){
            if(main.res.scaledVirtualBounds.overlaps(bounds)){
                return true
            }
        } else {
            tempRectangle.set(
                    (main.render.cameraX() - Gdx.graphics.width / 2) / main.res.scale,
                    (main.render.cameraY() - Gdx.graphics.height / 2) / main.res.scale,
                    main.res.width(),
                    main.res.height()
            )
            if(tempRectangle.overlaps(bounds)){
                return true
            }
        }


        return false
    }

    //only call from inside views themselves
    fun addChildView(v: View){
        childViews.add(v)
    }

    fun removeChildView(v: View){
        childViews.remove(v)
    }

    open fun setAlpha(a: Float){
        if(a > this.a && MathUtils.isEqual(a, 1f, 0.05f)){
            this.a = 1f
        } else if(a < this.a && MathUtils.isEqual(a, 0f, 0.05f)){
            this.a = 0f
        } else {
            this.a = a
        }
    }

    fun setAlphaBy(value: Float){
        setAlpha(getAlpha() + value)
    }

    fun getAlpha(): Float{
        return a
    }

    fun hasAlpha(): Boolean{
        if(a == 1f == MathUtils.isEqual(a, 1f, 0.05f)){
            return false
        }
        return true
    }

    open fun setPos(x: Float, y: Float){
        bounds.setPosition(x, y)
    }

    fun getX(): Float {
        return bounds.x
    }

    fun getX1(): Float {
        return bounds.x + bounds.width
    }

    fun getY(): Float {
        return bounds.y
    }

    fun getY1(): Float {
        return bounds.y + bounds.height
    }

    open fun setSize(width: Float, height: Float) {
        bounds.setSize(width, height)
    }

    fun setWidth(width: Float) {
        bounds.setWidth(width)
    }

    fun setHeight(height: Float) {
        bounds.setHeight(height)
    }

    fun getWidth(): Float {
        return bounds.width
    }

    fun getHeight(): Float {
        return bounds.height
    }

    fun getCenterX(): Float {
        return bounds.x + bounds.width / 2
    }

    fun getCenterY(): Float {
        return bounds.y + bounds.height / 2
    }

    fun setCenter(x: Float, y: Float) {
        setPos(x - bounds.width / 2, y - bounds.height / 2)
    }

    fun setCenterY(y: Float) {
        setPos(getX(), y - bounds.height / 2)
    }

    fun setCenterX(x: Float) {
        setPos(x - bounds.width / 2, getY())
    }

    fun setX(x: Float) {
        setPos(x, getY())
    }

    fun setY(y: Float) {
        setPos(getX(), y)
    }

    fun setXBy(dx: Float) {
        setPos(getX() + dx, getY())
    }

    fun setYBy(dy: Float) {
        setPos(getX(), getY() + dy)
    }

    fun setPosBy(dx: Float, dy: Float) {
        setPos(getX() + dx, getY() + dy)
    }

    fun setCenterScreen() {
        setPos(
                main.res.width() / 2 - getWidth() / 2,
                main.res.height() / 2 - getHeight() / 2
        )
    }

    fun resetPos(){
        setPos(getX(), getY())
    }

    fun forceSetVisible(value: Boolean){
        if(visible == value){
            return
        }
        visible = value
    }

    fun getFixedCameraX(): Float{
        if(fixedToCamera){
            return getX()
        } else {
            return getX() - (((main.render.cameraX() - Gdx.graphics.width / 2)) / main.res.scale)
        }
    }

    fun getFixedCameraY(): Float{
        if(fixedToCamera){
            return getX()
        } else {
            return getY() - (((main.render.cameraY() - Gdx.graphics.height / 2)) / main.res.scale)
        }
    }

    fun getFixedCameraCenterX(): Float{
        if(fixedToCamera){
            return getCenterX()
        } else {
            return getFixedCameraX() + getWidth() / 2
        }
    }

    fun getFixedCameraCenterY(): Float{
        if(fixedToCamera){
            return getCenterY()
        } else {
            return getFixedCameraY() + getHeight() / 2
        }
    }
}