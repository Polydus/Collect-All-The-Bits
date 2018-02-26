package com.polydus.threecolor3button.game

import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.game.model.World
import com.polydus.threecolor3button.game.view.GameScreen


class Game(val main: Main, val screen: GameScreen){

    val world = World(this)

    var paused = false

    fun create(){
        world.build()
    }

    init {
        create()
    }

    fun update(){
        if(!paused){
            world.update()
        }
    }

    fun onButtonClick(button: Int){
        world.onButtonClick(button)
    }

    fun log(msg: String){
        //println(msg)
    }

    fun setPause(boolean: Boolean){
        paused = boolean
    }

    fun togglePause(){
        setPause(!paused)
    }

}