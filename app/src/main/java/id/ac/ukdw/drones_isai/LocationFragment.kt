package id.ac.ukdw.drones_isai

import MarkerData
import android.os.Bundle
<<<<<<< HEAD
import android.os.Handler
import android.util.Log
=======
>>>>>>> main
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.Api
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
<<<<<<< HEAD
import id.ac.ukdw.data.apiHelper.ApiClient
import id.ac.ukdw.data.model.GetMapsResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
=======
import id.ac.ukdw.data.MarkerData
>>>>>>> main


class LocationFragment : Fragment(), OnMapReadyCallback {

    // Your list of marker data

//    private val markerList = listOf(
//        MarkerData(
//            -7.776756032841577,
//            110.42144480217401,
//            "Marker 1",
//            "Snippet 1",
//            "KL-31",
//            "Lahan Jogja Siklus 1",
//            "Padi",
//            "14/11/2021",
//            "2.22",
//            "2.22"
//        ), MarkerData(
//            -7.762672251441492,
//            110.39508049474928,
//            "Marker 2",
//            "Snippet 2",
//            "KL-32",
//            "Lahan Jogja Siklus 2",
//            "Cabe",
//            "15/11/2021",
//            "2.22",
//            "2.22"
//        )
//        // Add more marker data as needed
//    )
    val markerList = mutableListOf<MarkerData>()
    private lateinit var googleMap: GoogleMap
    private lateinit var searchView: SearchView
    private var dataLoaded = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupSearchView(view)

        loadData()
    }

