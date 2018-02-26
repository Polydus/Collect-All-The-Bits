package com.polydus.threecolor3button.game.model

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Pool
import com.sun.deploy.uitoolkit.ToolkitStore.dispose
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.game.Game
import java.util.*


class World(val game: Game): ContactListener {

    val main: Main = game.main

    private val bounds = Rectangle(
            0f, 16f + 3f,
            main.res.width(), main.res.height() - 38f)

    private val wallTop: Body
    private val wallLeft: Body
    private val wallRight: Body
    private val wallBottom: Body

    val player: Player

    //private val wallBottom2: Body

    private val objActives = ArrayList<Bit>()
    private val objPool = object : Pool<Bit>(){
        override fun newObject(): Bit {
            return Bit(this@World)
        }
    }

    private val hole = Hole(this)

    val world: World
            = com.badlogic.gdx.physics.box2d.World(Vector2(0f, 0f), true)

    init {
        bounds.setPosition(bounds.x + 1f, bounds.y + 1f)
        bounds.setSize(bounds.width - 2f, bounds.height - 2f)

        world.setContactListener(this)

        //add walls

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.position.set(2f, bounds.height - 2f)

        //wallTop = world.createBody(bodyDef)

        val shape = EdgeShape()
        shape.set(Vector2(2f, bounds.height - 2f),
                Vector2(bounds.width - 2, bounds.height - 2f))

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 1f
        fixtureDef.friction = 0f
        fixtureDef.restitution = 1f // Make it bounce a little bit
        //fixtureDef.isSensor = true

        //var fixture = wallTop.createFixture(fixtureDef)

        val offset = 0f
        val x0 = bounds.x + offset
        val x1 = x0 + bounds.width - offset * 2
        val y0 = bounds.y + offset
        val y1 = y0 + bounds.height - offset * 2

        fixtureDef.filter.categoryBits = 3

        bodyDef.position.set(x0, y0)
        wallBottom = world.createBody(bodyDef)
        shape.set(Vector2(x0, y0),
                Vector2(x1, y0))
        fixtureDef.shape = shape
        var fixture = wallBottom.createFixture(fixtureDef)

        bodyDef.position.set(x0, y0)
        wallLeft = world.createBody(bodyDef)
        shape.set(Vector2(x0, y0),
                Vector2(x0, y1))
        fixtureDef.shape = shape
        fixture = wallLeft.createFixture(fixtureDef)

        bodyDef.position.set(x1, y0)
        wallRight = world.createBody(bodyDef)
        shape.set(Vector2(x0, y0),
                Vector2(x0, y1))
        fixtureDef.shape = shape
        fixture = wallRight.createFixture(fixtureDef)

        bodyDef.position.set(x0, y1)
        wallTop = world.createBody(bodyDef)
        shape.set(Vector2(x0, y0),
                Vector2(x1, y0))
        fixtureDef.shape = shape
        fixture = wallTop.createFixture(fixtureDef)

        shape.dispose()

        wallBottom.userData = wallBottom
        wallLeft.userData = wallLeft
        wallRight.userData = wallRight
        wallTop.userData = wallTop


        game.screen.worldLayout.addHole(hole)
        resetHole()

        player = Player(this)
        player.spawn(bounds.width / 2, bounds.height / 2, true)
        player.init()

        game.screen.worldLayout.onAddPlayer(player)

        repeat(50){
            spawnObj()
        }

    }

    fun build(){
    }

    var notEnoughBitsTimes = 0

    fun update(){
        world.step(1/60f, 6, 2)

        player.update()

        var shouldResetHole = true
        var count = 0

        try{
            for(o in objActives){
                o.update()


                if(o.type == hole.type){
                    //shouldResetHole = false
                    count++
                    if(hole.isInBounds(o)){
                        o.flaggedForRemove = true
                    }
                }

                if(o.flaggedForRemove){
                    player.points++
                    game.screen.uiLayout.onUpdateScore(player.points)
                    removeObj(o)
                    spawnObj()
                }
                if(o.bounds.x < 0 ||
                        (o.bounds.x + o.bounds.width) > main.res.width() ||
                        o.bounds.y < bounds.y + 2 ||
                        o.bounds.y > bounds.y + bounds.height + 20){
                    removeObj(o)
                    spawnObj()
                }
            }
        }catch (e: ConcurrentModificationException){
            //just break the loop, only happens when a view is removed
            //not a problem at all
            shouldResetHole = false
        }

        if(shouldResetHole){
            if(count < 5){
                notEnoughBitsTimes++
                if(notEnoughBitsTimes > 120){
                    resetHole()
                    notEnoughBitsTimes = 0
                }
            } else if(count == 0){
                resetHole()
                notEnoughBitsTimes = 0
            }
        }
    }

