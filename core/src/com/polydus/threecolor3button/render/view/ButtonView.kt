package com.polydus.threecolor3button.render.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.io.FontManager

class ButtonView(main: Main, size: String) : View(main) {

    private var size: String

    private val clickSound = "click"
    private val greySound = "sound_game_invalid"

    var useSound = true

    private var bg: AnimatedImageView
    private var icon: ImageView? = null
    private var text: TextView? = null

    private val greyColor = Color(0.38f, 0.38f, 0.38f, 1f)
    private val iconColor = Color()
    private val textColor = Color()
    private val bgColor = Color()
    var greyedOut = false
        set(value) {
            if(value){
                bg.setColor(greyColor.r, greyColor.g, greyColor.b)
                text?.setColor(greyColor.r, greyColor.g, greyColor.b)
                icon?.setColor(greyColor.r, greyColor.g, greyColor.b)
            }  else {
                bg.setColor(bgColor.r, bgColor.g, bgColor.b)
                text?.setColor(textColor.r, textColor.g, textColor.b)
                icon?.setColor(iconColor.r, iconColor.g, iconColor.b)
            }

            field = value
        }

    var onClickListener: OnClickListener? = null

    var onGreyClickListener: OnClickListener? = null

    var isToggle: Boolean = false
    private var state: Int = 0

    companion object {
        val STATE_NONE = 0
        val STATE_PRESSED = 1


        val SIZE_64x64 = "button"
        val SIZE_64x64_PAUSE = "button_pause"

        //val ICON_PAUSE = "ui_button_icon_pause"
    }

    init {
        clickable = true

        this.size = size

        bg = AnimatedImageView(main, size, 1, 2)
        bgColor.set(bg.getColor())

        //greyedOut = true

        setSize(bg.getWidth(), bg.getHeight())
        setPos(0f, 0f)
    }

    override fun draw(batch: SpriteBatch) {
        if(getAlpha() == 0f){
            return
        }
        bg.render(batch)
        if (text != null) {
            text!!.render(batch)
        }
        if(icon != null){
            icon!!.render(batch)
        }
    }

    override fun setPos(x: Float, y: Float) {
        super.setPos(x, y)
        bg.setPos(x, y)

        text?.setCenter(getCenterX(), getCenterY())
        icon?.setCenter(getCenterX(), getCenterY())
    }

    override fun setAlpha(a: Float) {
        super.setAlpha(a)
        bg.setAlpha(a)
        text?.setAlpha(a)
        icon?.setAlpha(a)
    }

    fun onUp(x: Float, y: Float) {
        //super.onUp(x, y)
        if(greyedOut){
            if(useSound){
                //main.asset.getSound(greySound).play(main.settings.soundLevel)
                onGreyClickListener?.onClick()
            }
            return
        }
        if (isToggle) {
            return
        }
        if (state == STATE_PRESSED) {
            this.bg.setKeyFrame(0)
            resetPos()
            state = STATE_NONE
            if(useSound){
               //main.asset.getSound(clickSound).play(main.settings.soundLevel)
            }
            onClickListener?.onClick()
        }
    }

    fun onDown(x: Float, y: Float) {
        //super.onDown(x, y)
        if(greyedOut){
            return
        }
        if (state == STATE_PRESSED) {
            return
        }
        this.bg.setKeyFrame(1)
        this.text?.setYBy(-1f)
        this.icon?.setYBy(-1f)
        state = STATE_PRESSED
        if (isToggle) {
            onClickListener?.onClick()
        }
    }

    fun forceUp() {
        this.bg.setKeyFrame(0)
        resetPos()
        state = STATE_NONE
    }

    fun forceDown() {
        this.bg.setKeyFrame(1)
        this.text?.setYBy(-1f)
        this.icon?.setYBy(-1f)
        state = STATE_PRESSED
    }

    fun setIcon(icon: String){
        setContent("")
        if(this.icon == null){
            this.icon = ImageView(main, icon)
        } else {
            this.icon!!.create(main, icon)
        }
        this.icon!!.visible = true
        iconColor.set(this.icon!!.getColor())
        if(greyedOut){
            this.icon!!.setColor(greyColor.r, greyColor.g, greyColor.b)
        }
        resetPos()
    }

    fun removeIcon(){
        if(this.icon != null){
            this.icon!!.visible = false
        }
    }

    fun addText(textView: TextView){
        text = textView
        text!!.alignment = Align.center
        text!!.setCenter(this.bg.getCenterX(), this.bg.getCenterY())
        textColor.set(text!!.getColor())
        if(greyedOut){
            text!!.setColor(greyColor.r, greyColor.g, greyColor.b)
        }
    }

    fun setContent(content: String){
        removeIcon()
        if(text != null){
            text!!.setContent(content, false)
            text!!.setCenter(this.bg.getCenterX(), this.bg.getCenterY())
        } else {
            addText(TextView(main, FontManager.SIZE_SMALL, content, false))
        }
    }

    fun setContent(content: String, x: Float){
        removeIcon()
        if(text != null){
            text!!.alignment = Align.left
            text!!.setContent(content, false)
            text!!.setCenter(this.bg.getCenterX(), this.bg.getCenterY())
            text!!.setX(x)
        } else {
            addText(TextView(main, FontManager.SIZE_SMALL, content, false))
        }
    }

    fun getTextX(): Float?{
        return text?.getX()
    }


    fun setColor(c: Color){
        setColor(c.r, c.g, c.b)
    }

    fun setColor(r: Float, g: Float, b: Float){
        this.bg.setColor(r, g, b)
    }

}