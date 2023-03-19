package com.justdevelopers.a7minutesworkout

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.justdevelopers.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.roundToInt

class BMIActivity : AppCompatActivity() {
    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }
private var currentVisibleView: String=METRIC_UNITS_VIEW //A variable To hold a value to make a view visible.
    private var binding: ActivityBmiBinding? =null
    var hyperspaceJump: Animation?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)
        supportActionBar?.title = "BMI CALCULATOR"
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.root?.setBackgroundResource(R.drawable.exercise_activity_background)
        binding?.toolbarExercise?.setNavigationOnClickListener{
            finishAfterTransition()

        }
        hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump)
        makeVisibleMetricUnitsView()
        binding?.rgGroup?.setOnCheckedChangeListener { _, checkedId:Int ->
            if(checkedId == R.id.rbMetric){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUsUnitsView()
            }
        }
        binding?.btnCalculateUnits?.setOnClickListener{
            if(isValidateUnits()){
                if(currentVisibleView==METRIC_UNITS_VIEW) {
                    val heightValue: Float =
                        (binding?.etMetricHeight?.text.toString().toFloat()) / 100
                    val weightValue: Float = binding?.etMetricWeight?.text.toString().toFloat()
                    val bmi =(weightValue / (heightValue.toDouble().pow(2.0)) * 10).roundToInt() / 10.0
                    displayResult(bmi.toFloat())
                }else{
                    val weightValue: Float = (binding?.etUsWeight?.text.toString().toFloat())*0.453592.toFloat()
                    val feet: Float =
                        (binding?.etFeetUsHeight?.text.toString().toFloat())
                    val inch:Float = (binding?.etInchUsHeight?.text.toString().toFloat())
                    val heightValue: Float = (((feet*12)+inch)* 0.0254).toFloat()
                    val bmi =(weightValue / (heightValue.toDouble().pow(2.0)) * 10).roundToInt() / 10.0
                    displayResult(bmi.toFloat())
                }
            }else{
                Toast.makeText(this,"Please enter valid data!",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.llMetric?.visibility = View.VISIBLE
        binding?.llUs?.visibility = View.GONE
        binding?.etMetricHeight?.text?.clear()
        binding?.etMetricWeight?.text?.clear()
        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE
        binding?.rbMetric?.setBackgroundResource(R.drawable.item_corner_color_accent_background)
        binding?.rbMetric?.startAnimation(hyperspaceJump)
        binding?.rbUs?.setBackgroundResource(R.drawable.item_gray_background)


    }
    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.llMetric?.visibility = View.GONE
        binding?.llUs?.visibility = View.VISIBLE
        binding?.etUsWeight?.text?.clear()
        binding?.etFeetUsHeight?.text?.clear()
        binding?.etInchUsHeight?.text?.clear()
        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE
        binding?.rbUs?.setBackgroundResource(R.drawable.item_corner_color_accent_background)
        binding?.rbUs?.startAnimation(hyperspaceJump)
        binding?.rbMetric?.setBackgroundResource(R.drawable.item_gray_background)

    }
    private fun displayResult(bmi:Float){
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {

            binding?.tvBMIType?.setTextColor(Color.parseColor("#FFFF0000"))
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            binding?.tvBMIType?.setTextColor(Color.parseColor("#FFFF0000"))
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            binding?.tvBMIType?.setTextColor(Color.parseColor("#FFFF5050"))
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            binding?.tvBMIType?.setTextColor(Color.parseColor("#FF00FF00"))
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            binding?.tvBMIType?.setTextColor(Color.parseColor("#FFFFFF00"))
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            binding?.tvBMIType?.setTextColor(Color.parseColor("#FFFF6060"))
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            binding?.tvBMIType?.setTextColor(Color.parseColor("#FFFF0000"))
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            binding?.tvBMIType?.setTextColor(Color.parseColor("#FFFF0000"))
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        //Use to set the result layout visible
        binding?.llDiplayBMIResult?.visibility = View.VISIBLE
        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding?.tvYourBMI?.text = "YOUR BMI"
        binding?.tvBMIValue?.text = bmiValue // Value is set to TextView
        binding?.tvBMIType?.text = bmiLabel // Label is set to TextView

        binding?.tvBMIDescription?.text = bmiDescription // Description is set to TextView

    }
    private fun isValidateUnits(): Boolean {
        var isValid = true
        if(currentVisibleView==METRIC_UNITS_VIEW) {
            if (binding?.etMetricHeight?.text.toString().isEmpty()) isValid = false
            if (binding?.etMetricWeight?.text.toString().isEmpty()) isValid = false
        }else{
            if (binding?.etInchUsHeight?.text.toString().isEmpty()) isValid = false
            if (binding?.etFeetUsHeight?.text.toString().isEmpty()) isValid = false
            if (binding?.etUsWeight?.text.toString().isEmpty()) isValid = false
        }
        return isValid

    }

    override fun onBackPressed() {
            finishAfterTransition()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}