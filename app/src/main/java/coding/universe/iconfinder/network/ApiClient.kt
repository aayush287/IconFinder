package coding.universe.iconfinder.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

//TODO add Api key to test
const val API_KEY = "ADD_YOUR_API_KEY_HERE"

class ApiClient private constructor() {
    private var iconApis: ApiService? = null
    val apis: ApiService?
        get() = instance!!.iconApis

    class AuthenticationInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val builder = original.newBuilder().removeHeader("@")

            builder.header("Authorization", "Bearer $API_KEY")

            builder.header("Content-Type", "application/json")
            val request = builder.build()
            return chain.proceed(request)
        }
    }


    class NeedUpgradeInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.proceed(chain.request())
        }
    }

    companion object {
        private var instance: ApiClient? = null

        fun getInstance(): ApiClient {
            if (instance == null) {
                instance = ApiClient()
            }
            return instance!!
        }
    }

    init {
        synchronized(Retrofit::class.java) {
            val builder = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)

            builder.addInterceptor(AuthenticationInterceptor())
            builder.addInterceptor(NeedUpgradeInterceptor())
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.iconfinder.com/v4/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()
            iconApis = retrofit.create(ApiService::class.java)
        }
    }
}