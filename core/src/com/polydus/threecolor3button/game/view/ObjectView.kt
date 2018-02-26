package com.polydus.threecolor3button.game.view

import com.badlogic.gdx.utils.Pool
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.game.model.Object
import com.polydus.threecolor3button.render.view.ImageView

class ObjectView(main: Main, asset: String) : ImageView(main, asset), Pool.Poolable {

    var obj: Object? = null

    override fun update(){
        setPos(obj!!.bounds.x, obj!!.bounds.y)
    }

    override fun reset() {
        obj = null
    }
}