    fun spawnObj(){
        addObj()
    }

    fun onButtonClick(button: Int){
        //world.spawnObj(button - 1)
        player.type = button - 1
        game.screen.worldLayout.player.setColor(player.getColor())
    }

    private fun addObj(){
        objActives.add(objPool.obtain())

        var x = 0f
        var y = 0f

        do {
            x = MathUtils.random(
                    bounds.x + 16f, bounds.width - 16)
            y = MathUtils.random(bounds.y + 16f, bounds.height - 16)
        } while(!validNewPos(x, y))

        objActives.last().spawn(x, y, true)

        objActives.last().resetType()

        game.screen.worldLayout.onAddObj(objActives.last())
        game.log("spawned obj at: ${objActives.last().bounds.x}x ${objActives.last().bounds.y}y." +
                " | Pool free: ${objPool.free} | Actives size: ${objActives.size}")
    }

    private fun validNewPos(x: Float, y: Float): Boolean{
        for(i in objActives){
            if(Math.abs(x - i.bounds.x) < 1 || Math.abs(y - bounds.y) < 1){
                return false
            }
        }
        return true
    }

    private fun removeObj(obj: Object){
        objActives.remove(obj)
        objPool.free(obj as Bit)
        game.screen.worldLayout.onRemoveObj(obj)
        game.log("removed obj at: ${obj.bounds.x}x ${obj.bounds.y}y." +
                " | Pool free: ${objPool.free} | Actives size: ${objActives.size}")
    }


    private fun resetHole(){
        hole.resetType()

        if(hole.type == Hole.TYPE_RED){
            main.render.setBgColor(main.render.redBg)
        } else if(hole.type == Hole.TYPE_BLUE){
            main.render.setBgColor(main.render.blueBg)
        } else if(hole.type == Hole.TYPE_GREEN){
            main.render.setBgColor(main.render.greenBg)
        }

        var x = 0f
        var y = 0f

        var offset = 36

        do {
            x = MathUtils.random(
                    bounds.x + offset, bounds.width - offset)
            y = MathUtils.random(bounds.y + offset, bounds.height - offset)
        } while(hole.bounds.contains(x, y))

        game.screen.worldLayout.updateHole()

        hole.setPos(x, y)
    }

    override fun endContact(contact: Contact?) {
    }

    override fun beginContact(contact: Contact?) {
        if(contact == null){
            return
        }

        val obj1 = contact.fixtureA.body.userData
        val obj2 = contact.fixtureB.body.userData

        if(obj1 is Player){
            if(obj2 is Bit){


                if((contact.fixtureA.userData as Int) == Object.FIXTURE_CORE &&
                        (contact.fixtureB.userData as Int) == Object.FIXTURE_CORE){
                    if((obj2 as Object).type == (obj1 as Object).type){
                        //obj2.flaggedForRemove = true
                        return
                    }
                }

                if((contact.fixtureA.userData as Int) == Object.FIXTURE_SENSOR &&
                        (contact.fixtureB.userData as Int) == Object.FIXTURE_CORE){
                    if((obj2 as Object).type == (obj1 as Object).type){
                        //attr
                        //obj2.attr(obj1)
                    } else {
                        //repel
                        obj2.repel(obj1)
                    }
                    return
                }

            }
        } else if(obj2 is Player){
            if(obj1 is Bit){

                if((contact.fixtureB.userData as Int) == Object.FIXTURE_CORE &&
                        contact.fixtureA.userData as Int == Object.FIXTURE_CORE){
                    if((obj2 as Object).type == (obj1 as Object).type){

                        //obj1.flaggedForRemove = true
                        return
                    }
                }

                if((contact.fixtureB.userData as Int) == Object.FIXTURE_SENSOR &&
                        (contact.fixtureA.userData as Int) == Object.FIXTURE_CORE){
                    if((obj1 as Object).type == (obj2 as Object).type){
                        //attr
                        //obj1.attr(obj2)
                    } else {
                        //repel
                        obj1.repel(obj2)
                    }
                    return
                }

            }
        }

        //if(){

        //}



        //contact?.fixtureA!!.
        //println("${contact?.childIndexA.toString()}, ${contact?.childIndexB.toString()}")
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

    }

}