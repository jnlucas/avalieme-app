package com.a14mob.empresa.empresa

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.a14mob.empresa.empresa.entity.Profissional
import com.a14mob.empresa.empresa.retrofit.RetroFitRestAPI
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import android.content.SharedPreferences
import com.google.firebase.iid.FirebaseInstanceId
import com.orhanobut.hawk.Hawk
import com.orhanobut.hawk.Hawk.put
import java.util.jar.Manifest


class LoginActivity : AppCompatActivity() {

    var fieldCpf: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Stetho.initializeWithDefaults(this);

        login.setOnClickListener {

            verificarLogin()
        }



    }

    fun verificarLogin(){

        var  liberado: Int = verificaProfissional();

        login.text = "Verificando..."
        login.isClickable = false


    }

    fun carregarInformacoes(response: Profissional){

        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("cpf",fieldCpf.toString());

        val editor = getSharedPreferences("PROFISSIONAL", MODE_PRIVATE).edit()
        editor.putString("nome", response.nome)
        editor.putString("meta", response.meta)
        editor.putString("profissionalId", response.id.toString())
        editor.putString("foto", response.foto.toString())

        editor.putString("cpf",fieldCpf)

        editor.apply()



        startActivity(intent)
    }

    fun verificaProfissional(): Int {

        fieldCpf = cpf.editText?.text.toString()

        PermissionUtils.api.buscarProfissional(fieldCpf)
                .enqueue(object : Callback<Profissional> {
                    override fun onResponse(call: Call<Profissional>?, response: Response<Profissional>?) {


                        if(response?.body()?.nome != null){
                            this@LoginActivity.carregarInformacoes(response?.body() as Profissional)

                        }else{
                            msgErro.text = "Profissional nao encontrado!"
                        }
                        login.text = "Entrar"
                        login.isClickable = true


                    }

                    override fun onFailure(call: Call<Profissional>?, t: Throwable?) {

                        msgErro.text = "Profissional nao encontrado!"
                        login.text = "Entrar"
                        login.isClickable = true

                    }
                })


        return 1;
    }
}
