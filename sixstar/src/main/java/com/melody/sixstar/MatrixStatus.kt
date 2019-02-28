package com.melody.sixstar

import android.opengl.Matrix

object MatrixStatus {

    private var mVMatrix = FloatArray(16)
    private var mPMatrix = FloatArray(16) //透视投影


    //设置摄像机
    fun createCarmera(
        eyeX: Float, eyeY: Float, eyeZ: Float,
        centerX: Float, centerY: Float, centerZ: Float, upX: Float, upY: Float,
        upZ: Float
    ) {
        Matrix.setLookAtM(mVMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
    }

    //透视投影
    fun createFrustumOrtho(
        left: Float, right: Float, bottom: Float, top: Float,
        near: Float, far: Float
    ) {
        Matrix.frustumM(mPMatrix, 0, left, right, bottom, top, near, far)
    }

    //设置正交投影的方法
    fun setProjectOrtho(
        left: Float,
        right: Float,
        bottom: Float,
        top: Float,
        near: Float,
        far: Float
    ) {
        Matrix.orthoM(
            mPMatrix, //存储生成矩阵元素的float[]类型数组
            0, //填充起始偏移量
            left, right, //near面的left、right
            bottom, top, //near面的bottom、top
            near, far        //near面、far面与视点的距离
        )
    }

    fun getMVPMatrxi(spec: FloatArray): FloatArray {
        var mMVPMatrix = FloatArray(16)
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, mPMatrix, 0, mMVPMatrix, 0)
        return mMVPMatrix
    }
}