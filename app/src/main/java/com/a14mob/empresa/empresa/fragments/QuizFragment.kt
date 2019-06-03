package com.a14mob.empresa.empresa.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.a14mob.empresa.empresa.MainActivity

import com.a14mob.empresa.empresa.R
import com.a14mob.empresa.empresa.adapter.RespostaAdapter
import com.a14mob.empresa.empresa.controller.KeysResponseAPI
import com.a14mob.empresa.empresa.controller.RetrofitImpl
import com.a14mob.empresa.empresa.entity.*
import kotlinx.android.synthetic.main.fragment_quiz.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QuizFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class QuizFragment : Fragment(), RetrofitImpl.Iresponse, RespostaAdapter.OnItemClickListener {

    var retrofitImpl = RetrofitImpl().apply { this.responseApi = this@QuizFragment }
    private lateinit var profissional: Profissional

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        profissional = (activity as MainActivity).profissional


        quiz()

        return inflater?.inflate(R.layout.fragment_quiz, container, false)
    }


    fun carregarInformacoes(quiz: Quiz) {

        if (quiz.id == 1) {
            pergunta.text = "Sem Quizz..."
            quiz_resposta_list_recyclerview.visibility = View.GONE
            return
        }


        if ((activity as MainActivity).isShowingFragment(this@QuizFragment)) {
            tvDefaultQuizz.visibility = View.GONE
            pergunta.text = quiz.pergunta
            quiz_resposta_list_recyclerview.layoutManager = LinearLayoutManager(activity?.applicationContext)
            var adapter = RespostaAdapter(quiz.respostas, this@QuizFragment.context!!)
            adapter.mClick = this@QuizFragment
            quiz_resposta_list_recyclerview.adapter = adapter

        }
    }

    override fun getResponse(boolean: Boolean, any: Any?, key: KeysResponseAPI) {
        when (key) {
            KeysResponseAPI.buscaQuizz -> {
                if (boolean) {
                    any?.let {
                        carregarInformacoes(it as Quiz)
                    }
                }
            }
            KeysResponseAPI.enviarRespostaQuizz -> {
                if (boolean) {
                    defautFragment()
                    Toast.makeText(activity, "Obrigado, sua contribuição é muito importante!", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun defautFragment() {
        pergunta.text = "Sem Quizz..."
        quiz_resposta_list_recyclerview.visibility = View.GONE
        tvDefaultQuizz.visibility = View.VISIBLE
    }

    fun quiz() {
        retrofitImpl.buscaQuizz(profissional.id.toString())
    }

    override fun OnClick(resposta: Resposta) {
        enviarResposta(resposta)
    }

    fun enviarResposta(resposta: Resposta) {
        retrofitImpl.enviarRespostaQuizz(profissional.id.toString(), resposta.perguntaId.toString())
    }

}
