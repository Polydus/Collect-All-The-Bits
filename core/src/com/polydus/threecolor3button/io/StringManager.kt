package com.polydus.threecolor3button.io

import com.badlogic.gdx.utils.XmlReader

class StringManager {

    private var lang = java.util.Locale.getDefault().language

    private lateinit var elements: com.badlogic.gdx.utils.Array<XmlReader.Element>

    init {
        create()
    }

    private fun create(){
        //if other languages are supported, add them here
        if(lang != "en"){
            lang = "en"
        }

        val reader = com.badlogic.gdx.utils.XmlReader()

        val file = reader.parse(com.badlogic.gdx.Gdx.files.internal("string/strings_" + lang + ".xml"))
        elements = file.getChildrenByName("string")
    }

    fun getString(id : String): String{
        for (string in elements) {
            if(string.getAttribute("id") == id){
                return string.getAttribute("content")
            }
        }

        return "ERR: string not found!"
    }


}