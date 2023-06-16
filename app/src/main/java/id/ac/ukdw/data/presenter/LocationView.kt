package id.ac.ukdw.data.presenter

import MarkerData

interface LocationView {
    fun isMapReady(): Boolean
    fun displayMarkers(markerList: List<MarkerData>)
    fun updateMarkers(filteredMarkers: List<MarkerData>)

    // Other methods as needed
}
