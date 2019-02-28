package com.melody.sixstar

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.view.View
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ScenceRender(var view: View) : GLSurfaceView.Renderer {
    private var mPreviousY: Float = 0.toFloat()//上次的触控位置Y坐标
    private var mPreviousX: Float = 0.toFloat()//上次的触控位置X坐标

    private val TOUCH_SCALE_FACTOR = 180.0f / 320//角度缩放比例

    var stars = ArrayList<SixStar>()

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        for (star in stars) {
            star.drawSelf()
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //
        GLES30.glViewport(0, 0, width, height)
        var ratio = width.toFloat() / height
        MatrixStatus.createFrustumOrtho(-ratio, ratio, -1f, 1f, 1f, 20f)

        MatrixStatus.createCarmera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        for (i in 0..5) {
            var star = SixStar(0.2f, 0.5f, -0.5f * i, view)
            stars.add(star)
        }

        //打开深度检测
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }

    fun setAngle(x: Float, y: Float) {
        for (h in stars)
        //设置各个六角星绕x轴、y轴旋转的角度
        {

            h.angleX += y
            h.angleY += x
        }
    }
}