package id.ac.ukdw.drones_isai.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import com.github.mikephil.charting.charts.BarChart

object GraphCaptureUtils {
    fun captureGraph(graphView: BarChart): Bitmap? {
        // Create a bitmap with the same size as the graph view
        val bitmap = Bitmap.createBitmap(graphView.width, graphView.height, Bitmap.Config.ARGB_8888)

        // Create a canvas with the bitmap
        val canvas = Canvas(bitmap)

        // Draw the graph view onto the canvas
        graphView.draw(canvas)

        return bitmap
    }
}