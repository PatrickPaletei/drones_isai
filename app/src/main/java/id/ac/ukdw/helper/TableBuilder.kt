package id.ac.ukdw.helper

import com.github.mikephil.charting.data.BarEntry

class TableBuilder {
    companion object {
        fun buildTable(
            xAxisLabels: List<String>,
            rowTemplate: String,
            vararg barEntries: List<BarEntry>
        ): String {
            val tableBuilder = StringBuilder()

            // Create table headers
            tableBuilder.append("+-----------------")
            for (i in barEntries.indices) {
                tableBuilder.append("+-----------------")
            }
            tableBuilder.append("+\n")

            tableBuilder.append("|  Bulan ")
            for (i in barEntries.indices) {
                tableBuilder.append("|  Dataset ${i + 1}  ")
            }
            tableBuilder.append("|\n")

            tableBuilder.append("+-----------------")
            for (i in barEntries.indices) {
                tableBuilder.append("+-----------------")
            }
            tableBuilder.append("+\n")

            // Add table rows based on xAxisLabels size
            for (i in xAxisLabels.indices) {
                val label = when (xAxisLabels[i]) {
                    "1" -> "Januari"
                    "2" -> "Februari"
                    "3" -> "Maret"
                    "4" -> "April"
                    "5" -> "Mei"
                    "6" -> "Juni"
                    "7" -> "Juli"
                    "8" -> "Agustus"
                    "9" -> "September"
                    "10" -> "Oktober"
                    "11" -> "November"
                    "12" -> "Desember"
                    else -> xAxisLabels[i]
                }

                val rowBuilder = StringBuilder()
                rowBuilder.append("|  ${label.padEnd(9)}")

                for (element in barEntries) {
                    val value = element[i].y.toString()
                    rowBuilder.append("|  ${value.padEnd(9)}")
                }

                rowBuilder.append("|\n")
                tableBuilder.append(rowBuilder)
                tableBuilder.append("+-----------------")
                for (j in barEntries.indices) {
                    tableBuilder.append("+-----------------")
                }
                tableBuilder.append("+\n")
            }

            // Render the table
            return tableBuilder.toString()
        }
    }
}






