package com.duyp.architecture.clean.redux.data.di

import android.content.Context
import com.duyp.architecture.clean.redux.data.BuildConfig
import com.duyp.architecture.clean.redux.data.api.ApiConstants
import com.duyp.architecture.clean.redux.data.api.GithubDateDeserializer
import com.duyp.architecture.clean.redux.data.api.GithubResponseConverter
import com.duyp.architecture.clean.redux.data.api.interceptors.ContentTypeInterceptor
import com.duyp.architecture.clean.redux.data.api.interceptors.PaginationInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object ApiModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapter(
                Date::class.java,
                GithubDateDeserializer()
            )
            .create()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context): OkHttpClient {
        // okHttp client
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(ApiConstants.TIME_OUT_API.toLong(), TimeUnit.SECONDS)
            .writeTimeout(ApiConstants.TIME_OUT_API.toLong(), TimeUnit.SECONDS)
            .readTimeout(ApiConstants.TIME_OUT_API.toLong(), TimeUnit.SECONDS)
            .addInterceptor(ContentTypeInterceptor())
            .addInterceptor(PaginationInterceptor())
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)

        // add logging interceptor
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            // only log network calls (no log for force cache which isn't through network)
            clientBuilder.addNetworkInterceptor(logging)
        }

        // cache
        var cache: Cache? = null
        try {
            val httpCacheDirectory = File(context.cacheDir, "httpCache")
            cache = Cache(httpCacheDirectory, (10 * 1024 * 1024).toLong()) // 10 MB
        } catch (ignored: Exception) {
        }

        if (cache != null) {
            clientBuilder
                .cache(cache)
        }

        return clientBuilder.build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson) =
        Retrofit.Builder()
            .baseUrl(BuildConfig.REST_URL)
            .client(okHttpClient)
            .addConverterFactory(GithubResponseConverter(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
}
