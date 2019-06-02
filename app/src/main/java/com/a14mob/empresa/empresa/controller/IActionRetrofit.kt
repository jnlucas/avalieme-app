package com.a14mob.empresa.empresa.controller

import com.a14mob.empresa.empresa.entity.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface IActionRetrofit {

    @FormUrlEncoded()
    @POST("/profissional/api")
    fun buscarProfissional(@Field("cpf") cpf: String): Call<Profissional>



    @GET("/score/{profissional}/{meta}/api")
    fun scoreProfissional(@Path("profissional") profissional: Int,
                          @Path("meta") meta: Int): Call<List<Score>>

    @GET("/score/{profissional}/{meta}/api-avaliacao")
    fun scoreProfissionalAvaliacao(@Path("profissional") profissional: Int,
                                   @Path("meta") meta: Int): Call<List<Avaliacao>>

    @FormUrlEncoded()
    @POST("/profissional/apiToken")
    fun sendTokenProfissional(@Field("cpf") cpf: String, @Field("token") token: String): Call<Profissional>


    @FormUrlEncoded()
    @POST("/profissional/{id}/update")
    fun sendUrlFoto(@Path("id") id: String, @Field("foto") foto: String): Call<Profissional>


    @Multipart
    @POST("/upload/")
    fun postImage(@Part image: MultipartBody.Part): Call<Imagem>
    //resposta fazer novo POST para atualizar o usuario com URL de resposta


    @FormUrlEncoded()
    @POST("/painel/quiz/list")
    fun listQuiz(@Field("profissional") profissional: String): Call<Quiz>


    @FormUrlEncoded()
    @POST("/painel/resposta/responder")
    fun responderQuiz(@Field("profissional") profissional: String, @Field("resposta") resposta: String): Call<Quiz>



}