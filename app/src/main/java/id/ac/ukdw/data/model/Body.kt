package id.ac.ukdw.data.model


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("crs")
    val crs: Crs,
    @SerializedName("features")
    val features: List<Feature>,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)