package id.ac.ukdw.data.apiHelper


import id.ac.ukdw.data.model.GetMapsResponse

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    //Get Maps
    @GET("dev")
    suspend fun getMaps(): Response<GetMapsResponse>

}