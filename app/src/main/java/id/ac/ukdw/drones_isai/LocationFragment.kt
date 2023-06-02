package id.ac.ukdw.drones_isai

import MarkerData
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.ac.ukdw.data.apiHelper.ApiClient
import id.ac.ukdw.data.model.GetMapsResponse
import id.ac.ukdw.data.presenter.LocationPresenter
import id.ac.ukdw.data.presenter.LocationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LocationFragment : Fragment(), OnMapReadyCallback, LocationView {

    private lateinit var presenter: LocationPresenter
    private lateinit var googleMap: GoogleMap
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LocationPresenter(this)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupSearchView(view)

        presenter.loadData()
    }

    // Implement LocationView interface methods
    override fun isMapReady(): Boolean {
        return ::googleMap.isInitialized
    }

    override fun displayMarkers(markerList: List<MarkerData>) {
        // Your existing displayMarkers() implementation goes here
        val boundsBuilder = LatLngBounds.Builder()

        for (markerData in markerList) {
            if (markerData.latitude != 0.0 && markerData.longitude != 0.0
                && markerData.komoditas != "null" && markerData.namaLahan != "null"
                && markerData.karbonTanah != "null" && markerData.karbonTanaman != "null"
                && markerData.KodeSampel != "null" && markerData.tglSampel != "null"
            ) {
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

    override fun updateMarkers(filteredMarkers: List<MarkerData>) {
        // Your existing searchMarkers() implementation goes here
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

    // Other methods in LocationFragment
    private fun setupSearchView(view: View) {
        searchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.searchMarkers(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.searchMarkers(newText)
                return true
            }
        })

        searchView.isIconified = false
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
    }


}














