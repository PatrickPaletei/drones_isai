package id.ac.ukdw.drones_isai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.ac.ukdw.data.MarkerData


class LocationFragment : Fragment(), OnMapReadyCallback {

    // Your list of marker data
    private val markerList = listOf(
        MarkerData(
            37.7749,
            -122.4194,
            "Marker 1",
            "Snippet 1",
            "KL-31",
            "Lahan Jogja Siklus 1",
            "Padi",
            "14/11/2021",
            "2.22",
            "2.22"
        ),MarkerData(
            37.7749,
            -123.4194,
            "Marker 2",
            "Snippet 2",
            "KL-32",
            "Lahan Jogja Siklus 2",
            "Padi",
            "15/11/2021",
            "2.22",
            "2.22"
        )
        // Add more marker data as needed
    )

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

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

            boundsBuilder.include(LatLng(markerData.latitude, markerData.longitude)) // Include marker position in the bounds
        }

        // Focus the camera on all the markers
        val bounds = boundsBuilder.build() // Build the LatLngBounds

        val padding = resources.getDimensionPixelSize(R.dimen.map_padding) // Set padding around the bounds (if needed)
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


}