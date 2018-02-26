package com.polydus.threecolor3button.game.model

import com.badlogic.gdx.math.MathUtils

class Bit(world: World) : Object(world) {

    var force = 5000 * world.main.res.scale

    var repel = false
    var attr = false

    var canRepel = true
    var canAttr = true

    var player: Player = world.player

    private var angle = 0f

    init {
        bounds.setSize(6f, 6f)
        damping = 0.9f

        filterVal = 1
        collVal = 3
        type = MathUtils.random(0, 2)

    }

    override fun onUpdate() {
        if(attr && canAttr){

            angle = MathUtils.atan2(
                    player.body.worldCenter.y - body.worldCenter.y,
                    player.body.worldCenter.x - body.worldCenter.x)


            var forceMulti =
                    (((40 - Math.abs(body.worldCenter.x - player.body.worldCenter.x)) +
                            (40 - Math.abs(body.worldCenter.y - player.body.worldCenter.y))) / 2) / 40

            forceMulti++


            body.applyForceToCenter(
                    (force * forceMulti) * MathUtils.cos(angle),
                    (force * forceMulti) * MathUtils.sin(angle),
                    true)

            if(Math.abs(body.worldCenter.x - player.body.worldCenter.x) > 40f ||
                    Math.abs(body.worldCenter.y - player.body.worldCenter.y) > 40f){
                attr = false
            }
            if(player.type != type){
                attr = false
            }
        } else if(repel && canRepel){
            angle = MathUtils.atan2(
                    player.body.worldCenter.y - body.worldCenter.y,
                    player.body.worldCenter.x - body.worldCenter.x)

            var forceMulti =
                    (((40 - Math.abs(body.worldCenter.x - player.body.worldCenter.x)) +
                            (40 - Math.abs(body.worldCenter.y - player.body.worldCenter.y))) / 2) / 40

            forceMulti++

           // println(forceMulti)

            body.applyForceToCenter(
                    (-force * forceMulti) * MathUtils.cos(angle),
                    (-force * forceMulti) * MathUtils.sin(angle),
                    true)

            if(Math.abs(body.worldCenter.x - player.body.worldCenter.x) > 40f ||
                    Math.abs(body.worldCenter.y - player.body.worldCenter.y) > 40f){
                repel = false
            }
            if(player.type == type){
                repel = false
            }
            //repel = false
        }


        if(Math.abs(body.worldCenter.x - player.body.worldCenter.x) < 40f &&
                Math.abs(body.worldCenter.y - player.body.worldCenter.y) < 40f){

            if(player.type == type){
                attr = true
            } else {
                repel = true
            }


        }



    }

    fun repel(player: Player){
        //println("repel")
        repel = true
    }

    fun attr(player: Player){
        //println("attr type $type ${player.type}")
        attr = true
    }

    override fun reset() {
        super.reset()
        repel = false
        attr = false
    }

    fun resetType(){
        var type = 0
        do{
            type = MathUtils.random(0, 2)
        } while(type == this.type)

        this.type = type
    }

}