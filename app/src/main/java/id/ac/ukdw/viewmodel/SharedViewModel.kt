package id.ac.ukdw.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.ukdw.data.model.GetMapsResponse

class SharedViewModel : ViewModel() {

    private val _mapsResponse = MutableLiveData<GetMapsResponse>()
    val mapsResponse: MutableLiveData<GetMapsResponse> = _mapsResponse

    fun setMapsResponse(response: GetMapsResponse) {
        _mapsResponse.value = response
    }
}