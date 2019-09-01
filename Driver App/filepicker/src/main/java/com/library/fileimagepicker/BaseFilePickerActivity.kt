package com.library.fileimagepicker.filepicker

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.library.fileimagepicker.R


abstract class BaseFilePickerActivity : AppCompatActivity() {

    protected fun onCreate(savedInstanceState: Bundle?, @LayoutRes layout: Int) {
        super.onCreate(savedInstanceState)
        setTheme(PickerManager.theme)
        setContentView(layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //set orientation
        requestedOrientation = PickerManager.orientation
        initView()
    }

    protected abstract fun initView()
}
