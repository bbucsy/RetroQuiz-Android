package hu.eqn34f.retroquiz.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenTdbNetwork {
    private val retrofit: Retrofit
    val api: OpenTdbApi

    private const val API_URL = "https://opentdb.com/"


    init {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging) // <-- this is the important line!


        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(OpenTdbApi::class.java)

    }

}