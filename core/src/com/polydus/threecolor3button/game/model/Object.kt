package com.polydus.threecolor3button.game.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Pool

abstract class Object(val world: World): Pool.Poolable{

    val bounds = Rectangle(0f, 0f, 16f, 16f)

    var type = TYPE_RED

    var flaggedForRemove = false

    //var mass = 0f
    var damping = 0f

    lateinit var body: Body

    var filterVal: Short = Short.MIN_VALUE
    var collVal: Short = Short.MIN_VALUE

    companion object {
        val TYPE_RED = 0
        val TYPE_GREEN = 1
        val TYPE_BLUE = 2

        val FIXTURE_CORE = 1000
        val FIXTURE_SENSOR = 1001
    }

    fun update(){
        onUpdate()
        updatePos()
    }

    open fun onUpdate(){

    }

    private fun updatePos(){
        bounds.setPosition(body.position.x - bounds.width / 2, body.position.y - bounds.height / 2)
    }

    fun attracts(obj: Object): Boolean{
        if(sameType(obj)){
            return false
        }
        when{
            type == TYPE_RED && obj.type == TYPE_GREEN -> return true
            type == TYPE_GREEN && obj.type == TYPE_BLUE -> return true
            type == TYPE_BLUE && obj.type == TYPE_RED -> return true
        }
        return false
    }

    fun repels(obj: Object): Boolean{
        if(sameType(obj)){
            return false
        }
        when{
            type == TYPE_RED && obj.type == TYPE_GREEN -> return false
            type == TYPE_GREEN && obj.type == TYPE_BLUE -> return false
            type == TYPE_BLUE && obj.type == TYPE_RED -> return false
        }
        return true
    }

    fun sameType(obj: Object): Boolean{
        return type == obj.type
    }

    fun spawn(x: Float, y: Float, solid: Boolean){
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(x + bounds.width / 2, y + bounds.height / 2)
        bodyDef.fixedRotation = true
        bodyDef.angularDamping = 0f
        bodyDef.linearDamping = damping

        body = world.world.createBody(bodyDef)

        val circle = CircleShape()
        circle.radius = bounds.width / 2


        val fixtureDef = FixtureDef()
        fixtureDef.shape = circle
        fixtureDef.density = 1f
        fixtureDef.friction = 0f
        fixtureDef.restitution = 1f // Make it bounce a little bit
        fixtureDef.isSensor = !solid

        if(this is Player){ //lol
            fixtureDef.density = 50000f
        }

        if(filterVal != Short.MIN_VALUE){
            fixtureDef.filter.categoryBits = filterVal
        }
        if(collVal != Short.MIN_VALUE){
            fixtureDef.filter.maskBits = collVal
        }

        val fixture = body.createFixture(fixtureDef)
        fixture.userData = FIXTURE_CORE

        val sensorDef = FixtureDef()
        circle.radius = bounds.width * 3
        sensorDef.shape = circle
        fixtureDef.friction = 0.4f
        sensorDef.isSensor = true

        val sensorFix = body.createFixture(sensorDef)
        sensorFix.userData = FIXTURE_SENSOR
        body.userData = this


        circle.dispose()

        bounds.setPosition(x, y)
    }

    fun deSpawn(){
        world.world.destroyBody(body)
    }

    override fun reset() {
        bounds.setPosition(0f, 0f)
        //type = TYPE_RED
        flaggedForRemove = false
        deSpawn()
    }


    fun getColor(): Color {
        if(type == Object.TYPE_RED){
            return world.main.render.red
        } else if(type == Object.TYPE_GREEN){
            return world.main.render.green
        } else if(type == Object.TYPE_BLUE){
            return world.main.render.blue
        }
        return world.main.render.red
    }
}