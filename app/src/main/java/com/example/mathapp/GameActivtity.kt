package com.example.mathapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.random.Random

class GameActivtity : AppCompatActivity() {

    lateinit var textScore :TextView
    lateinit var textLife : TextView
    lateinit var textTime : TextView

    lateinit var questionView: TextView
    lateinit var answerView: EditText

    lateinit var buttonOK:Button
    lateinit var buttonNEXT: Button

    var correctAnswer = 0
    var userScore =0
    var userLife=3

    //for time changing
    lateinit var timer:CountDownTimer
    private val startTimerInMillis : Long=30000  //time should in milli
    var timeLeftInMillis : Long=startTimerInMillis
    lateinit var actionTitle:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_activtity)

        actionTitle=intent.getStringExtra("actionTitle").toString()
        when(actionTitle){
            "Addition"->supportActionBar!!.title="Addition"
            "Subtraction"->supportActionBar!!.title="Subtraction"
            "Multiplication"->supportActionBar!!.title="Multiplication"
        }

//        supportActionBar!!.title="Addition"

        textScore=findViewById(R.id.textViewScore)
        textLife=findViewById(R.id.textViewLife)
        textTime=findViewById(R.id.textViewTime)

        questionView=findViewById(R.id.questionView)
        answerView=findViewById(R.id.answerView)

        buttonOK=findViewById(R.id.buttonOK)
        buttonNEXT=findViewById(R.id.buttonNEXT)

        //calling the function that is gameContinue
        gameContinue()

        buttonOK.setOnClickListener {
            val input=answerView.text.toString()
            if(input==""){
                Toast.makeText(applicationContext,"Write your answer or Click on the NEXT button",
                Toast.LENGTH_LONG).show()
            }
            else{
                pauseTimer()
                val userAnswer=input.toInt()
                if(userAnswer==correctAnswer){
                    userScore=userScore + 10
                    questionView.text= "congo!! your ans is correct"
                    textScore.text=userScore.toString()
                }
                else{
                    println(userLife)
                    if (userLife>0) {
                        userLife--
                    }
                    questionView.text=" oops :( your ans is wrong TRY AGAIN"
                    println(userLife)
                    textLife.text=userLife.toString()
                }
            }

        }
        buttonNEXT.setOnClickListener{
            pauseTimer()
            resetTimer()
            gameContinue()
            answerView.setText("")

            if(userLife==0){
                Toast.makeText(applicationContext,"GameOver",Toast.LENGTH_LONG).show()
                val intent=Intent(this@GameActivtity,ResultActivity::class.java)
                intent.putExtra("score",userScore)
                startActivity(intent)
                finish()
            }
            else{
                gameContinue()

            }

        }
    }
    fun gameContinue(){
        val number1=Random.nextInt(0,200)
        val number2=Random.nextInt(0,200)
        if(actionTitle=="addition"){
        questionView.text="$number1 + $number2"
        correctAnswer = number1 + number2
        }
        else if(actionTitle=="subtraction"){
            questionView.text="$number1 - $number2"
            correctAnswer = number1 - number2
        }
        if(actionTitle=="multiplication"){
            questionView.text="$number1 *$number2"
            correctAnswer = number1 * number2
        }

        startTimer()
    }
    fun startTimer(){
        timer=object : CountDownTimer(timeLeftInMillis,1000){
            override fun onTick(millisUntilFinished: Long) {

                timeLeftInMillis=millisUntilFinished
                updateText()
            }

            override fun onFinish() {

                pauseTimer()
                updateText()
                resetTimer()
                if(userLife>0) {
                    userLife--
                }
                textLife.text=userLife.toString()
                questionView.text="Sorry,your Time's up!!"

            }

        }.start()
    }

    fun updateText(){
        val remainingTime : Int=(timeLeftInMillis/1000).toInt()
        textTime.text= String.format(Locale.getDefault(),"%02d",remainingTime)
    }

    fun pauseTimer(){
        timer.cancel()
    }

    fun resetTimer(){
        timeLeftInMillis=startTimerInMillis
        updateText()
    }
}