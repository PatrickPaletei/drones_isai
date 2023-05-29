package id.ac.ukdw.data.apiHelper

import id.ac.ukdw.data.model.Feature
import id.ac.ukdw.data.model.GetMapsResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    //Get Maps
    @GET("dev/maps")
    fun getMaps():Call<GetMapsResponse>
}