package com.polydus.threecolor3button.render.view

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.polydus.threecolor3button.Main

class AnimatedImageView//TextureRegion.split()

//val temp = /*TextureRegion.split(texture,
//texture.regionWidth / columns, texture.regionHeight / rows)*/
(main: Main, asset: String, private var rows: Int, private var columns: Int) : ImageView(main, asset) {

    private var textureAnimation: Animation<TextureRegion?>

    private var animationStateTime: Int = 0

    private var framesPerTexture: Int = 0
    private var amountOfFrames: Int = 0

    private var maskSprites: AnimatedImageView? = null

    init {
        val texture = main.asset.getTexture(asset)
        val temp = texture.split(
                texture.regionWidth / columns,
                texture.regionHeight / rows)
        val frames = arrayOfNulls<TextureRegion>(rows * columns)
        var i = 0
        for (x in 0 until rows) {
            for (y in 0 until columns) {
                frames[i++] = temp[x][y]
            }
        }
        animationStateTime = 0
        framesPerTexture = 2
        amountOfFrames = frames.size
        val frames2 = Array<TextureRegion>()
        for (j in frames.indices) {
            frames2.add(frames[j])
        }
        textureAnimation = Animation(framesPerTexture.toFloat(), *frames)
        textureAnimation.playMode = Animation.PlayMode.NORMAL
        sprite = Sprite(textureAnimation.getKeyFrame(animationStateTime.toFloat()))
        sprite.setOriginCenter()
        sprite.setScale(main.res.scale * 1f)
        setPos(0f, 0f)
        setSize(sprite.width, sprite.height)
    }


    fun setAnimationMode(playMode: Animation.PlayMode) {
        textureAnimation.playMode = playMode
        animationStateTime = 0
    }

    fun setFramesPerTexture(framesPerTexture: Int) {
        this.framesPerTexture = framesPerTexture
        textureAnimation.frameDuration = this.framesPerTexture.toFloat()
    }

    fun getKeyFrame(): Int {
        return textureAnimation.getKeyFrameIndex(animationStateTime.toFloat())
    }

    fun getRows(): Int {
        return rows
    }

    fun getColumns(): Int {
        return columns
    }

    fun setKeyFrame(frameIndex: Int) {
        sprite.setRegion(textureAnimation.getKeyFrame((frameIndex * framesPerTexture).toFloat()))

        maskSprite?.setRegion(maskSprites!!.textureAnimation.getKeyFrame((frameIndex * framesPerTexture).toFloat()))

        sprite.setFlip(flipX, flipY)
        maskSprite?.setFlip(flipX, flipY)
        //maskSprite?.setRegion(textureAnimation.getKeyFrame((frameIndex * framesPerTexture).toFloat()))
        //setMaskAlpha(maskSprite?.color.a)
    }

    override fun initMask() {
        maskSprites = AnimatedImageView(main, "${asset}_mask",
                rows, columns)
        maskSprite = Sprite(maskSprites!!.textureAnimation.getKeyFrame(0f))
        maskSprite!!.setOriginCenter()
        maskSprite!!.setScale(main.res.scale * 1f)
        setMaskAlpha(0f)
        resetPos()
    }

}