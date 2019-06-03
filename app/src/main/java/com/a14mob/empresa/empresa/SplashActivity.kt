package com.a14mob.empresa.empresa

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 1000L
    private val SPLASH_DISPLAY_QTD = 1
    lateinit var ivLogo: ImageView
    private var vezes: Int = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ivLogo = findViewById(R.id.ivLogo);
        //carregar(1);


    }

    /**
     * CARREGA O SPLASH
     */
    fun carregar(vezes:Int) {
        this.vezes = vezes + 1;

        var animacao = AnimationUtils.loadAnimation( this, R.anim.abc_popup_enter)
        animacao.reset()

        ivLogo!!.clearAnimation()
        ivLogo!!.startAnimation(animacao)



        Handler().postDelayed({

            if(vezes < SPLASH_DISPLAY_QTD){
                this.carregar(this.vezes)
            }else{

                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                this@SplashActivity.finish()

            }

        },SPLASH_DISPLAY_LENGTH)







    }

}
