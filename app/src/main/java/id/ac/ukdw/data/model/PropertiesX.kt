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
    @SerializedName("emisi_lingkungan")
    val emisiLingkungan: String,
    @SerializedName("emisi_tanah")
    val emisiTanah: String,
    @SerializedName("emisi_tanaman")
    val emisiTanaman: String,
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
    val loc: String,
    @SerializedName("NewValue")
    val newValue: String,
    @SerializedName("NewValue1")
    val newValue1: String,
    @SerializedName("NewValue2")
    val newValue2: String
)