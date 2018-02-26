package com.polydus.threecolor3button.render.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.io.FontManager

class TextView: View {

    private lateinit var font: BitmapFont
    private lateinit var content: String
    private lateinit var layout: GlyphLayout

    private var renderX: Float = 0f
    private var renderY: Float = 0f

    private var color: Color = Color()

    private var wrap = false
    private var wrapWidth = -1f

    var alignment: Int = 0
        set(value) {
            field = value
            setContent(this.content, false)
        }

    private var size: Int = 0

    constructor(main: Main, content: String): super(main){
        create(FontManager.SIZE_SMALL, content, true)
    }

    constructor(main: Main, content: String, managed: Boolean): super(main){
        create(FontManager.SIZE_SMALL, content, managed)
    }

    constructor(main: Main, size: Int, content: String): super(main){
        create(size, content, true)
    }

    constructor(main: Main, size: Int, content: String, managed: Boolean): super(main){
        create(size, content, managed)
    }

    private fun create(size: Int, content: String, managed: Boolean){
        this.size = size

        if (managed) {
            this.content = main.string.getString(content)
        } else {
            this.content = content
        }

        font = main.font.getFont(size)

        layout = GlyphLayout(font, this.content)
        color.set(font.color.r, font.color.g, font.color.b, font.color.a)

        alignment = Align.left

        setSize(layout.width / main.res.scale, layout.height / main.res.scale)
        setPos(0f, 0f)
    }



    override fun draw(batch: SpriteBatch) {
        if(getAlpha() == 0f){
            return
        }
        font.draw(batch, layout, renderX, renderY)
    }

    override fun setPos(x: Float, y: Float) {
        super.setPos(x, y)
        renderX = getX()
        renderY = getY()

        if (main.res.scale > 1f) {
            renderX = getX() * main.res.scale

            renderY = getY() * main.res.scale + getHeight() * main.res.scale + main.res.scale
        } else {
            renderY += getHeight() * main.res.scale + main.res.scale
        }

        renderY -= main.res.scale
    }

    fun setContent(content: String){
        setContent(content, true)
    }

    fun setContent(content: String, managedString: Boolean) {
        if (managedString) {
            this.content = main.string.getString(content)
        } else {
            this.content = content
        }

        //layout.setText(font, this.content, color, layout.width, alignment, wrap)
        if(this.wrap){
            layout.setText(font, content, color, this.wrapWidth, alignment, wrap)
        } else {
            layout.setText(font, content, color, layout.width, alignment, wrap)
        }

        setSize(layout.width / main.res.scale, layout.height / main.res.scale)
    }

    fun getContent(): String{
        return content
    }

    fun wrapWidth(size: Float){
        if(size == -1f){
            this.wrap = false
            layout.setText(font, this.content, color, layout.width, alignment, false)
        } else {
            this.wrap = true
            this.wrapWidth = size * main.res.scale
            layout.setText(font, this.content, color, this.wrapWidth, alignment, true)
        }
        setSize(layout.width / main.res.scale, layout.height / main.res.scale)
    }

    override fun setAlpha(a: Float) {
        super.setAlpha(a)

        color.set(
                color.r,
                color.g,
                color.b,
                a
        )

        if(this.wrap){
            layout.setText(font, content, color, this.wrapWidth, alignment, wrap)
        } else {
            layout.setText(font, content, color, layout.width, alignment, wrap)
        }
    }

    fun setColor(c: Color){
        setColor(c.r, c.g, c.b)
    }

    fun setColor(r: Float, g: Float, b: Float){
        this.color.set(r, g, b, getAlpha())
        if(this.wrap){
            layout.setText(font, content, color, this.wrapWidth, alignment, wrap)
        } else {
            layout.setText(font, content, color, layout.width, alignment, wrap)
        }
    }

    fun getColor(): Color {
        return this.color
    }
}