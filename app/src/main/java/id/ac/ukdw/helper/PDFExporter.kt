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
    // Rest of your code

    private companion object {
        private const val MAX_GRAPH_WIDTH = 500 // Set the maximum width of the graph image in pixels
        private const val MAX_GRAPH_HEIGHT = 500 // Set the maximum height of the graph image in pixels
    }

    fun exportToPDF(outputStream: OutputStream, dataFragments: List<DataExportable>) {
        // Rest of your code

        // Create a new PDF document
        val document = Document()

        // Create a PDF writer
        PdfWriter.getInstance(document, outputStream)

        // Open the document
        document.open()

        // Iterate over the data fragments and add their data to the PDF document
        for (fragment in dataFragments) {
            val dataString = fragment.getData()
            val dataBitmap = fragment.getScreen()

            if (dataBitmap != null) {
                // Resize the graph image if necessary
                val resizedBitmap = resizeBitmap(dataBitmap, MAX_GRAPH_WIDTH, MAX_GRAPH_HEIGHT)

                // Add the resized graph image to the PDF
                addImageToPDF(document, resizedBitmap)
                // Create a new paragraph in the PDF document and add the data
                val paragraph = Paragraph(dataString)
                document.add(paragraph)
            } else {
                Log.d("pdferr", "exportToPDF: dataNull")
            }
        }

        // Close the document
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

    fun addImageToPDF(document: Document, image: Bitmap) {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val imageBytes = stream.toByteArray()
        val image = Image.getInstance(imageBytes)
        document.add(image)
    }
}



