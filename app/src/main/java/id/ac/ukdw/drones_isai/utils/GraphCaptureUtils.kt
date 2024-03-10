package id.ac.ukdw.drones_isai.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.github.mikephil.charting.charts.BarChart

object GraphCaptureUtils {
    fun captureGraph(graphView: BarChart): Bitmap? {
        // Measure and layout the graph view
        graphView.measure(
            View.MeasureSpec.makeMeasureSpec(graphView.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(graphView.height, View.MeasureSpec.EXACTLY)
        )
        graphView.layout(0, 0, graphView.measuredWidth, graphView.measuredHeight)

        // Create a bitmap with the same size as the graph view
        val bitmap = Bitmap.createBitmap(graphView.width, graphView.height, Bitmap.Config.ARGB_8888)

        // Create a canvas with the bitmap
        val canvas = Canvas(bitmap)

        // Draw the graph view onto the canvas
        graphView.draw(canvas)

        return bitmap
    }
}
