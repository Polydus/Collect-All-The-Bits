package com.polydus.threecolor3button

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.math.Vector2
import com.polydus.threecolor3button.game.Game
import com.polydus.threecolor3button.game.view.GameScreen
import com.polydus.threecolor3button.io.AssetManager
import com.polydus.threecolor3button.io.FontManager
import com.polydus.threecolor3button.io.InputManager
import com.polydus.threecolor3button.io.StringManager
import com.polydus.threecolor3button.render.Renderer
import com.polydus.threecolor3button.render.Resolution
import com.polydus.threecolor3button.render.screen.Screen
import com.polydus.threecolor3button.util.Updater

class Main: ApplicationAdapter() {

    lateinit var asset: AssetManager
        private set

    lateinit var string: StringManager
        private set

    lateinit var font: FontManager
        private set

    lateinit var res: Resolution
        private set

    lateinit var render: Renderer
        private set
    lateinit var input: InputManager
        private set

    lateinit var game: Game
        private set

    private lateinit var updater: Updater

    lateinit var screen: Screen
        private set

    override fun render() {
        updater.update(game, render)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun create() {
        val size = Vector2(225f, 400f)
        size.set(112f, 200f)
        res = Resolution(size.x, size.y)
        render = Renderer(this)
        string = StringManager()
        asset = AssetManager()
        font = FontManager(this)
        input = InputManager(this)


        updater = Updater(this)

        asset.load("all")
        screen = GameScreen(this)
        game = Game(this, screen as GameScreen)
    }

    override fun dispose() {
    }

    fun perfString(): String{
        return updater.perfString
    }

}