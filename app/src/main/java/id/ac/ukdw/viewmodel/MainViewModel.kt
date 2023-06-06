package id.ac.ukdw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.ukdw.data.apiHelper.ApiClient
import id.ac.ukdw.data.apiHelper.ApiService
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.data.model.GetMapsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val dataCache: HashMap<String, List<Body>> = HashMap()
    private val apiClient: ApiService = ApiClient.instance

    private val _responseData: MutableLiveData<List<Body>?> = MutableLiveData()
    val responseData: MutableLiveData<List<Body>?>
        get() = _responseData

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun fetchData() {
        _isLoading.value = true

        val cachedData = dataCache[CACHE_KEY]
        if (cachedData != null) {
            _responseData.value = cachedData
            _isLoading.value = false
        } else {
            viewModelScope.launch {
                try {
                    val response: Response<GetMapsResponse> = apiClient.getMaps()
                    val statusCode = response.code()
                    val body = response.body()
                    if (statusCode == 200) {
                        if (body != null) {
                            val responseData = body.body
                            dataCache[CACHE_KEY] = responseData
                            _responseData.value = responseData
                        }
                    } else {
                        _errorMessage.value = "Failed to fetch data: $statusCode"
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "Failed to fetch data: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
    companion object {
        private const val CACHE_KEY = "cached_data"
    }
}


