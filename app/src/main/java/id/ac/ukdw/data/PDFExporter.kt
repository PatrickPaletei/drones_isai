package id.ac.ukdw.data

import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import id.ac.ukdw.helper.DataExportable
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class PDFExporter {
    fun exportToPDF(outputStream: OutputStream, dataFragments: List<DataExportable>) {
        val document = Document()

        try {
            PdfWriter.getInstance(document, outputStream)
            document.open()

            for (fragment in dataFragments) {
                val fragmentData = fragment.getData()
                val fragmentParagraph = Paragraph(fragmentData)
                document.add(fragmentParagraph)
            }

            document.close()
        } catch (e: DocumentException) {
            e.printStackTrace()
        } finally {
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}


