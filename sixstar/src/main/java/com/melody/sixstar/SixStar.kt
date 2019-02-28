package com.melody.sixstar

import android.opengl.GLES30
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class SixStar(r: Float, R: Float, z: Float, view: View) {
    private lateinit var mVertexBuffer: FloatBuffer
    private lateinit var mColorBuffer: FloatBuffer
    private var vCount = 0  //点的数量

    private var mProgram = 0 //shader程序ID
    private var mPositionHandler = 0  //坐标点引用
    private var mColorHandler = 0 //颜色引用
    private var mMVPMatrixHandler = 0 //变换矩阵引用

    private var mMVPMatrix = FloatArray(16) //总变换矩阵

    var angleX = 0f
    var angleY = 0f

    init {
        initVertex(r, R, z)
        initShader(view)
    }

    private fun initVertex(r: Float, R: Float, z: Float) {
        initVertexLocation(r, R, z)
        initVertexColor()
    }

    /**
     * 初始化顶点坐标
     */
    private fun initVertexLocation(r: Float, R: Float, z: Float) {

        var count = 6 // 六边形
        var tempAngle = 360 / count
        var vertex = ArrayList<Float>()
        //第一个六边形
        for (index in 0 until count) {
            //第一个三角形
            var angle = index * tempAngle


            vertex.add(0f)  //第一个点的x值
            vertex.add(0f)  //第一个点的y值
            vertex.add(z)  //第一个点的z值

            vertex.add(R*Math.cos(Math.toRadians(angle.toDouble())).toFloat())//第二个点的x值
            vertex.add(R*Math.sin(Math.toRadians(angle.toDouble())).toFloat())//第二个点的y值
            vertex.add(z) //第二点的z值

            vertex.add(r * Math.cos(Math.toRadians((angle + tempAngle / 2).toDouble())).toFloat()) //第三个点x值
            vertex.add(r * Math.sin(Math.toRadians((angle + tempAngle / 2).toDouble())).toFloat())  //第三个点y值
            vertex.add(z)  //第三个点z值


            //第二个三角形
            vertex.add(0f)  //第一个点的x值
            vertex.add(0f)  //第一个点的y值
            vertex.add(z)  //第一个点的z值

            vertex.add(r * Math.cos(Math.toRadians((angle + tempAngle / 2).toDouble())).toFloat())//第二个点x值
            vertex.add(r * Math.sin(Math.toRadians((angle + tempAngle / 2).toDouble())).toFloat())   //第二个点y值
            vertex.add(z)  //第三个点z值

            vertex.add(R * Math.cos(Math.toRadians((angle + tempAngle).toDouble())).toFloat()) //第三个点x值
            vertex.add(R * Math.sin(Math.toRadians((angle + tempAngle).toDouble())).toFloat())  //第三个点y值
            vertex.add(z)  //第三个点z值
        }

        vCount = vertex.size / 3

        var vertexsArray = vertex.toFloatArray()
        var vbb = ByteBuffer.allocateDirect(vertexsArray.size * 4)
        vbb.order(ByteOrder.nativeOrder())  //设置字节顺序为本地操作系统顺序
        mVertexBuffer = vbb.asFloatBuffer() //转换为浮点型缓冲
        mVertexBuffer.put(vertexsArray)
        mVertexBuffer.position(0)

    }


    /**
     * 初始化顶点颜色
     */
    private fun initVertexColor() {
        var colorArray = FloatArray(vCount * 4)
        for (i in 0 until vCount) {
            if (i % 3 == 0) {
                colorArray[i * 4] = 1f
                colorArray[i * 4 + 1] = 1f
                colorArray[i * 4 + 2] = 1f
                colorArray[i * 4 + 3] = 0f
            } else {
                colorArray[i * 4] = 0.45f
                colorArray[i * 4 + 1] = 0.75f
                colorArray[i * 4 + 2] = 0.75f
                colorArray[i * 4 + 3] = 0f
            }
        }

        var cbb = ByteBuffer.allocateDirect(colorArray.size * 4)
        cbb.order(ByteOrder.nativeOrder())
        mColorBuffer = cbb.asFloatBuffer()
        mColorBuffer.put(colorArray)
        mColorBuffer.position(0)//设置缓冲区起始位置
    }

    private fun initShader(view: View) {
        var vertexShaderString = ShaderUtil.loadFromAssetsFile("vertex", view.resources)
        var fragmentShaderString = ShaderUtil.loadFromAssetsFile("colorfrag", view.resources)

        mProgram = ShaderUtil.createShaderProgram(vertexShaderString!!, fragmentShaderString!!)

        mPositionHandler = GLES30.glGetAttribLocation(mProgram, "aPos")
        mColorHandler = GLES30.glGetAttribLocation(mProgram, "aColor")
        mMVPMatrixHandler = GLES30.glGetUniformLocation(mProgram, "mMVPMatrix")

    }

    fun drawSelf() {
        GLES30.glUseProgram(mProgram)

        //初始化变换矩阵
        Matrix.setRotateM(mMVPMatrix, 0, 0f, 0f, 1f, 0f)
        //设置沿Z轴正向位移1
        Matrix.translateM(mMVPMatrix, 0, 0f, 0f, 1f)
        //设置绕y轴旋转yAngle度
        Matrix.rotateM(mMVPMatrix, 0, angleY, 0f, 1f, 0f)
        //设置绕x轴旋转xAngle度
        Matrix.rotateM(mMVPMatrix, 0, angleX, 1f, 0f, 0f)

        GLES30.glUniformMatrix4fv(mMVPMatrixHandler, 1, false, MatrixStatus.getMVPMatrxi(mMVPMatrix), 0)


        GLES30.glVertexAttribPointer(mPositionHandler, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer)
        GLES30.glVertexAttribPointer(mColorHandler, 4, GLES30.GL_FLOAT, false, 4 * 4, mColorBuffer)

        GLES30.glEnableVertexAttribArray(mPositionHandler)
        GLES30.glEnableVertexAttribArray(mColorHandler)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount)
    }
}