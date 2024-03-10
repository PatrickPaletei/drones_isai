package id.ac.ukdw.helper

import android.graphics.Bitmap
import android.util.Log
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class PDFExporter {
    companion object {
        private const val MAX_GRAPH_WIDTH = 500
        private const val MAX_GRAPH_HEIGHT = 500
    }

    fun exportToPDF(outputStream: OutputStream, dataFragments: List<DataExportable>) {
        val document = Document()
        PdfWriter.getInstance(document, outputStream)
        document.open()

        for (fragment in dataFragments) {
            val dataString = fragment.getData()
//            val dataBitmap = fragment.getScreen()
//
//            dataBitmap?.let {
//                val resizedBitmap = resizeBitmap(it, MAX_GRAPH_WIDTH, MAX_GRAPH_HEIGHT)
//                addImageToPDF(document, resizedBitmap)
//            } ?: Log.d("pdferr", "exportToPDF: dataNull")

            val paragraph = Paragraph(dataString)
            document.add(paragraph)
        }

        document.close()
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val scaleFactor = minOf(maxWidth.toFloat() / width, maxHeight.toFloat() / height)

        val newWidth = (width * scaleFactor).toInt()
        val newHeight = (height * scaleFactor).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun addImageToPDF(document: Document, image: Bitmap) {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val imageBytes = stream.toByteArray()
        val pdfImage = Image.getInstance(imageBytes)
        document.add(pdfImage)
    }
}




