package com.polydus.threecolor3button.game.view

import com.badlogic.gdx.utils.Pool
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.game.model.Bit
import com.polydus.threecolor3button.game.model.Hole
import com.polydus.threecolor3button.game.model.Object
import com.polydus.threecolor3button.game.model.Player
import com.polydus.threecolor3button.render.layout.Layout
import com.polydus.threecolor3button.render.screen.Screen
import com.polydus.threecolor3button.render.view.ImageView
import com.polydus.threecolor3button.render.view.TextView

class WorldLayout(main: Main, layer: Int, screen: Screen) : Layout(main, layer, screen) {

    val gameScreen: GameScreen = screen as GameScreen

    lateinit var player: ObjectView

    private val objActives = ArrayList<ObjectView>()
    private val objPool = object : Pool<ObjectView>(){
        override fun newObject(): ObjectView {
            return ObjectView(main, "bit")
        }
    }

    /*private val holeActives = ArrayList<HoleView>()
    private val holePool = object : Pool<HoleView>(){
        override fun newObject(): HoleView {
            return HoleView(main, "hole")
        }
    }*/

    lateinit var holeView: HoleView

    init {
        //player = ImageView(main, "ship")
        //player.setCenterScreen()

        //player.setColor(main.render.blue)

        //addView(player)
    }

    override fun updateLayout() {
        //player.rotate(-1f)
    }

    fun onAddPlayer(pl: Player){
        player = ObjectView(main, "ship")
        player.obj = pl
        player.setColor(pl.getColor())


        addView(player)
        if(isShow){
            removeViews()
            addViews()
        }
    }

    fun addHole(hole: Hole){
        holeView = HoleView(main, "hole")
        holeView.obj = hole

        updateHole()

        addView(holeView)
        if(isShow){
            removeViews()
            addViews()
        }

    }

    fun updateHole(){
        holeView.setColor(holeView.obj!!.getColor())
        gameScreen.uiLayout.pauseButtonView.setColor(holeView.obj!!.getColor())

        if(holeView.obj!!.type == Hole.TYPE_GREEN){
            gameScreen.uiLayout.devBg.setColor(main.render.green)
        } else if(holeView.obj!!.type == Hole.TYPE_BLUE){
            gameScreen.uiLayout.devBg.setColor(main.render.blue)
        } else if(holeView.obj!!.type == Hole.TYPE_RED){
            gameScreen.uiLayout.devBg.setColor(main.render.red)
        }

    }

    /*fun onAddHole(hole: Hole){
        holeActives.add(holePool.obtain())
        holeActives.last().obj = hole

        holeActives.last().setColor(hole.getColor())

        addView(holeActives.last())
        if(isShow){
            removeViews()
            addViews()
        }
    }

    fun onRemoveHole(hole: Hole){
        for(o in holeActives){
            if(o.obj == hole){
                holeActives.remove(o)
                holePool.free(o)
                if(isShow){
                    removeViews()
                    removeView(o)
                    addViews()
                } else {
                    removeView(o)
                }
                return
            }
        }
    }*/

    fun onAddObj(obj: Object){
        objActives.add(objPool.obtain())
        objActives.last().obj = obj

        if(obj is Bit){
            objActives.last().create(main, "bit")
        } else {
            objActives.last().create(main, "ship")
        }
        objActives.last().setColor(obj.getColor())

        addView(objActives.last())
        if(isShow){
            removeViews()
            addViews()
        }
    }

    fun onRemoveObj(obj: Object){
        for(o in objActives){
            if(o.obj == obj){
                objActives.remove(o)
                objPool.free(o)
                if(isShow){
                    removeViews()
                    removeView(o)
                    addViews()
                } else {
                    removeView(o)
                }
                return
            }
        }
    }
}