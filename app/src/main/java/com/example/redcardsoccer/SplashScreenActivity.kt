package com.example.redcardsoccer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashScreenActivity : AppCompatActivity() {

    //Initializing instance variables in Kotlin
    companion object {
        val SPLASH_DELAY: Long = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplashScreen()
    }//onCreate function

    //displays splash screen for SPLASH_DELAY amount of milliseconds
    private fun showSplashScreen(){
        val mThread = object: Thread() {
            override fun run() {
                try {
                    sleep(SPLASH_DELAY)
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }catch (e: Exception){
                    e.printStackTrace() //prints where the exception occurs
                }
            }
        }
        mThread.start()
    }//showSplashScreen function
}
