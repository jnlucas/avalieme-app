package com.a14mob.empresa.empresa.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.a14mob.empresa.empresa.MainActivity
import com.a14mob.empresa.empresa.PermissionUtils


import com.a14mob.empresa.empresa.R
import com.a14mob.empresa.empresa.adapter.AvaliacaoAdapter
import com.a14mob.empresa.empresa.controller.KeysResponseAPI
import com.a14mob.empresa.empresa.controller.RetrofitImpl
import com.a14mob.empresa.empresa.entity.Avaliacao
import com.a14mob.empresa.empresa.entity.Profissional
import com.hendraanggrian.pikasso.picasso
import com.orhanobut.hawk.Hawk
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_avaliacoes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class AvaliacoesFragment : Fragment(), RetrofitImpl.Iresponse {

    //    var nome: String = ""
//    var profissionalId: Int = 0
//    var meta: Int = 0
//    var foto: String = ""
//    var cpf = ""
//    lateinit var prefs: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


//        prefs = inflater.context.getSharedPreferences("PROFISSIONAL", Context.MODE_PRIVATE)
//
//        profissionalId = prefs.getString("profissionalId", null).toInt()
//
//        meta = prefs.getString("meta", null).toInt()
//
//        nome = prefs.getString("nome", null).toString()

        //carregaFoto()

//        cpf = prefs.getString("cpf", null).toString()


        return inflater?.inflate(R.layout.fragment_avaliacoes, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        nomeProfissional.text = (activity as MainActivity).profissional.nome


        buscarAvaliacoes()

        imgProfissional.setOnClickListener {
            (activity as MainActivity).buscaFoto()
        }


    }

    private fun buscarAvaliacoes() {
        var profissional = (activity as MainActivity).profissional
        var retrofitImpl = RetrofitImpl()
        retrofitImpl.responseApi = this@AvaliacoesFragment
        retrofitImpl.avaliacoesProfissional(profissional.id, profissional.meta.toInt())

    }

    override fun getResponse(boolean: Boolean, any: Any?, key: KeysResponseAPI) {

        if (boolean) {
            any.let {
                carregarInformacoes(any as List<Avaliacao>)
            }
        } else {
            mandaToast("Algum Erro ao Carregar!")
            Log.i("List<Avaliacao>", any.toString())
        }
    }

    private fun mandaToast(msg: String) {
        Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
    }


    private lateinit var profissional: Profissional

    override fun onResume() {
        super.onResume()
        profissional = (activity as MainActivity).profissional
        carregaFoto()

    }

    fun carregaFoto() {
        Picasso.get().load(profissional.foto)
                .placeholder(R.drawable.boy)
                .error(R.drawable.boy)
                .into(imgProfissional)
    }


    fun carregarInformacoes(avaliacoes: List<Avaliacao>) {


        avaliacoes_list_recyclerview
                .adapter = AvaliacaoAdapter(avaliacoes, this@AvaliacoesFragment.context!!)

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        avaliacoes_list_recyclerview
                .layoutManager = layoutManager

    }

    fun sendUrl(foto: String) {
        carregaFoto()
    }


}// Required empty public constructor
