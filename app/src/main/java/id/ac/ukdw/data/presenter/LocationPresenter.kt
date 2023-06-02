package id.ac.ukdw.data.presenter

import MarkerData
import android.util.Log
import id.ac.ukdw.data.apiHelper.ApiClient
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.data.model.Feature
import id.ac.ukdw.data.model.GetMapsResponse
import id.ac.ukdw.data.model.NewGetMapsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationPresenter(private val view: LocationView) {

    private val markerList = mutableListOf<MarkerData>()
    private var dataLoaded = false

    fun loadData() {
        ApiClient.instance.getMaps().enqueue(object : Callback<GetMapsResponse> {
            override fun onResponse(
                call: Call<GetMapsResponse>,
                response: Response<GetMapsResponse>
            ) {
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
                dataLoaded = false
                checkDataAndMapReady()
            }
        })
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
                i?.carbonTanah ?: "null",  // Provide "null" string value if i or i.carbonTanah is null
                i?.carbonTanaman ?: "null"  // Provide "null" string value if i or i.carbonTanaman is null
            )

            markerList.add(marker)
        }
    }

    fun loadData2() {
        ApiClient.instance.getMaps2().enqueue(object : Callback<NewGetMapsResponse> {
            override fun onResponse(
                call: Call<NewGetMapsResponse>,
                response: Response<NewGetMapsResponse>
            ) {
                val code = response.code()
                val body = response.body()
                Log.d("dataBody", "response: " + body.toString())
                if (code == 200) {
                    if (body != null) {
                        val features = body.features
                        for (feature in features) {
                            processMarkers2(feature)
                        }
                    }
                }
                dataLoaded = true
                checkDataAndMapReady()
            }

            override fun onFailure(call: Call<NewGetMapsResponse>, t: Throwable) {
                Log.d("nodata", "onFailure: " + t.message)
                dataLoaded = false
                checkDataAndMapReady()
            }
        })
    }

    private fun processMarkers2(feature: Feature) {
        val lat = feature.geometry.coordinates[0]
        val long = feature.geometry.coordinates[1]
        val marker = MarkerData(
            long,
            lat,
            "marker",
            "snip",
            feature.properties.idSample,
            "Lahan ${feature.properties.loc}",
            feature.properties.comodity,
            feature.properties.date,
            feature.properties.carbonTanah,
            feature.properties.carbonTanaman
        )
        markerList.add(marker)
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