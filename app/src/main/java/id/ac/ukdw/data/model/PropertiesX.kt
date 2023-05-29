package id.ac.ukdw.data.model


import com.google.gson.annotations.SerializedName

data class PropertiesX(
    @SerializedName("carbon_tanah")
    val carbonTanah: String,
    @SerializedName("carbon_tanaman")
    val carbonTanaman: String,
    @SerializedName("comodity")
    val comodity: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("hst")
    val hst: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("id_sample")
    val idSample: String,
    @SerializedName("link_nir")
    val linkNir: String,
    @SerializedName("link_rgb")
    val linkRgb: String,
    @SerializedName("loc")
    val loc: String
)