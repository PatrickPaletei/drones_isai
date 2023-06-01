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
            if(i.carbonTanah != null && i.carbonTanaman !=null){
                val lat = i.lat
                val long = i.long
                val idSample = i.idSample
                val namaLahan = "lahan ${i.loc}"
                val comodity = i.comodity
                val date = i.date
                val carbon_tanah = i.carbonTanah
                val carbon_tanaman = i.carbonTanaman
                Log.d("data i", carbon_tanah.toString() )
                val marker = MarkerData(
                    latitude = lat.toDouble(),
                    longitude = long.toDouble(),
                    title = "title",
                    snippet = "snip",
                    KodeSampel = idSample,
                    namaLahan = namaLahan,
                    komoditas = comodity,
                    tglSampel = date,
                    carbon_tanah = carbon_tanah,
                    karbonTanaman = carbon_tanaman

                )
                markerList.add(marker)
            }else Log.d("data carbon kosong", "kosong $i " )

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
