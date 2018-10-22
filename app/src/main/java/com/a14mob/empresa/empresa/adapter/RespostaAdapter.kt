package com.a14mob.empresa.empresa.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.a14mob.empresa.empresa.MainActivity
import com.a14mob.empresa.empresa.PermissionUtils
import com.a14mob.empresa.empresa.R
import com.a14mob.empresa.empresa.entity.Quiz

import com.a14mob.empresa.empresa.entity.Resposta
import com.a14mob.empresa.empresa.fragments.QuizFragment
import com.a14mob.empresa.empresa.fragments.ScoreFragment
import kotlinx.android.synthetic.main.resposta_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v4.app.FragmentActivity
import com.a14mob.empresa.empresa.fragments.AvaliacoesFragment


class RespostaAdapter(private val respostas: List<Resposta>, private val context: Context) : RecyclerView.Adapter<RespostaAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resposta = respostas[position]

        holder?.onclick(context, position,resposta)


        holder?.let {
            it.descricao.text =  resposta.descricao



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.resposta_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {


        return respostas.size
    }





    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val descricao = itemView.descricao
        var profissionalId: Int = 0

        fun onclick(context: Context, position: Int,resp:Resposta) {


            val inflater = LayoutInflater.from(context)

            val prefs = inflater.context.getSharedPreferences("PROFISSIONAL", Context.MODE_PRIVATE)

            profissionalId = prefs.getString("profissionalId", null).toInt()

            descricao.setOnClickListener {

                enviarResposta(context,resp)

            }}


        fun enviarResposta(context:Context,resposta: Resposta){

            PermissionUtils.api.responderQuiz(profissionalId.toString(),resposta.id.toString())
                    .enqueue(object : Callback<Quiz> {
                        override fun onResponse(call: Call<Quiz>?, response: Response<Quiz>?) {

                            val resposta = response?.body()

                            if (resposta != null) {

                                //context@QuizFragment().quiz(profissionalId.toInt(), 1)

                                Toast.makeText(context,"Obrigado! Sua opnião é muito importante para nós.",Toast.LENGTH_LONG).show()
                            }

                        }

                        override fun onFailure(call: Call<Quiz>?, t: Throwable?) {

                            Toast.makeText(context,"falhou",Toast.LENGTH_LONG).show()
                        }
                    })

        }


        fun bindView(resposta: Resposta) {
            val descricao = itemView.descricao
            val idResposta = resposta.id


            descricao.text = resposta.descricao.toString()

        }

    }

}


