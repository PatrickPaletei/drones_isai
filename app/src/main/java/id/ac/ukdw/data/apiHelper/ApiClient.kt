package id.ac.ukdw.data.apiHelper

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    //base url api
    const val BASE_URL = "https://sk695s0owe.execute-api.us-east-1.amazonaws.com/"
    const val BASE_URL_NEW = "https://f7p2q26auk56oxdvp2sjaf6ioy0sximk.lambda-url.us-east-1.on.aws"

    private val logging: HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    val instance: ApiService by lazy {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(
                    client
                ).build()
        retrofit.create(ApiService::class.java)
    }
    private val client2 = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    val instance2: ApiService by lazy {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL_NEW).addConverterFactory(GsonConverterFactory.create())
                .client(
                    client
                ).build()
        retrofit.create(ApiService::class.java)
    }

}