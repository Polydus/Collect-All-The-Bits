package com.polydus.threecolor3button.game.view

import com.badlogic.gdx.utils.Align
import com.polydus.threecolor3button.Main
import com.polydus.threecolor3button.render.layout.Layout
import com.polydus.threecolor3button.render.view.ButtonView
import com.polydus.threecolor3button.render.view.ImageView
import com.polydus.threecolor3button.render.view.OnClickListener
import com.polydus.threecolor3button.render.view.TextView

class UILayout(main: Main, layer: Int, screen: GameScreen) : Layout(main, layer, screen) {

    private val debugTextView: TextView

    private val button1: ButtonView
    private val button2: ButtonView
    private val button3: ButtonView

    val devString:TextView
    val devBg:ImageView

    val pauseButtonView: ButtonView


    init {
        debugTextView = TextView(main, "0 pts", false)
        debugTextView.setPos(4f, main.res.height() - debugTextView.getHeight() - 10)

        button1 = ButtonView(main, ButtonView.SIZE_64x64)
        button2 = ButtonView(main, ButtonView.SIZE_64x64)
        button3 = ButtonView(main, ButtonView.SIZE_64x64)

        button1.setPos(4f, 4f)
        button2.setPos(main.res.width() / 2 - button2.getWidth() / 2, 4f)
        button3.setPos(main.res.width() - button3.getWidth() - 4, 4f)

        devString = TextView(main, "" +
                "   Game paused!\n\n" +
                "Collect all the bits! was made for " +
                "a 48hs game jam by @Polydus!", false)
        devString.wrapWidth(main.res.width() * 0.9f)
        //devString.setX(main.res.width() * 0.2f)
        devString.setCenterScreen()
        devString.setYBy(16f)
        //devString.setCenterY(main.res.height() * 0.6f)
        //devString.visible = false
        //devString.alignment = Align.center

        devBg = ImageView(main, "px")
        //devBg.setSize(devString.getWidth() + 16, devString.getHeight() + 16)
        devBg.setSize(main.res.width(), main.res.height() - 36)
        devBg.setY(36f)
        //devBg.setCenter(devString.getCenterX(), devString.getCenterY())

        pauseButtonView = ButtonView(main, ButtonView.SIZE_64x64_PAUSE)
        pauseButtonView.setPos(main.res.width() - pauseButtonView.getWidth() - 2,
                main.res.height() - pauseButtonView.getHeight() - 2)

        pauseButtonView.onClickListener = object :OnClickListener{
            override fun onClick() {
                main.game.togglePause()
                devString.visible = main.game.paused
                devBg.visible = main.game.paused
            }
        }

        button1.setColor(main.render.red)
        button2.setColor(main.render.green)
        button3.setColor(main.render.blue)

        button1.onClickListener = object : OnClickListener{
            override fun onClick() {
                main.game.onButtonClick(1)
            }
        }
        button2.onClickListener = object : OnClickListener{
            override fun onClick() {
                main.game.onButtonClick(2)
            }
        }
        button3.onClickListener = object : OnClickListener{
            override fun onClick() {
                main.game.onButtonClick(3)
            }
        }

        debugTextView.setCenterX(main.res.width() / 2)

        addView(debugTextView)
        addView(button1)
        addView(button2)
        addView(button3)
        addView(devBg)
        addView(devString)
        addView(pauseButtonView)
    }



    fun onUpdateScore(score: Int){
        debugTextView.setContent("$score pts")
        debugTextView.setCenterX(main.res.width() / 2)
    }

    override fun updateLayout() {
        //debugTextView.setContent("$", false)
        //debugTextView.setContent(main.perfString() +
        //        "\nw: ${main.res.width()} | h: ${main.res.height()}", false)
    }
}