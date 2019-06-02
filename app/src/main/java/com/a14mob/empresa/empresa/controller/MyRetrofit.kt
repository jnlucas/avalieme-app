package com.a14mob.empresa.empresa.controller

import com.a14mob.empresa.empresa.PermissionUtils
import com.a14mob.empresa.empresa.retrofit.RetroFitRestAPI
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class MyRetrofit {

    companion object {
        var instance: Retrofit? = null

        fun getIntance(): IActionRetrofit? {
            if (instance == null) {
                instance = retrofitBuilder()
            }

            return instance!!.create<IActionRetrofit>(IActionRetrofit::class.java)!!
        }

        private fun retrofitBuilder(): Retrofit {
            return Retrofit.Builder()
                    .baseUrl("http://api.14mob.com")
                    .client(OkHttpClient.Builder()
                            .addNetworkInterceptor(StethoInterceptor())
                            .build())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                            .setLenient()
                            .create()))
                    .build()
        }

    }


}