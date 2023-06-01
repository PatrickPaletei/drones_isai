package id.ac.ukdw.data.presenter

import MarkerData
import android.util.Log
import id.ac.ukdw.data.apiHelper.ApiClient
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.data.model.GetMapsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationPresenter(private val view: LocationView) {

    private val markerList = mutableListOf<MarkerData>()
    private var dataLoaded = false

    fun loadData() {
        ApiClient.instance.getMaps().enqueue(object : Callback<GetMapsResponse> {
            override fun onResponse(call: Call<GetMapsResponse>, response: Response<GetMapsResponse>) {
                val code = response.code()
                val body = response.body()
                Log.d("dataBody", "response: " + body.toString())
                if (code == 200) {
                    if (body != null) {
                        val coordinat = body.body
                        processMarkers(coordinat)
                    }
                }
                dataLoaded = true
                checkDataAndMapReady()
            }

            override fun onFailure(call: Call<GetMapsResponse>, t: Throwable) {
                Log.d("nodata", "onFailure: " + t.message)
                dataLoaded = true
                checkDataAndMapReady()
            }
        })
    }

    private fun processMarkers(coordinat: List<Body>) {
        for (i in coordinat) {
            val marker = MarkerData(
                i.long.toDouble(),
                i.lat.toDouble(),
                "marker",
                "snip",
                i.idSample,
                "lahan ${i.loc}",
                i.comodity,
                i.date,
                i.carbonTanah,
                i.carbonTanaman
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

    // Other methods as needed
}
