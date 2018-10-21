package com.a14mob.empresa.empresa.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a14mob.empresa.empresa.R

import com.a14mob.empresa.empresa.entity.Resposta
import kotlinx.android.synthetic.main.resposta_item.view.*

class RespostaAdapter(private val respostas: List<Resposta>, private val context: Context) : RecyclerView.Adapter<RespostaAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resposta = respostas[position]

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


        fun bindView(resposta: Resposta) {
            val descricao = itemView.descricao

            descricao.text = resposta.descricao.toString()

        }

    }

}

