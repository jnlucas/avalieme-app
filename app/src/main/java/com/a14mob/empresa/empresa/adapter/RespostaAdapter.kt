package com.a14mob.empresa.empresa.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.a14mob.empresa.empresa.R

import com.a14mob.empresa.empresa.entity.Resposta
import kotlinx.android.synthetic.main.resposta_item.view.*
import com.a14mob.empresa.empresa.controller.KeysResponseAPI
import com.a14mob.empresa.empresa.controller.RetrofitImpl
import com.a14mob.empresa.empresa.entity.Profissional
import com.orhanobut.hawk.Hawk


class RespostaAdapter(private val respostas: List<Resposta>, var context: Context) : RecyclerView.Adapter<RespostaAdapter.ViewHolder>() {


    interface OnItemClickListener {
        fun OnClick(resposta: Resposta)
    }

    lateinit var mClick: OnItemClickListener


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resposta = respostas[position]



        holder.itemView.setOnClickListener {
            if (mClick != null) {
                mClick.OnClick(resposta)
            }
        }



        holder?.let {
            it.descricao.text = resposta.descricao
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.resposta_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return respostas.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val descricao = itemView.descricao
        lateinit var context: Context


        fun bindView(resposta: Resposta) {
            val descricao = itemView.descricao
            val idResposta = resposta.id
            descricao.text = resposta.descricao.toString()

        }

    }


}


