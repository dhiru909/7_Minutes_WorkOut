package com.justdevelopers.a7minutesworkout


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.justdevelopers.a7minutesworkout.databinding.ActivityExerciseBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    var binding:ActivityExerciseBinding? = null
    private var tts:TextToSpeech? = null
    private var restTimer:CountDownTimer? =null
    private var restProgress:Int = 0
    private var restTime: Int = 1
    private var adapter:ExerciseStatusAdapter? = null
    private var exerciseTime:Int = 1
    private var exerciseTimer:CountDownTimer? = null
    private var exerciseProgress:Int = 0
    private var player: MediaPlayer? = null
    private var exerciseList:ArrayList<ExerciseModel>? =null
    private var currentExercisePosition :Int =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        binding?.root?.setBackgroundResource(R.drawable.exercise_activity_background)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)
        exerciseList = Constants.defaultExerciseList()
        val historyDao = (application as HistoryApp).db.historyDao()
        adapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = adapter
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
//        binding?.rvExerciseStatus?.setList = R.layout.item_exercise_status
        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {

                exitOnBackPressed()
            }
        } else {
            onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {

                        Log.i("TAG", "handleOnBackPressed: Exit")
                        exitOnBackPressed()
                    }
                })
        }
        try{
            val soundUri =
                Uri.parse("android.resource://com.justdevelopers.a7minutesworkout/" + R.raw.app_src_main_res_raw_press_start)
            player = MediaPlayer.create(applicationContext, soundUri)
            player?.isLooping = false
        }catch (e:Exception){
            e.printStackTrace()
        }

        player?.isLooping =false
        tts = TextToSpeech(this,this)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogBackConfirm()

        }
        setupRestView()

    }

    private fun exitOnBackPressed() {
        customDialogBackConfirm()
    }


    private fun customDialogBackConfirm() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_dialog_custom_back_confirmation)
        dialog.setCanceledOnTouchOutside(false)
        val btnConfirm = dialog.findViewById<Button>(R.id.tvYes)
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            if(tts != null){
                tts?.stop()
                tts?.shutdown()

            }
            if(player!=null){
                player?.stop()
                player?.release()
                player = null
            }
            binding=null
            if(restTimer!=null){
                exerciseTimer?.cancel()
                restTimer=null
                restProgress = 0
            }
            if(exerciseTimer!=null){
                exerciseTimer?.cancel()
                exerciseProgress=0
            }
            binding=null
            finishAfterTransition()
        }
        val btnNo = dialog.findViewById<Button>(R.id.tvNo)
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setupRestView(){
        binding?.flRestView?.visibility=View.VISIBLE
        binding?.tvTitle?.visibility=View.VISIBLE
        binding?.tvTitle?.text ="Get ready for '${exerciseList!![currentExercisePosition+1].getName()}'"
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE

        binding?.flExercise?.visibility = View.INVISIBLE
        if(restTimer!=null){
            restTimer!!.cancel()
            restTimer=null
            restProgress = 0
        }
        speakOut("Get ready for ${exerciseList!![currentExercisePosition+1].getName()}")
        setRestProgressBar()
    }
    private fun setupExerciseView(){
        try{player?.start()}catch(e:Exception){e.printStackTrace()}
        binding?.flRestView?.visibility=View.INVISIBLE
        binding?.tvTitle?.visibility=View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE

        binding?.flExercise?.visibility = View.VISIBLE
        if(exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseTimer=null
            exerciseProgress = 0
        }
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress =restProgress

        restTimer = object: CountDownTimer(restTime*1000.toLong(), 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = restTime-restProgress
                binding?.tvTimer?.text = (restTime-restProgress).toString()
            }

            override fun onFinish() {
//                Toast.makeText(this@ExerciseActivity,"Start the exercise",Toast.LENGTH_LONG).show()
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                adapter!!.notifyItemChanged(currentExercisePosition)
                setupExerciseView()
            }
        }.start()
    }
    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress =exerciseProgress

        restTimer = object: CountDownTimer(exerciseTime*1000.toLong(), 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = exerciseTime-exerciseProgress
                binding?.tvTimerExercise?.text = (exerciseTime-exerciseProgress).toString()
            }

            override fun onFinish() {

                exerciseProgress=0
                if(currentExercisePosition <exerciseList!!.size - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    adapter!!.notifyItemChanged(currentExercisePosition)
                    setupRestView()
                }else{
                    lifecycleScope.launch {
                        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        val current = LocalDateTime.now().format(formatter)
                        val historyDao = (application as HistoryApp).db.historyDao()
                        historyDao.insert(HistoryEntity(createdAt = current))
                    }
                    speakOut("Congratulations!")
                    finish()

                    val intent:Intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
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
    private fun speakOut(text:String){

        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    override fun onDestroy() {
        super.onDestroy()
        if(tts != null){
            tts?.stop()
            tts?.shutdown()

        }
        if(player!=null){
            player?.stop()
            player?.release()
            player = null
        }
        binding=null
        if(restTimer!=null){
            restTimer=null
            restProgress = 0
        }
        if(exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
        binding=null
    }
}