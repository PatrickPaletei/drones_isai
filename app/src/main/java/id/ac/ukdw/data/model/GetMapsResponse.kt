package id.ac.ukdw.data.model


import com.google.gson.annotations.SerializedName

data class GetMapsResponse(
    @SerializedName("body")
    val body: List<Body>,
    @SerializedName("statusCode")
    val statusCode: Int
)