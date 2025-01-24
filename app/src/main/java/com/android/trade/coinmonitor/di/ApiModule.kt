package com.android.trade.coinmonitor.di

import com.android.trade.coinmonitor.BuildConfig
import com.android.trade.data.remote.network.service.BinanceService
import com.android.trade.data.remote.network.service.BithumbService
import com.android.trade.data.remote.network.service.BybitService
import com.android.trade.data.remote.network.service.UpbitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(provideLoggingInterceptor())
            .build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor ()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return interceptor
    }

    private fun createRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @UpbitRetrofit
    @Provides
    fun provideUpbitRetrofit(okHttpClient: OkHttpClient) =
        createRetrofit(okHttpClient, "https://api.upbit.com/")

    @Singleton
    @Provides
    fun provideUpbitService(@UpbitRetrofit retrofit: Retrofit): UpbitService =
        retrofit.create(UpbitService::class.java)

    @Singleton
    @BithumbRetrofit
    @Provides
    fun provideBithumbRetrofit(okHttpClient: OkHttpClient) =
        createRetrofit(okHttpClient, "https://api.bithumb.com")

    @Singleton
    @Provides
    fun provideBithumbService(@BithumbRetrofit retrofit: Retrofit): BithumbService =
        retrofit.create(BithumbService::class.java)

    @Singleton
    @BinanceRetrofit
    @Provides
    fun provideBinanceRetrofit(okHttpClient: OkHttpClient) =
        createRetrofit(okHttpClient, "https://api.binance.com/")

    @Singleton
    @Provides
    fun provideBinanceService(@BinanceRetrofit retrofit: Retrofit): BinanceService =
        retrofit.create(BinanceService::class.java)

    @Singleton
    @BybitRetrofit
    @Provides
    fun provideBybitRetrofit(okHttpClient: OkHttpClient) =
        createRetrofit(okHttpClient, "https://api.bybit.com/")

    @Singleton
    @Provides
    fun provideBybitService(@BybitRetrofit retrofit: Retrofit): BybitService =
        retrofit.create(BybitService::class.java)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UpbitRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BithumbRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BinanceRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BybitRetrofit
