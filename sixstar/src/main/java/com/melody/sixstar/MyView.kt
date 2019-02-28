package com.melody.sixstar

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class MyView(context: Context) : GLSurfaceView(context) {

    private val TOUCH_SCALE_FACTOR = 180.0f / 320//角度缩放比例
    private var mPreviousY: Float = 0.toFloat()//上次的触控位置Y坐标
    private var mPreviousX: Float = 0.toFloat()//上次的触控位置X坐标


    var renderer: ScenceRender

    init {
        setEGLContextClientVersion(3)
        renderer = ScenceRender(this)
        setRenderer(renderer)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val y = event.getY()//获取此次触控的y坐标
        val x = event.getX()//获取此次触控的x坐标
        when (event.getAction()) {
            MotionEvent.ACTION_MOVE//若为移动动作
            -> {
                val dy = y - mPreviousY//计算触控位置的Y位移
                val dx = x - mPreviousX//计算触控位置的X位移
                renderer.setAngle(dx * TOUCH_SCALE_FACTOR, dy * TOUCH_SCALE_FACTOR)
            }
        }
        mPreviousY = y//记录触控笔y坐标
        mPreviousX = x//记录触控笔x坐标

        return true
    }
}