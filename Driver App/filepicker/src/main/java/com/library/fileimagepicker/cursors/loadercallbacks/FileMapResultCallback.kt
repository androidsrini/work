package com.library.fileimagepicker.cursors.loadercallbacks

import com.library.fileimagepicker.models.Document
import com.library.fileimagepicker.models.FileType


interface FileMapResultCallback {
    fun onResultCallback(files: Map<FileType, List<Document>>)
}