<<<<<<< HEAD

    private fun loadData() {
        ApiClient.instance.getMaps().enqueue(object : Callback<GetMapsResponse> {
            override fun onResponse(
                call: Call<GetMapsResponse>,
                response: Response<GetMapsResponse>
            ) {
                val code = response.code()
                val body = response.body()
                if (code == 200) {
                    if (body != null) {
                        val coordinat = body.body
                        for (i in coordinat) {
                            val marker = MarkerData(
                                i.long.toDouble(),
                                i.lat.toDouble(),
                                "marker ",
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


    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        checkDataAndMapReady()
    }


    private fun checkDataAndMapReady() {
        if (dataLoaded && ::googleMap.isInitialized) {
            displayMarkers()
=======
    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        // Add markers from the markerList
        val boundsBuilder = LatLngBounds.Builder() // Create a LatLngBounds builder

        // Add markers from the markerList
        for (markerData in markerList) {
            val markerOptions = MarkerOptions()
                .position(LatLng(markerData.latitude, markerData.longitude))
                .title(markerData.title)
                .snippet(markerData.snippet)
            googleMap.addMarker(markerOptions)

            boundsBuilder.include(
                LatLng(
                    markerData.latitude,
                    markerData.longitude
                )
            ) // Include marker position in the bounds
        }

        // Focus the camera on all the markers
        val bounds = boundsBuilder.build() // Build the LatLngBounds

        val padding =
            resources.getDimensionPixelSize(R.dimen.map_padding) // Set padding around the bounds (if needed)
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        googleMap.moveCamera(cameraUpdate)

        // Set a marker click listener
        googleMap.setOnMarkerClickListener { marker ->
            val markerData = markerList.find { data ->
                data.latitude == marker.position.latitude && data.longitude == marker.position.longitude
            }

            // Show a BottomSheetDialog when a marker is clicked
            if (markerData != null) {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val view = layoutInflater.inflate(R.layout.layout_popup_dialog, null)

                // Access the views in the bottom sheet layout
                val kodeSampelTextView = view.findViewById<TextView>(R.id.kode_sample)
                val namaLahanTextView = view.findViewById<TextView>(R.id.kode_lahan)
                val komoditasTextView = view.findViewById<TextView>(R.id.komoditas)
                val tglSampelTextView = view.findViewById<TextView>(R.id.tanggal_sampling)
                val karbonTanahTextView = view.findViewById<TextView>(R.id.karbonTanah)
                val karbonTanamahTextView = view.findViewById<TextView>(R.id.karbon_tanaman)

                // Set the data from the markerData to the views
                kodeSampelTextView.text = markerData.KodeSampel
                namaLahanTextView.text = markerData.namaLahan
                komoditasTextView.text = markerData.komoditas
                tglSampelTextView.text = markerData.tglSampel
                karbonTanahTextView.text = markerData.karbonTanah
                karbonTanamahTextView.text = markerData.karbonTanah

                bottomSheetDialog.setContentView(view)
                bottomSheetDialog.show()
            }

            // Return true to consume the event and prevent the default behavior (e.g., showing the info window)
            true
        }
    }

    private fun searchMarkers(query: String) {
        val filteredMarkers = markerList.filter { markerData ->
            markerData.namaLahan.contains(query, ignoreCase = true) ||
                    markerData.komoditas.contains(query, ignoreCase = true)
>>>>>>> main
        }
    }

    private fun displayMarkers() {
        val boundsBuilder = LatLngBounds.Builder()

        for (markerData in markerList) {
            val markerOptions = MarkerOptions()
                .position(LatLng(markerData.latitude, markerData.longitude))
                .title(markerData.title)
                .snippet(markerData.snippet)
            googleMap.addMarker(markerOptions)

            boundsBuilder.include(
                LatLng(
                    markerData.latitude,
                    markerData.longitude
                )
            )
        }

        val bounds = boundsBuilder.build()
        val padding = resources.getDimensionPixelSize(R.dimen.map_padding)
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        googleMap.moveCamera(cameraUpdate)

        googleMap.setOnMarkerClickListener { marker ->
            val markerData = markerList.find { data ->
                data.latitude == marker.position.latitude && data.longitude == marker.position.longitude
            }

            if (markerData != null) {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val view = layoutInflater.inflate(R.layout.layout_popup_dialog, null)

                val kodeSampelTextView = view.findViewById<TextView>(R.id.kode_sample)
                val namaLahanTextView = view.findViewById<TextView>(R.id.kode_lahan)
                val komoditasTextView = view.findViewById<TextView>(R.id.komoditas)
                val tglSampelTextView = view.findViewById<TextView>(R.id.tanggal_sampling)
                val karbonTanahTextView = view.findViewById<TextView>(R.id.karbonTanah)
                val karbonTanamahTextView = view.findViewById<TextView>(R.id.karbon_tanaman)

                kodeSampelTextView.text = markerData.KodeSampel
                namaLahanTextView.text = markerData.namaLahan
                komoditasTextView.text = markerData.komoditas
                tglSampelTextView.text = markerData.tglSampel
                karbonTanahTextView.text = markerData.karbonTanah
                karbonTanamahTextView.text = markerData.karbonTanah

                bottomSheetDialog.setContentView(view)
                bottomSheetDialog.show()
            }

            true
        }
    }


    private fun setupSearchView(view: View) {
        searchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchMarkers(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchMarkers(newText)
                return true
            }
        })

        searchView.isIconified = false
    }

    private fun searchMarkers(query: String) {
        val filteredMarkers = markerList.filter { markerData ->
            markerData.namaLahan.contains(query, ignoreCase = true) ||
                    markerData.komoditas.contains(query, ignoreCase = true)
        }

        googleMap.clear()

        for (markerData in filteredMarkers) {
            val markerOptions = MarkerOptions()
                .position(LatLng(markerData.latitude, markerData.longitude))
                .title(markerData.title)
                .snippet(markerData.snippet)
            googleMap.addMarker(markerOptions)
        }

        if (filteredMarkers.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()
            for (markerData in filteredMarkers) {
                boundsBuilder.include(LatLng(markerData.latitude, markerData.longitude))
            }
            val bounds = boundsBuilder.build()
            val padding = resources.getDimensionPixelSize(R.dimen.map_padding)
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            googleMap.moveCamera(cameraUpdate)
        }
    }



}













