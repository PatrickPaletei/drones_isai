package id.ac.ukdw.data.model


import com.google.gson.annotations.SerializedName

data class Feature(
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("properties")
    val properties: PropertiesX,
    @SerializedName("type")
    val type: String
)