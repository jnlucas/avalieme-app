package com.a14mob.empresa.empresa

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.a14mob.empresa.empresa.entity.Profissional
import com.a14mob.empresa.empresa.fragments.AvaliacoesFragment
import com.a14mob.empresa.empresa.fragments.ScoreFragment
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.widget.Toast
import com.a14mob.empresa.empresa.controller.KeysResponseAPI
import com.a14mob.empresa.empresa.controller.RetrofitImpl
import com.a14mob.empresa.empresa.entity.Imagem
import com.a14mob.empresa.empresa.fragments.QuizFragment
import com.google.firebase.iid.FirebaseInstanceId
import com.orhanobut.hawk.Hawk
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class MainActivity() : AppCompatActivity(), RetrofitImpl.Iresponse {


    private val PICK_IMAGE = 100
    private val CAPTURE_IMAGE = 101

    lateinit var profissional: Profissional

    lateinit var avalicaoFragment: AvaliacoesFragment
    lateinit var scoreFragment: ScoreFragment
    lateinit var quizFragment: QuizFragment


    lateinit var transaction: FragmentTransaction


    var retrofitImpl = RetrofitImpl().apply {
        this.responseApi = this@MainActivity
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_score -> {

                this.changeFragment(scoreFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_atividades -> {

                this.changeFragment(avalicaoFragment)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_quiz -> {

                this.changeFragment(quizFragment);

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sair -> {

                this@MainActivity.finish();

                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        profissional = Hawk.get("profissional")


        avalicaoFragment = AvaliacoesFragment()
        scoreFragment = ScoreFragment()
        quizFragment = QuizFragment()

        setContentView(R.layout.activity_main)
        this.changeFragment(scoreFragment)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        retrofitImpl.atualizaToken(Hawk.get("CPF"), FirebaseInstanceId.getInstance().token.toString())


        val service = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enable = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        PermissionUtils.validate(this@MainActivity, 1,
                android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA
        )


    }


    fun buscaFoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Escolha a foto"), PICK_IMAGE)
    }


    fun tirarFoto() {
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        startActivityForResult(Intent.createChooser(intent, "Foto"), CAPTURE_IMAGE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            var uri = data?.data

            try {
                var bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)
                // Log.d(TAG, String.valueOf(bitmap));


                //avalicaoFragment?.setFoto(bitmap)

                preparaEEnviaFoto(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        if (requestCode == CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            preparaEEnviaFoto(bitmap)

        }
    }

    private fun preparaEEnviaFoto(bitmap: Bitmap?) {
        val f = File(getCacheDir(), "upload")
        f.createNewFile()

        var bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        var bitmapdata = bos.toByteArray()
        var fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        val reqFile = RequestBody.create(MediaType.parse("image/*"), f)
        val body = MultipartBody.Part.createFormData("upload", f.getName(), reqFile)

        retrofitImpl.enviarFoto(body)
    }

    override fun getResponse(boolean: Boolean, any: Any?, key: KeysResponseAPI) {
        when (key) {
            KeysResponseAPI.Token -> {
                Log.i("Token", boolean.toString())
            }

            KeysResponseAPI.envioFotoPerfil -> {
                if (boolean) {
                    any?.let {
                        atualizaUrlProfissional((any as Imagem).foto)
                    }
                } else {
                    mandaToast("Erro ao Atualizar a foto do perfil!")
                    Log.i(KeysResponseAPI.envioFotoPerfil.name, any.toString())
                }
            }
            KeysResponseAPI.envioUrlFoto -> {
                if (boolean) {
                    any?.let {
                        //var profissional: Profissional = Hawk.get("profissional")
                        profissional = it as Profissional
                        Hawk.put("profissional", profissional)
                        if (isShowingFragment(avalicaoFragment)) {
                            avalicaoFragment?.atualizarFoto()
                        }

                    }
                }
            }
        }

    }


    fun isShowingFragment(fragment: Fragment): Boolean {
        return fragment.isAdded() && fragment.isVisible() && fragment.getUserVisibleHint()

    }


    private fun mandaToast(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }


    private fun atualizaUrlProfissional(url: String) {
        retrofitImpl.enviarUrlFoto(profissional.id, url)
        retrofitImpl.responseApi = this@MainActivity

    }


    /**
     * altera o fragment
     */
    fun changeFragment(fragment: Fragment) {
        transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.animacao_fragment_out, R.anim.animacao_fragment_in)
                .replace(R.id.frame, fragment)
                .commit()


    }


}
