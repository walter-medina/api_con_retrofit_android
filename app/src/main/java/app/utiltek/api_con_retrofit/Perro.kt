package app.utiltek.api_con_retrofit

import com.google.gson.annotations.SerializedName
//objeto perro
data class Perro(
    @SerializedName("status") var status: String,
    @SerializedName("message") var images: List<String>
)