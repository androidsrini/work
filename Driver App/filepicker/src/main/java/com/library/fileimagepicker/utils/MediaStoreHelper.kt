package com.library.fileimagepicker.filepicker.utils

import android.content.ContentResolver
import android.os.Bundle
import com.library.fileimagepicker.cursors.DocScannerTask
import com.library.fileimagepicker.cursors.loadercallbacks.FileMapResultCallback
import com.library.fileimagepicker.cursors.loadercallbacks.FileResultCallback
import com.library.fileimagepicker.cursors.loadercallbacks.PhotoScannerTask
import com.library.fileimagepicker.models.Document
import com.library.fileimagepicker.models.FileType
import com.library.fileimagepicker.models.PhotoDirectory
import java.util.*

object MediaStoreHelper {

    fun getDirs(contentResolver: ContentResolver, args: Bundle, resultCallback: FileResultCallback<PhotoDirectory>) {
        PhotoScannerTask(contentResolver,args,resultCallback).execute()
    }

    fun getDocs(contentResolver: ContentResolver,
                fileTypes: List<FileType>,
                comparator: Comparator<Document>?,
                fileResultCallback: FileMapResultCallback) {
        DocScannerTask(contentResolver, fileTypes, comparator, fileResultCallback).execute()
    }
}