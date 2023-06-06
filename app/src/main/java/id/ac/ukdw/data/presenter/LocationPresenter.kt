package id.ac.ukdw.data.presenter

import MarkerData
import android.util.Log
import id.ac.ukdw.data.apiHelper.ApiClient
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.data.model.GetMapsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationPresenter(private val view: LocationView) {

    private val markerList = mutableListOf<MarkerData>()
    private var dataLoaded = false


    private var isLoading = false

    fun loadData() {
        if (isLoading) return // Skip if data is already being loaded

        isLoading = true


        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.instance.getMaps()
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("dataBody", "response: " + body.toString())
                    if (body != null) {
                        val coordinat = body.body
                        processMarkers(coordinat)
                    }
                    dataLoaded = true
                } else {
                    Log.d("nodata", "Error: ${response.code()}")
                    dataLoaded = false
                }
            } catch (e: Exception) {
                Log.d("nodata", "Error: ${e.message}")
                dataLoaded = false
            }

            isLoading = false

            checkDataAndMapReady()
        }
    }


    private fun processMarkers(coordinat: List<Body>) {
        for (i in coordinat) {


            val marker = MarkerData(
                i?.long?.toDouble() ?: 0.0,  // Provide default latitude if i or i.long is null
                i?.lat?.toDouble() ?: 0.0,  // Provide default longitude if i or i.lat is null
                "marker",
                "snip",
                i?.idSample ?: "null",  // Provide "null" string value if i or i.idSample is null
                "Lahan ${i?.loc ?: "null"}",  // Provide "lahan null" string value if i or i.loc is null
                i?.comodity ?: "null",  // Provide "null" string value if i or i.comodity is null
                i?.date ?: "null",  // Provide "null" string value if i or i.date is null
                i?.carbonTanah
                    ?: "null",  // Provide "null" string value if i or i.carbonTanah is null
                i?.carbonTanaman
                    ?: "null"  // Provide "null" string value if i or i.carbonTanaman is null
            )

            markerList.add(marker)

        }
    }


    private fun checkDataAndMapReady() {
        if (dataLoaded && view.isMapReady()) {
            view.displayMarkers(markerList)

        }
    }

    fun searchMarkers(query: String) {
        val filteredMarkers = markerList.filter { markerData ->
            markerData.namaLahan.contains(query, ignoreCase = true) ||
                    markerData.komoditas.contains(query, ignoreCase = true)
        }
        view.updateMarkers(filteredMarkers)
    }


}
