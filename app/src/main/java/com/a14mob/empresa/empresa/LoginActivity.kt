package com.a14mob.empresa.empresa

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
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



        cpf.visibility = View.GONE
        login.visibility = View.GONE
        imageView.visibility = View.GONE


        var animacaoIv = AnimationUtils.loadAnimation( this@LoginActivity, R.anim.sequencial)

        animacaoIv.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                login.visibility = View.VISIBLE
                login.clearAnimation()
                login.startAnimation(AnimationUtils.loadAnimation( this@LoginActivity, R.anim.abc_slide_in_bottom))


                login.animation.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        cpf.visibility = View.VISIBLE
                        cpf.clearAnimation()
                        cpf.startAnimation(AnimationUtils.loadAnimation( this@LoginActivity, R.anim.abc_slide_in_top))
                        cpf.requestFocus()

                    }
                    override fun onAnimationStart(animation: Animation?) {

                    }

                })





            }

            override fun onAnimationStart(animation: Animation?) {
                imageView.visibility = View.VISIBLE
            }

        })


        imageView!!.clearAnimation()
        imageView!!.startAnimation(animacaoIv)




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

        Hawk.put("profissional", profissional)
        Hawk.put("CPF", fieldCpf)
        startActivity(intent)

        Handler().postDelayed({defautUI()},2000)



        Toast.makeText(this@LoginActivity,"Bem vindo, ${profissional.nome} !",Toast.LENGTH_SHORT).show()


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
        login.text = "Entrar"
        login.isClickable = true
        cpf.editText?.isEnabled = true
    }
}
