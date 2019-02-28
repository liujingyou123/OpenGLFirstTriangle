package com.melody.openglfirsttriangle

import android.content.Context
import android.opengl.GLSurfaceView

class MyView(context: Context) : GLSurfaceView(context) {

    init {
        setEGLContextClientVersion(3)
        var renderer = SceneRender(this)
        setRenderer(renderer)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }
}