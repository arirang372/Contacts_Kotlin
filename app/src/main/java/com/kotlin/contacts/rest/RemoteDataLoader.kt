package com.kotlin.contacts.rest

import com.kotlin.contacts.data.DataManager
import rx.subjects.BehaviorSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteDataLoader {
    private val BASE_URL: String = "https://s3.amazonaws.com/"
    private val HTTP_READ_TIMEOUT = 15
    private val HTTP_WRITE_TIMEOUT = 15
    private val HTTP_CONNECT_TIMEOUT = 15
    private val TAG = this.javaClass.simpleName
    private var service: ContactService
    private lateinit var networkInUse: BehaviorSubject<Boolean>

    init {
        var retrofitBuilder: Retrofit.Builder =
                Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)

        var clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        clientBuilder?.run{
             readTimeout(HTTP_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
             writeTimeout(HTTP_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
             connectTimeout(HTTP_CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }

        var intercepter = HttpLoggingInterceptor()
        intercepter.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.addInterceptor(intercepter)

        retrofitBuilder.client(clientBuilder.build())

        service = retrofitBuilder.build().create(ContactService::class.java)
    }

    fun loadAllContacts(dm: DataManager, networkLoading : BehaviorSubject<Boolean>)
            = service?.getContacts()

}