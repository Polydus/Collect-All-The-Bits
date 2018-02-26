package com.polydus.threecolor3button.game.view

import com.badlogic.gdx.utils.Pool
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.game.model.Hole
import com.polydus.threecolor3button.render.view.ImageView

class HoleView(main: Main, asset: String) : ImageView(main, asset), Pool.Poolable {

    var obj: Hole? = null

    override fun update(){
        setCenter(obj!!.bounds.x + obj!!.bounds.width / 2,
                obj!!.bounds.y + obj!!.bounds.height / 2)
    }

    override fun reset() {
        obj = null
    }
}