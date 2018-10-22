package com.a14mob.empresa.empresa.fragments

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a14mob.empresa.empresa.MainActivity
import com.a14mob.empresa.empresa.PermissionUtils

import com.a14mob.empresa.empresa.R
import com.a14mob.empresa.empresa.adapter.AvaliacaoAdapter
import com.a14mob.empresa.empresa.adapter.RespostaAdapter
import com.a14mob.empresa.empresa.adapter.ScoreAdapter
import com.a14mob.empresa.empresa.entity.Avaliacao
import com.a14mob.empresa.empresa.entity.Quiz
import com.a14mob.empresa.empresa.entity.Resposta
import com.a14mob.empresa.empresa.entity.Score
import com.google.firebase.iid.FirebaseInstanceId
import com.hendraanggrian.pikasso.picasso
import kotlinx.android.synthetic.main.fragment_avaliacoes.*
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.android.synthetic.main.fragment_score.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QuizFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class QuizFragment : Fragment() {



    var nome: String = ""
    var profissionalId: Int = 0
    var meta: Int = 0
    var foto: String = ""
    var cpf = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val prefs = inflater.context.getSharedPreferences("PROFISSIONAL", Context.MODE_PRIVATE)

        profissionalId = prefs.getString("profissionalId", null).toInt()

        meta = prefs.getString("meta", null).toInt()

        nome = prefs.getString("nome", null).toString()

        foto = prefs.getString("foto", null).toString()

        cpf = prefs.getString("cpf", null).toString()


        PermissionUtils.atualizaToken(cpf, FirebaseInstanceId.getInstance().token.toString())


        return inflater?.inflate(R.layout.fragment_quiz, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)





        quiz(profissionalId.toInt(), meta.toInt())




    }


    fun carregarInformacoes(quiz: Quiz) {

        pergunta.text = quiz.pergunta.toString()

        val recyclerView = quiz_resposta_list_recyclerview

        recyclerView.adapter = RespostaAdapter(quiz.respostas, this@QuizFragment.context!!)

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

    }




    fun quiz(profissionalId: Int, meta: Int) {

        PermissionUtils.api.listQuiz(profissionalId.toString())
                .enqueue(object : Callback<Quiz> {
                    override fun onResponse(call: Call<Quiz>?, response: Response<Quiz>?) {
                        if (this@QuizFragment.isVisible == true) {
                            this@QuizFragment.carregarInformacoes(response?.body() as Quiz)
                        }

                    }

                    override fun onFailure(call: Call<Quiz>?, t: Throwable?) {


                    }
                })

    }


}
