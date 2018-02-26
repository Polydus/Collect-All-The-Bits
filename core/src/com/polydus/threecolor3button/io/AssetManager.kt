package com.polydus.threecolor3button.io

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

class AssetManager {

    private val textures = HashMap<String, TextureRegion>()
    private val sounds = HashMap<String, Sound>()

    fun load(suffix: String){

        for(t in textures){
            t.value.texture.dispose()
        }
        textures.clear()

        val atlas = TextureAtlas(Gdx.files.internal("texture/$suffix.atlas"))
        for(a in atlas.regions){
            textures.put(a.name, a)
        }

        //sounds.put("click", Gdx.audio.newSound(Gdx.files.internal("audio/sfx/click.ogg")))
        //sounds.put("sound_game_invalid", Gdx.audio.newSound(Gdx.files.internal("audio/sfx/sound_game_invalid.ogg")))
        //sounds.put("sound_game_success", Gdx.audio.newSound(Gdx.files.internal("audio/sfx/sound_game_success.ogg")))
    }

    fun getTexture(key : String) : TextureRegion{
        return textures[key]!!
    }

    fun getSound(key : String) : Sound{
        return sounds[key]!!
    }

}