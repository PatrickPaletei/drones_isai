package id.ac.ukdw.drones_isai.ui

import MarkerData
import android.annotation.SuppressLint
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


import id.ac.ukdw.data.presenter.LocationPresenter
import id.ac.ukdw.data.presenter.LocationView
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.drones_isai.databinding.FragmentLocationBinding


class LocationFragment : Fragment(), OnMapReadyCallback, LocationView {

    private lateinit var presenter: LocationPresenter
    private lateinit var googleMap: GoogleMap
    private lateinit var searchView: SearchView

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LocationPresenter(this)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupSearchView()

        presenter.loadData()
        binding.switchMap.setOnClickListener {
            if (googleMap.mapType == GoogleMap.MAP_TYPE_NORMAL) {
                googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            } else {
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }
    }


    // Implement LocationView interface methods
    override fun isMapReady(): Boolean {
        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        return ::googleMap.isInitialized

    }
    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
    }

    @SuppressLint("InflateParams")
    override fun displayMarkers(markerList: List<MarkerData>) {
        val boundsBuilder = LatLngBounds.Builder()
        Log.d("marker", "displayMarkers: $markerList")
        for (markerData in markerList) {

            if (markerData.latitude != 0.0 && markerData.longitude != 0.0
                && markerData.komoditas != "null" && markerData.namaLahan != "null"
                && markerData.karbonTanah != "null" && markerData.karbonTanaman != "null"
                && markerData.KodeSampel != "null" && markerData.tglSampel != "null"
            ) {
                val markerOptions = MarkerOptions()
                    .position(LatLng(markerData.latitude, markerData.longitude))
                googleMap.addMarker(markerOptions)

                boundsBuilder.include(
                    LatLng(markerData.latitude, markerData.longitude))

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

    private fun setupSearchView() {
        searchView = binding.searchView
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

        searchView.isIconified = true
    }





}














