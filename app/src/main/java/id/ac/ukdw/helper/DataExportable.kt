package id.ac.ukdw.helper

import android.graphics.Bitmap

interface DataExportable {
    fun getData(): String
    fun getScreen(): Bitmap?
}
