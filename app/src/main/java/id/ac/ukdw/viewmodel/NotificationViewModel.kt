package id.ac.ukdw.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.ukdw.data.apiHelper.ApiClient
import id.ac.ukdw.data.apiHelper.ApiService
import id.ac.ukdw.data.model.GetMapsResponse
import id.ac.ukdw.services.LocationData
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NotificationViewModel : ViewModel() {

    private val apiClient: ApiService = ApiClient.instance

    // Define LiveData for the filtered data
    private val _filteredData = MutableLiveData<List<LocationData>>()
    val filteredData: LiveData<List<LocationData>> get() = _filteredData

    fun fetchDataLocation() {
        viewModelScope.launch {
            try {
                val response: Response<GetMapsResponse> = apiClient.getMaps()

                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        // Log the raw API response
                        Log.d("FetchDataLocation", "Raw API Response: $body")

                        val filteredData = filterData(it)
                        // Log the filtered data before updating the LiveData
                        Log.d("FetchDataLocation", "Filtered Data: $filteredData")

                        // Update the LiveData
                        _filteredData.value = filteredData
                    }
                }
            } catch (e: IOException) {
                // Log the error
                Log.e("FetchDataLocation", "Error fetching data: $e")
                // Handle the error
                // You might want to expose an error LiveData for observing in the UI
            }
        }
    }


    private fun filterData(response: GetMapsResponse): List<LocationData> {
        val result = mutableListOf<LocationData>()

        for (item in response.body) {
            val filteredMap = LocationData(
                item.lat.toDouble(),
                item.long.toDouble(),
                item.idSample,
                item.date
            )
            result.add(filteredMap)
        }

        return result
    }


}