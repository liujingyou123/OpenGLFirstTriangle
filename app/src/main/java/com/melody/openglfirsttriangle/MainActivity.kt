package com.melody.openglfirsttriangle

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {

    private var mView: MyView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = MyView(this)
        mView?.requestFocus()//获取焦点
        mView?.isFocusableInTouchMode = true//设置为可触控
        setContentView(mView)
    }

    override fun onResume() {
        super.onResume()
        mView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mView?.onPause()
    }
}
