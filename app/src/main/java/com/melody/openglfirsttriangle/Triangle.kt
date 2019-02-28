package com.melody.openglfirsttriangle

import android.opengl.GLES30
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle(view: View) {
    private var vCount = 3 //顶点个数
    private lateinit var mVertexBuffer: FloatBuffer // 顶点数据
    private lateinit var mColorBuffer: FloatBuffer
    private var mVertexShader: String? = null
    private var mFragmentShader: String? = null
    private var mProgram = 0 //着色器程序ID
    private var muMVPMatrixHandle: Int = 0//总变换矩阵引用
    private var maPositionHandle: Int = 0 //顶点位置属性引用
    private var maColorHandle: Int = 0 //顶点颜色属性引用


    var mProjMatrix = FloatArray(16) //投影矩阵
    var mVMatrix = FloatArray(16) //摄像机的参数矩阵

    private var mMMatrix = FloatArray(16) //物体的变换矩阵

    var xAngle = 0f//绕x轴旋转的角度


    init {
        initVertexData()
        initShader(view)
    }

    //初始化顶点坐标数据
    private fun initVertexData() {
        //顶点坐标数据的初始化
        initVertexLocation()
        //顶点颜色数据的初始化
        initVertexColor()
    }

    //初始化着色器
    private fun initShader(mv: View) {
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex", mv.getResources())
        //加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("colorfrag", mv.getResources())
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createShaderProgram(mVertexShader!!, mFragmentShader!!)
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPos")
        //获取程序中顶点颜色属性引用
        maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor")
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "mMVPMatrix")
    }


    /**
     * 初始化顶点坐标
     */
    private fun initVertexLocation() {
        var vertices = floatArrayOf(
            -0.8f, 0f, 0f,
            0f, -0.8f,
            0f, 0.8f, 0f, 0f
        )

        var vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder())  //设置字节顺序为本地操作系统顺序
        mVertexBuffer = vbb.asFloatBuffer() //转换为浮点型缓冲
        mVertexBuffer.put(vertices)
        mVertexBuffer.position(0)

    }

    /**
     * 初始化顶点颜色
     */
    private fun initVertexColor() {
        var colors = floatArrayOf(
            1F, 1F, 1F, 0F, //白色
            0F, 0F, 1F, 0F, //蓝色
            0F, 1F, 0F, 0F //绿色
        )

        var cbb = ByteBuffer.allocateDirect(colors.size * 4)
        cbb.order(ByteOrder.nativeOrder())
        mColorBuffer = cbb.asFloatBuffer()
        mColorBuffer.put(colors)
        mColorBuffer.position(0)//设置缓冲区起始位置
    }

    fun drawSelf() {
        //指定使用某套shader程序
        GLES30.glUseProgram(mProgram)
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0f, 1f, 0f, 0f)
        //设置沿Z轴正向位移1
        Matrix.translateM(mMMatrix, 0, 0f, 0f, 1f)
        //设置绕x轴旋转
        Matrix.rotateM(mMMatrix, 0, xAngle, 1f, 0f, 0f)
        //将变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, getFianlMatrix(mMMatrix), 0)
        //将顶点位置数据传送进渲染管线
        GLES30.glVertexAttribPointer(
            maPositionHandle,
            3,
            GLES30.GL_FLOAT,
            false,
            3 * 4,
            mVertexBuffer
        )
        //将顶点颜色数据传送进渲染管线
        GLES30.glVertexAttribPointer(
            maColorHandle,
            4,
            GLES30.GL_FLOAT,
            false,
            4 * 4,
            mColorBuffer
        )
        GLES30.glEnableVertexAttribArray(maPositionHandle)//启用顶点位置数据
        GLES30.glEnableVertexAttribArray(maColorHandle)//启用顶点着色数据
        //绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount)

    }

    fun getFianlMatrix(spec: FloatArray): FloatArray {
        var mMVPMatrix = FloatArray(16)
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0)
        return mMVPMatrix
    }


}