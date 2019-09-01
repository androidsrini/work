package com.library.fileimagepicker.cursors.loadercallbacks

interface FileResultCallback<T> {
    fun onResultCallback(files: List<T>)
}