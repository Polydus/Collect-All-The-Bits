package com.polydus.threecolor3button.game.model

import com.badlogic.gdx.math.MathUtils

class Player(world: World) : Object(world) {

    var initImpulse = 3000 * world.main.res.scale

    var points = 0

    var angle = 0f

    get() {
        return body.angle
    }

    init {
        bounds.setSize(16f, 16f)
        filterVal = 2
        //collVal = 1

        initImpulse *= 30000000
    }

    fun init(){
        points = 0
        body.applyLinearImpulse(
                initImpulse * MathUtils.cos(body.angle + 1),
                initImpulse * MathUtils.sin(body.angle + 1),
                body.worldCenter.x, body.worldCenter.y,
                true)

        type = MathUtils.random(0, 2)
    }

    override fun onUpdate() {

    }

    fun onHitWall(){
        //body.applyLinearImpulse(
        //        2600f * MathUtils.cos(body.angle + Math.PI.toFloat()),
        //        2600f * MathUtils.sin(body.angle + Math.PI.toFloat()),
        //        body.worldCenter.x, body.worldCenter.y,
        //        true)
        //body.applyAngularImpulse(30f, true)
        //body.setTransform(body.worldCenter, body.angle + Math.PI.toFloat())
    }




}