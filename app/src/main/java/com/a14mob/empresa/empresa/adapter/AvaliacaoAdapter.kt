package com.a14mob.empresa.empresa.adapter

import android.content.Context

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.a14mob.empresa.empresa.R
import com.a14mob.empresa.empresa.entity.Avaliacao


import kotlinx.android.synthetic.main.avaliacao_item.view.*



class AvaliacaoAdapter(private val avaliacoes: List<Avaliacao>, private val context: Context) : Adapter<AvaliacaoAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val avaliacao = avaliacoes[position]


        holder.itemView.startAnimation(AnimationUtils.loadAnimation( context, R.anim.animacao_fragment_out))


        holder?.let {
            it.nomeAvaliador.text = avaliacao.superiorImediato
            it.observacao.text = avaliacao.observacao
            it.ratingBar.rating = avaliacao.pontos

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.avaliacao_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {


        return avaliacoes.size
    }





    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nomeAvaliador = itemView.nomeAvaliador
        val observacao = itemView.observacao
        val pontuacao = itemView.pontuacao
        val ratingBar = itemView.ratingBar

        fun bindView(avaliacao: Avaliacao) {
            val nomeAvaliador = itemView.nomeAvaliador
            val observacao = itemView.observacao
            val pontuacao = itemView.pontuacao
            val ratingBar = itemView.ratingBar


            nomeAvaliador.text = avaliacao.superiorImediato.toString()
            observacao.text = avaliacao.observacao.toString()
            pontuacao.text = avaliacao.pontos.toString()
            ratingBar.rating = avaliacao.pontos
        }

    }

}

