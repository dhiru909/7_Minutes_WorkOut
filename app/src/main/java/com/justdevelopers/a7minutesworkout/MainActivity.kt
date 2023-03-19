package com.justdevelopers.a7minutesworkout

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.justdevelopers.a7minutesworkout.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding:ActivityMainBinding?=null
    private var tts:TextToSpeech? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //val flStartButton: FrameLayout =findViewById(R.id.flStart)
        tts = TextToSpeech(this,this)
        binding?.flBMI?.setOnClickListener {
            startActivity(Intent(this, BMIActivity::class.java))
        }
        binding?.flHistory?.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        binding?.flStart?.setOnClickListener {
            Toast.makeText(this,"Here we will start the exercise",Toast.LENGTH_LONG).show()
            val intent = Intent(this,ExerciseActivity::class.java)

            startActivity(intent)
        }
        tts?.speak("Welcome to seven minutes workout press start to continue",TextToSpeech.QUEUE_FLUSH,null,"")
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.getDefault())
            if(result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("tts","language is not supported")
            }
        }else{
            Log.e("tts","initialization failed")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(tts != null)
            tts=null
    }
}