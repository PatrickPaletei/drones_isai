package id.ac.ukdw.data

import android.util.Log
import id.ac.ukdw.data.model.Body
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataPopulator {

    fun populateData(body: List<Body>): MutableMap<Int, MutableList<String>> {
        val mapData = mutableMapOf<Int, MutableList<String>>()

        for (item in body) {
            val carbonTanaman = item.carbonTanaman
            val carbonTanah = item.carbonTanah
            val emisiTanaman = item.emisiTanaman
            val emisiTanah = item.emisiTanah
            val emisiLingkungan = item.emisiLingkungan
            val date = item.date
            val komoditas = item.comodity
            val lokasi = item.loc

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val parsedDate = dateFormat.parse(date)
            if (parsedDate != null) {
                calendar.time = parsedDate
                val month = calendar.get(Calendar.MONTH) + 1

                val value = "$carbonTanaman $carbonTanah $emisiTanaman $emisiTanah $emisiLingkungan $komoditas $lokasi"
                if (mapData.containsKey(month)) {
                    mapData[month]?.add(value)
                } else {
                    mapData[month] = mutableListOf(value)
                }
            }
        }

        for ((month, values) in mapData) {
            Log.d("dataPair", "$month: $values")
        }

        if (body.isEmpty()) {
            Log.d("noData", "body is empty")
        }
        return mapData
    }

    fun populateDataFilter(
        body: List<Body>, filterEnabled: Boolean,
        selectedYear: Int?, selectedKomoditas: String?,
        selectedLokasi: String?
    ): MutableMap<Int, MutableList<String>> {


        val mapData = mutableMapOf<Int, MutableList<String>>()

        for (item in body) {
            val carbonTanaman = item.carbonTanaman
            val carbonTanah = item.carbonTanah
            val emisiTanaman = item.emisiTanaman
            val emisiTanah = item.emisiTanah
            val emisiLingkungan = item.emisiLingkungan
            val date = item.date
            val komoditas = item.comodity
            val lokasi = item.loc

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val parsedDate = dateFormat.parse(date)
            if (parsedDate != null) {
                calendar.time = parsedDate
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1

                // Apply filtering if the flag is enabled
                if (!filterEnabled || (selectedYear == null || year == selectedYear)
                    && (selectedKomoditas == null || komoditas == selectedKomoditas)
                    && (selectedLokasi == null || lokasi == selectedLokasi)
                ) {
                    val value =
                        "$carbonTanaman $carbonTanah $emisiTanaman $emisiTanah $emisiLingkungan"
                    if (mapData.containsKey(month)) {
                        mapData[month]?.add(value)
                    } else {
                        mapData[month] = mutableListOf(value)
                    }
                }
            }
        }

        return mapData
    }

}
