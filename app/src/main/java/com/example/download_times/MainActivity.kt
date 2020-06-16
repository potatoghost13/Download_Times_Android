package com.example.download_times

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Setup UI with Ids.
        val outputHoursText: TextView = findViewById(R.id.outputHours)
        val outputMinutesText: TextView = findViewById(R.id.outputMinutes)
        val outputSecondsText: TextView = findViewById(R.id.outputSeconds)
        val userSizeText: TextView = findViewById(R.id.userSize)
        val userSpeedText: TextView = findViewById(R.id.userSpeed)
        val userCalcBtn: Button = findViewById(R.id.userCalculate)

        //Create the Speed Spinner
        val speedSpinnerTemp: Spinner = findViewById(R.id.userSpeedUnits)
        val speedSpinner = createSpinner(this, R.array.speedUnits, speedSpinnerTemp)

        //Create the Size Spinner
        val sizeSpinnerTemp: Spinner = findViewById(R.id.userSizeUnits)
        val sizeSpinner = createSpinner(this, R.array.sizeUnits, sizeSpinnerTemp)

        //Calculate Button.
        userCalcBtn.setOnClickListener {
            val speedUnitSelected = speedSpinner.selectedItem.toString()
            val sizeUnitSelected = sizeSpinner.selectedItem.toString()
            //Check the TextViews are Populated
            val userSize: Int = if (userSizeText.text.toString() == "") {
                10
            }else {
                (userSizeText.text.toString()).toInt()
            }
            val userSpeed: Int = if (userSpeedText.text.toString() == "") {
                10
            }else {
                (userSpeedText.text.toString()).toInt()
            }
            Log.d("Btn", "The Calculate Button was Pressed.")
            Log.d("Btn", "Speed: $userSpeed $speedUnitSelected, Size: $userSize $sizeUnitSelected")

            val timeInSeconds = calc(speed = userSpeed,speedUnit = speedUnitSelected,size = userSize,sizeUnit = sizeUnitSelected)

            //Output
            //Years
            if (timeInSeconds >= 3.15E7) {
                outputHoursText.text = R.string.duration_over_a_year_L1.toString()
                outputMinutesText.text = R.string.duration_over_a_year_L2.toString()
                outputSecondsText.text = R.string.duration_over_a_year_L3.toString()
            } else {

                //Hours
                val doubleHours: Double = (timeInSeconds.toDouble() / 3600)
                val hours = doubleHours.toInt()
                //Minutes
                val doubleMinutes: Double = ((timeInSeconds.toDouble() / 3600) - hours) * 60
                val minutes = doubleMinutes.toInt()
                //Seconds
                val doubleSeconds: Double = (doubleMinutes - minutes) * 60
                val seconds = doubleSeconds.toInt()

                //OutPut
                outputHoursText.text = "$hours ${R.string.str_Hours},"
                outputMinutesText.text = "$minutes ${R.string.str_Minutes},"
                outputSecondsText.text = "$seconds ${R.string.str_Seconds}"
            }
        }
    }
}

//Todo: Move TextView Checking to a function
//Todo: Fix Layout but as a last Priority

//Function Used to Create the Spinners
fun createSpinner(activity:Activity, arrayId:Int, passingSpinner:Spinner ):Spinner{
    //Create the Size Spinner
    val tempSpinner: Spinner = passingSpinner
    ArrayAdapter.createFromResource(
        activity,
        arrayId,
        android.R.layout.simple_spinner_item
    ).also { adapter ->
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        tempSpinner.adapter = adapter
    }
    return tempSpinner
}

//Function Used to Convert the Size or Speed into Bytes
fun unitConversion(unit: String): Long {
    return when (unit) {
        //Bytes
        "TB", "TB/s" -> 1000000000000L
        "GB", "GB/s" -> 1000000000L
        "MB", "MB/s" -> 1000000L
        "KB", "KB/s" -> 1000L
        "B", "B/s" -> 1
        //Bits to Bytes
        "Tbps" -> (1000000000000L / 8)
        "Gbps" -> (1000000000L / 8)
        "Mbps" -> (1000000L / 8)
        "Kbps" -> (1000L / 8)
        "bps" -> (1 / 8)
        else -> {
            1
        }
    }
}

//Function to actually Calculate the Time in Seconds.
fun calc(size: Int, speed: Int, speedUnit: String, sizeUnit: String): Long {

    val speedCalc: Long = speed * unitConversion(speedUnit)
    val sizeCalc: Long = size * unitConversion(sizeUnit)
    val timeInSeconds = sizeCalc / speedCalc

    println("Speed is $speed $speedUnit and the Size is $size $sizeUnit")
    return timeInSeconds
}

//Function to Output the Time
fun timeOutput(timeInSeconds: Long): Boolean {

    //Years
    if (timeInSeconds >= 3.15E7) {
        println("The Duration is over a year long")
        return true
    }

    //Hours
    val doubleHours: Double = (timeInSeconds.toDouble() / 3600)
    val hours = doubleHours.toInt()
    //Minutes
    val doubleMinutes: Double = ((timeInSeconds.toDouble() / 3600) - hours) * 60
    val minutes = doubleMinutes.toInt()
    //Seconds
    val doubleSeconds: Double = (doubleMinutes - minutes) * 60
    val seconds = doubleSeconds.toInt()

    //OutPut
    println("The Duration is $hours Hours, $minutes Minutes, $seconds Seconds")
    return true
}

