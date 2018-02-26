package com.polydus.threecolor3button.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.polydus.threecolor3button.render.view.View
import java.util.LinkedHashMap
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.polydus.threecolor3button.Main


class Renderer(val main: Main){

    private val batch = SpriteBatch()

    val white = Color(1f, 1f, 1f, 1f)
    val black = Color(0f, 0f, 0f, 1f)

    val red = Color(229f / 255f, 57f / 255f, 53f / 255f, 1f)
    val green = Color(76f / 255f, 175f / 255f, 80f / 255f, 1f)
    val blue = Color(33f / 255f, 150f / 255f, 243f / 255f, 1f)

    //500
    //244, 67, 54
    //76, 175, 80
    //33, 150, 243

    //red 600
    //229, 57,g 53

    val redBg = Color(59f / 255f, 15f / 255f, 14f / 255f, 1f)
    val greenBg = Color(20f / 255f, 45f / 255f, 21f / 255f, 1f)
    val blueBg = Color(9f / 255f, 39f / 255f, 63f / 255f, 1f)

    //3b0f0e  	59	15	14
    //142d15  	20	45	21
    //09273f  	9	39	63


    //val red = Color(1f, 0f, 0f, 1f)
    //val green = Color(0f, 1f, 0f, 1f)
    //val blue = Color(0f, 0f, 1f, 1f)

    val color212121 = Color(33f / 255f, 33f / 255f, 33f / 255f, 1f)
    val color424242 = Color(66f / 255f, 66f / 255f, 66f / 255f, 1f)

    private val views: LinkedHashMap<Long, View>
    private val uiViews: LinkedHashMap<Long, View>

    //val LAYER_BG = 3
    val LAYER_WORLD = 2
    //val LAYER_WORLD_SUPER = 1
    val LAYER_UI = 0

    private val camera: OrthographicCamera
    private val uiCamera: OrthographicCamera

    //val cameraBounds = Rectangle(0f, 0f, 0f, 0f)

    private var lastViewId: Long = 0

    init {
        setBgColor(color424242)

        views = LinkedHashMap()
        uiViews = LinkedHashMap()

        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        uiCamera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        camera.position.set(
                (Gdx.graphics.width / 2).toFloat(),
                (Gdx.graphics.height / 2).toFloat(),
                0f
        )

        uiCamera.position.set(camera.position)

        camera.update()
        uiCamera.update()
    }

    fun render(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        batch.begin()

        batch.projectionMatrix = camera.combined
        renderBatch(views)

        batch.projectionMatrix = uiCamera.combined
        renderBatch(uiViews)


        batch.end()

        //debugRenderer.render(main.game.world.world, camera.combined)
    }

    //private val debugRenderer = Box2DDebugRenderer()

    private fun renderBatch(views: LinkedHashMap<Long, View>) {
        val iterator = views.values.iterator()
        while (iterator.hasNext()) {
            val v = iterator.next()
            v.render(batch)
        }
    }

    fun translateCam(x: Int, y: Int) {
        if (java.lang.Double.isNaN(x.toDouble()) || java.lang.Double.isNaN(y.toDouble())) {
            return
        }
        camera.translate(x.toFloat(), y.toFloat())

        onCamPosChange()
    }

    fun setCameraPos(x: Int, y: Int) {
        if (java.lang.Double.isNaN(x.toDouble()) || java.lang.Double.isNaN(y.toDouble())) {
            return
        }
        camera.position.set(x.toFloat(), y.toFloat(), 0f)

        onCamPosChange()
    }

    private fun onCamPosChange() {
        //enable if cam should be bounded
        /*if (camera.position.x < cameraBounds.x) {
            camera.position.x = cameraBounds.x
        } else if (camera.position.x > cameraBounds.x + cameraBounds.width) {
            camera.position.x = cameraBounds.x + cameraBounds.width
        }
        if (camera.position.y < cameraBounds.y) {
            camera.position.y = cameraBounds.y
        } else if (camera.position.y > cameraBounds.y + cameraBounds.height) {
            camera.position.y = cameraBounds.y + cameraBounds.height
        }*/

        camera.update()
    }

    fun setBgColor(color: Color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1f)
    }

    fun getNewId(): Long {
        lastViewId++
        return lastViewId - 1
    }

    fun cameraX(): Float {
        return camera.position.x
    }

    fun cameraY(): Float {
        return camera.position.y
    }

    fun addView(v: View, layer: Int) {
        when (layer) {
            //LAYER_BG -> bgViews.put(v.getId(), v)
            LAYER_WORLD -> views[v.id] = v
            //LAYER_WORLD_SUPER -> superViews.put(v.getId(), v)
            LAYER_UI -> uiViews[v.id] = v
        }
    }

    fun removeView(v: View, layer: Int) {
        when (layer) {
            //LAYER_BG -> bgViews.remove(v.getId())
            LAYER_WORLD -> views.remove(v.id)
            //LAYER_WORLD_SUPER -> superViews.remove(v.getId())
            LAYER_UI -> uiViews.remove(v.id)
        }
    }

    fun reset() {
        //bgViews.clear()
        views.clear()
        //superViews.clear()
        uiViews.clear()
    }

    fun calcScissors(clipBounds: Rectangle, scissors: Rectangle) {
        batch.projectionMatrix = camera.combined
        ScissorStack.calculateScissors(camera, batch.transformMatrix, clipBounds, scissors)
    }
}