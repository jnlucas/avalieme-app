package com.a14mob.empresa.empresa

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.a14mob.empresa.empresa.entity.Profissional
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_login.*
import com.a14mob.empresa.empresa.controller.KeysResponseAPI
import com.a14mob.empresa.empresa.controller.RetrofitImpl
import com.orhanobut.hawk.Hawk


class LoginActivity : AppCompatActivity(), RetrofitImpl.Iresponse {


    var fieldCpf: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        cpf.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                msgErro.text = ""
            }

        })

        Stetho.initializeWithDefaults(this)

        login.setOnClickListener {

            verificarLogin()
        }


    }

    fun verificarLogin() {

        fieldCpf = cpf.editText?.text.toString()

        login.text = "Verificando..."
        cpf.isEnabled = false
        login.isClickable = false
        verificaProfissional()


    }

    fun carregarInformacoes(profissional: Profissional) {


        Hawk.init(applicationContext).build()

//        val intent = Intent(this,MainActivity::class.java)
//        intent.putExtra("cpf",fieldCpf.toString());
//
//        val editor = getSharedPreferences("PROFISSIONAL", MODE_PRIVATE).edit()
//        editor.putString("nome", response.nome)
//        editor.putString("meta", response.meta)
//        editor.putString("profissionalId", response.id.toString())
//        editor.putString("foto", response.foto.toString())
//
//        editor.putString("cpf",fieldCpf)
//
//        editor.apply()

        Hawk.put("profissional", profissional)
        Hawk.put("CPF", fieldCpf)
        startActivity(intent)




        startActivity(Intent(this@LoginActivity, MainActivity::class.java))

    }

    fun verificaProfissional() {
        var retrofitImpl = RetrofitImpl()
        retrofitImpl.responseApi = this@LoginActivity
        retrofitImpl.buscarProfissional(cpf.editText?.text.toString())
    }

    override fun getResponse(boolean: Boolean, any: Any?, key: KeysResponseAPI) {

        if (boolean)
            if ((any as Profissional?)?.id != null) {
                carregarInformacoes(any!!)
            } else {
                msgErro.text = "Profissional não encontrado!"
                defautUI()
            }
        else {
            defautUI()
            Log.i("buscarProfissional", any.toString())
        }

    }

    private fun defautUI() {
        msgErro.text = "Profissional não encontrado!"
        login.text = "Entrar"
        login.isClickable = true
        cpf.editText?.isEnabled = true
    }
}
