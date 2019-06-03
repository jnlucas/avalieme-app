package com.a14mob.empresa.empresa.controller

import com.a14mob.empresa.empresa.entity.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitImpl {


    interface Iresponse {
        fun getResponse(boolean: Boolean, any: Any?, key: KeysResponseAPI)
    }

    lateinit var responseApi: Iresponse


    fun atualizaToken(cpf: String, token: String) {
        MyRetrofit.getIntance()?.sendTokenProfissional(cpf, token)
                ?.enqueue(object : Callback<Profissional> {
                    override fun onResponse(call: Call<Profissional>?, response: Response<Profissional>?) {
                        sendResponse(true, response?.body(), KeysResponseAPI.Token)
                    }

                    override fun onFailure(call: Call<Profissional>?, t: Throwable?) {
                        sendResponse(false, t, KeysResponseAPI.Token)
                    }
                })
    }


    fun scoreProfissional(profissionalId: Int, meta: Int) {
        MyRetrofit.getIntance()?.scoreProfissional(profissionalId, meta)
                ?.enqueue(object : Callback<List<Score>> {
                    override fun onResponse(call: Call<List<Score>>?, response: Response<List<Score>>?) {
                        sendResponse(true, response?.body(), KeysResponseAPI.Score)
                    }

                    override fun onFailure(call: Call<List<Score>>?, t: Throwable?) {
                        sendResponse(false, t, KeysResponseAPI.Score)
                    }
                })
    }


    fun avaliacoesProfissional(profissionalId: Int, meta: Int) {
        MyRetrofit.getIntance()?.scoreProfissionalAvaliacao(profissionalId, meta)
                ?.enqueue(object : Callback<List<Avaliacao>> {
                    override fun onResponse(call: Call<List<Avaliacao>>?, response: Response<List<Avaliacao>>?) {
                        sendResponse(true, response?.body(), KeysResponseAPI.avaliacoesProfissional)
                    }

                    override fun onFailure(call: Call<List<Avaliacao>>?, t: Throwable?) {
                        sendResponse(false, t, KeysResponseAPI.avaliacoesProfissional)
                    }
                })
    }

    fun buscarProfissional(cpf: String) {
        MyRetrofit.getIntance()?.buscarProfissional(cpf)
                ?.enqueue(object : Callback<Profissional> {
                    override fun onResponse(call: Call<Profissional>?, response: Response<Profissional>?) {
                        sendResponse(true, response?.body(), KeysResponseAPI.buscaProfissional)
                    }

                    override fun onFailure(call: Call<Profissional>?, t: Throwable?) {
                        sendResponse(false, t, KeysResponseAPI.buscaProfissional)
                    }

                })
    }


    fun enviarFoto(body: MultipartBody.Part) {
        MyRetrofit.getIntance()?.postImage(body)
                ?.enqueue(object : Callback<Imagem> {
                    override fun onResponse(call: Call<Imagem>?, response: Response<Imagem>?) {
                        sendResponse(true, response?.body(), KeysResponseAPI.envioFotoPerfil)
                    }

                    override fun onFailure(call: Call<Imagem>?, t: Throwable?) {
                        sendResponse(false, t, KeysResponseAPI.envioFotoPerfil)
                    }
                })
    }

    fun enviarUrlFoto(id: Int, url: String) {
        MyRetrofit.getIntance()?.sendUrlFoto(id.toString(), url)
                ?.enqueue(object : Callback<Profissional> {
                    override fun onResponse(call: Call<Profissional>?, response: Response<Profissional>?) {
                        sendResponse(true, response?.body(), KeysResponseAPI.envioUrlFoto)

                    }

                    override fun onFailure(call: Call<Profissional>?, t: Throwable?) {
                        sendResponse(false, t, KeysResponseAPI.envioUrlFoto)
                    }
                })
    }


    fun enviarRespostaQuizz(profissionalId: String, idResposta: String) {
        MyRetrofit.getIntance()?.responderQuiz(profissionalId, idResposta)
                ?.enqueue(object : Callback<Quiz> {
                    override fun onResponse(call: Call<Quiz>?, response: Response<Quiz>?) {
                        sendResponse(true, response?.body(), KeysResponseAPI.enviarRespostaQuizz)

                    }

                    override fun onFailure(call: Call<Quiz>?, t: Throwable?) {
                        sendResponse(false, t, KeysResponseAPI.enviarRespostaQuizz)

                    }
                })
    }


    fun buscaQuizz(profissionalId: String) {
        MyRetrofit.getIntance()?.listQuiz(profissionalId)
        ?.enqueue(object : Callback<Quiz> {
            override fun onResponse(call: Call<Quiz>?, response: Response<Quiz>?) {
                sendResponse(true, response?.body(), KeysResponseAPI.buscaQuizz)

            }

            override fun onFailure(call: Call<Quiz>?, t: Throwable?) {
                sendResponse(false, t, KeysResponseAPI.buscaQuizz)
            }
        })

    }

    fun sendResponse(b: Boolean, any: Any?, key: KeysResponseAPI) {
            if (responseApi != null) {
                responseApi.getResponse(b, any, key)
            }


    }


}