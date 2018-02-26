package com.polydus.threecolor3button.util

import com.badlogic.gdx.Gdx
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.game.Game
import com.polydus.threecolor3button.render.Renderer

class Updater(val main: Main) {

    private var fpsCounter = 0f
    private var lastFpsCounter = 0f
    private var lastUpdateTime = System.nanoTime()
    private var lastTick = System.nanoTime()

    //measure how much time these calls cost
    private var updateTimeCost = 0L
    private var renderTimeCost = 0L

    var lastUpdateTimeCost = 0L
        private set
        get() {
            return field / 1000000
        }

    var lastRenderTimeCost = 0L
        private set
        get() {
            return field / 1000000
        }

    fun update(game: Game, render: Renderer){
        if(canUpdate()){
            updateTimeCost = System.nanoTime()
            game.update()
            updateTimeCost = System.nanoTime() - updateTimeCost
            lastUpdateTimeCost = updateTimeCost

            lastUpdateTime = System.nanoTime()
        }
        renderTimeCost = System.nanoTime()
        main.screen.update()
        render.render()
        renderTimeCost = System.nanoTime() - renderTimeCost
        lastRenderTimeCost = renderTimeCost

        fpsCounter++
        if(System.nanoTime() - lastTick > 1000000000){
            lastTick = System.nanoTime()

            lastFpsCounter = fpsCounter
            fpsCounter = 0f

            report()
        }
    }

    fun canUpdate(): Boolean{
        return System.nanoTime() - lastUpdateTime > 16666666
    }

    private val baseTitle = main.string.getString("title")

    var perfString = ""
        private set

    private fun report(){
        perfString = "fps $lastFpsCounter | r: ${lastRenderTimeCost}ms | u: ${lastUpdateTimeCost}ms"
        //println("fps $lastFpsCounter | r: ${lastRenderTimeCost}ms | u: ${lastUpdateTimeCost}ms")
        Gdx.graphics.setTitle("$baseTitle | $perfString")
    }

}