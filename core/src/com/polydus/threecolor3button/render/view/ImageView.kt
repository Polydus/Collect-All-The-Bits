package com.polydus.threecolor3button.render.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.polydus.threecolor3button.Main

open class ImageView(main: Main, var asset: String) : View(main) {

    protected lateinit var sprite: Sprite

    protected open var maskSprite: Sprite? = null

    var flipX = false
        private set

    var flipY = false
        private set

    var setColor = false
        private set

    private lateinit var color: Color

    private val scissors = Rectangle()
    private val clipBounds = Rectangle()

    var clip = false

    var drawMaskAutomatically = true

    init {
        create(main, main.asset.getTexture(asset))
    }

    private fun create(main: Main, texture: TextureRegion?){
        sprite = Sprite(texture)
        sprite.setOriginCenter()
        sprite.setScale(main.res.scale * 1f)

        color = Color()
        color.set(sprite.color)

        setPos(0f, 0f)
        setSize(sprite.width, sprite.height)
        setPos(0f, 0f)
    }

    fun create(main: Main, asset: String){
        if(this.asset == asset){
            return
        }
        this.asset = asset
        create(main, main.asset.getTexture(asset))
    }

    fun setClipTo(width: Float, height: Float){
        scissors.set(0f, 0f, 0f, 0f)

        clipBounds.set(
                getX() * main.res.scale,
                getY() * main.res.scale,
                getWidth() * main.res.scale,
                getHeight() * main.res.scale
        )

        clipBounds.setSize(
                clipBounds.width * width,
                clipBounds.height * height)

        if(clipBounds.width < 2f){
            clipBounds.setWidth(2f)
        }

        main.render.calcScissors(clipBounds, scissors)
    }

    fun setClipTo(x: Float, y: Float, width: Float, height: Float){
        scissors.set(0f, 0f, 0f, 0f)

        clipBounds.set(
                getX() * main.res.scale,
                getY() * main.res.scale,
                getWidth() * main.res.scale,
                getHeight() * main.res.scale
        )

        clipBounds.set(
                clipBounds.x + clipBounds.width * x,
                clipBounds.y + clipBounds.height * y,
                clipBounds.width * width,
                clipBounds.height * height)

        if(clipBounds.width < 2f){
            clipBounds.setWidth(2f)
        }
        if(clipBounds.height < 2f){
            clipBounds.setHeight(2f)
        }
        main.render.calcScissors(clipBounds, scissors)
    }

    override fun draw(batch: SpriteBatch) {
        if(getAlpha() == 0f){
            return
        }
        sprite.setAlpha(getAlpha())

        drawSprite(batch)

    }

    fun drawMask(batch: SpriteBatch){
        if(maskSprite != null){
            if(maskSprite!!.color.a > 0f){
                maskSprite!!.draw(batch)
            }
        }
    }


    private fun drawSprite(batch: SpriteBatch){
        if(clip){
            //free hacks
            //batch.draw(sprite.texture, -99900f, -99999f)

            batch.flush()

            ScissorStack.pushScissors(scissors)
            sprite.draw(batch)
            if(drawMaskAutomatically){
                drawMask(batch)
            }
            batch.flush()
            ScissorStack.popScissors()
        } else {
            sprite.draw(batch)
            if(drawMaskAutomatically) {
                drawMask(batch)
            }
        }
    }

    override fun setPos(x: Float, y: Float) {
        super.setPos(x, y)

        sprite.setPosition(getX(), getY())

        if (main.res.scale > 1) {

            val renderX: Float
            val renderY: Float

            var offset = getWidth() / 2
            offset *= main.res.scale - 1

            renderX = getX() * main.res.scale + offset

            offset = getHeight() / 2
            offset *= main.res.scale - 1

            renderY = getY() * main.res.scale + offset

            sprite.setPosition(renderX, renderY)
        }
        maskSprite?.setPosition(sprite.x, sprite.y)
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        sprite.setSize(width, height)
        setOriginCenter()
    }

    private fun setOriginCenter(){
        sprite.setOriginCenter()
    }

    fun setRotation(deg: Float) {
        sprite.rotation = deg
    }

    fun rotate(deg: Float) {
        sprite.rotate(deg)
    }

    fun flip(flipX: Boolean, flipY: Boolean) {
        sprite.setFlip(flipX, flipY)
        maskSprite?.setFlip(flipX, flipY)
        this.flipX = flipX
        this.flipY = flipY
    }

    fun setColor(c: Color){
        setColor(c.r, c.g, c.b)
    }

    fun setColor(r: Float, g: Float, b: Float){
        this.color.set(r, g, b, getAlpha())
        this.sprite.setColor(r, g, b, getAlpha())
    }

    fun getColor(): Color {
        return this.color
    }

    override fun setAlpha(a: Float) {
        super.setAlpha(a)
        this.color.a = a
        this.sprite.setAlpha(a)
    }

    open fun initMask(){
        maskSprite = Sprite(main.asset.getTexture("${asset}_mask"))
        maskSprite!!.setOriginCenter()
        maskSprite!!.setScale(main.res.scale * 1f)
        //maskSprite!!.setColor(0f, 0f, 0f, 0f)
        setMaskAlpha(0f)
        resetPos()
    }

    fun setMaskAlpha(a: Float){
        //println(a)
        if(maskSprite != null){
            maskSprite!!.setColor(maskSprite!!.color.r,
                    maskSprite!!.color.g,
                    maskSprite!!.color.b, a)
        }
    }

    fun setMaskAlphaBy(a: Float){
        if(maskSprite != null){
            if(maskSprite!!.color.a + a <= 0f){
                setMaskAlpha(0f)
            } else {
                maskSprite!!.setColor(maskSprite!!.color.r,
                        maskSprite!!.color.g,
                        maskSprite!!.color.b,
                        maskSprite!!.color.a + a)
            }
        }
    }


}