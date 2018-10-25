package com.a14mob.empresa.empresa

import android.content.Context
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.a14mob.empresa.empresa.entity.Profissional
import com.a14mob.empresa.empresa.fragments.AvaliacoesFragment
import com.a14mob.empresa.empresa.fragments.MapaFragment
import com.a14mob.empresa.empresa.fragments.ScoreFragment
import com.a14mob.empresa.empresa.retrofit.RetroFitRestAPI
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import com.a14mob.empresa.empresa.entity.Imagem
import com.a14mob.empresa.empresa.fragments.QuizFragment
import kotlinx.android.synthetic.main.fragment_avaliacoes.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {


    private val PICK_IMAGE = 100

    private var avalicaoFragment: AvaliacoesFragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_score -> {

                this.changeFragment(ScoreFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_atividades -> {

                this.changeFragment(avalicaoFragment!!)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_quiz -> {

                this.changeFragment(QuizFragment());

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


        avalicaoFragment = AvaliacoesFragment()

        setContentView(R.layout.activity_main)
        this.changeFragment(ScoreFragment());
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        val service = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enable = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        PermissionUtils.validate(this@MainActivity, 1,
                android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)


    }


    fun buscaFoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            var uri = data?.data

            try {
                var bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)
                // Log.d(TAG, String.valueOf(bitmap));


                //avalicaoFragment?.setFoto(bitmap)

                val f = File(getCacheDir(), "upload")
                f.createNewFile()

                var bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
                var bitmapdata = bos.toByteArray()
                var fos = FileOutputStream(f)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()

                val reqFile = RequestBody.create(MediaType.parse("image/*"), f)
                val body = MultipartBody.Part.createFormData("upload", f.getName(), reqFile)

                PermissionUtils.api.postImage(body)
                        .enqueue(object : Callback<Imagem> {
                            override fun onResponse(call: Call<Imagem>?, response: Response<Imagem>?) {
                                if (response?.body()?.toString() != null) {
                                    atualizaUrlProfissional(response.body()?.foto!!)
                                }
                            }

                            override fun onFailure(call: Call<Imagem>?, t: Throwable?) {
                                Log.i("deuMerda", "")
                            }
                        })

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun atualizaUrlProfissional(url: String) {
        var edit = getSharedPreferences("PROFISSIONAL", MODE_PRIVATE)
        var id = edit.getString("profissionalId", "")

        PermissionUtils.api.sendUrlFoto(id, url).enqueue(object : Callback<Profissional> {
            override fun onResponse(call: Call<Profissional>?, response: Response<Profissional>?) {
                if (response?.body()?.foto != null) {
                    avalicaoFragment?.setFotoShared(response?.body()?.foto!!)
                    avalicaoFragment?.carregaFoto(response?.body()?.foto!!)
                }
            }

            override fun onFailure(call: Call<Profissional>?, t: Throwable?) {

            }
        })
    }


    /**
     * altera o fragment
     */
    fun changeFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .commit()
    }


}
