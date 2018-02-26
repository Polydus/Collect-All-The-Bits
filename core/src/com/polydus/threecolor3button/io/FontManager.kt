package com.polydus.threecolor3button.io

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.polydus.threecolor3button.Main

class FontManager(val main: Main) {

    private val FONT = "redalert"

    private var generator: FreeTypeFontGenerator
    private var fonts: Array<BitmapFont>

    companion object {
        val SIZE_SMALL = 1
        val SIZE_MEDIUM = 2
        val SIZE_LARGE = 3
        val SIZE_SMALL_NOSHADOW = 4
    }

    init {
        generator = FreeTypeFontGenerator(Gdx.files.internal("font/$FONT.ttf"))
        fonts = arrayOf(
                genFont(SIZE_SMALL),
                genFont(SIZE_MEDIUM),
                genFont(SIZE_LARGE),
                genFont(SIZE_SMALL_NOSHADOW)
        )
        generator.dispose()
    }

    private fun genFont(size: Int): BitmapFont {
        val params = FreeTypeFontGenerator.FreeTypeFontParameter()


        params.color.set(main.render.white)


        params.size = 13//8 //* scale
        params.shadowColor = main.render.color212121
        params.shadowOffsetX = 0//scale

        if(size == SIZE_SMALL_NOSHADOW){
            params.shadowOffsetY = 0
        } else{
            params.shadowOffsetY = 1//scale
        }

        val result = generator.generateFont(params)

        if(size == SIZE_SMALL_NOSHADOW){
            result.data.scale((size - 3) * main.res.scale - 1f)
        } else {
            result.data.scale(size * main.res.scale - 1f)
        }

        return result
    }

    fun getFont(size: Int): BitmapFont {
        return fonts[size - 1]
    }
}