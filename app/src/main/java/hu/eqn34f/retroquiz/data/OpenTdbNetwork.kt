package hu.eqn34f.retroquiz.data

import hu.eqn34f.retroquiz.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenTdbNetwork {
    private val retrofit: Retrofit
    val api: OpenTdbApi
    private const val API_URL = "https://opentdb.com/"

    init {
        val httpClient = OkHttpClient.Builder()

        // this is for debug purposes
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        // build retrofit api
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(OpenTdbApi::class.java)
    }
}