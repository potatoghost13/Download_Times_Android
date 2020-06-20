package com.example.download_times

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //SetupVariables
        val defaultValue = 1

        //Setup UI with Ids.
        val outputHoursText: TextView = findViewById(R.id.outputHours)
        val outputMinutesText: TextView = findViewById(R.id.outputMinutes)
        val outputSecondsText: TextView = findViewById(R.id.outputSeconds)
        val userSizeText: TextView = findViewById(R.id.userSize)
        userSizeText.text = defaultValue.toString()
        val userSpeedText: TextView = findViewById(R.id.userSpeed)
        userSpeedText.text = defaultValue.toString()
        val userCalcBtn: Button = findViewById(R.id.userCalculate)

        //Create the Speed Spinner
        val speedSpinnerTemp: Spinner = findViewById(R.id.userSpeedUnits)
        val speedSpinner = createSpinner(this, R.array.speedUnits, speedSpinnerTemp)

        //Create the Size Spinner
        val sizeSpinnerTemp: Spinner = findViewById(R.id.userSizeUnits)
        val sizeSpinner = createSpinner(this, R.array.sizeUnits, sizeSpinnerTemp)

        //Calculate Button.
        userCalcBtn.setOnClickListener {
            //Pull the User Selected Units to be used within the Code.
            val speedUnitSelected = speedSpinner.selectedItem.toString()
            val sizeUnitSelected = sizeSpinner.selectedItem.toString()

            //Check the TextViews are Populated
            val userSize = checkUserInput(this, userSizeText, defaultValue)
            val userSpeed = checkUserInput(this, userSpeedText, defaultValue)

            Log.d("Btn", "The Calculate Button was Pressed.")
            Log.d("Btn", "Speed: $userSpeed $speedUnitSelected, Size: $userSize $sizeUnitSelected")

            val timeInSeconds = calc(userSize, sizeUnitSelected, userSpeed, speedUnitSelected)
            Log.d("Btn", "Time in Seconds: $timeInSeconds")

            //Output
            //Years
            if (timeInSeconds >= 3.15E7) {
                outputHoursText.text = getString(R.string.duration_over_a_year_L1)
                outputMinutesText.text = getString(R.string.duration_over_a_year_L2)
                outputSecondsText.text = getString(R.string.duration_over_a_year_L3)
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
                outputHoursText.text = "$hours ${getString(R.string.str_Hours)}, \n $minutes ${getString(R.string.str_Minutes)}"
                outputMinutesText.text = "$minutes ${getString(R.string.str_Minutes)},"
                outputSecondsText.text = "$seconds ${getString(R.string.str_Seconds)}."
            }
        }
    }
}

//Todo: Fix Layout but as a last Priority
//Todo: Improve Layout
//Todo: Correct Output Text function. Remove the three separate fields

// Function To Convert User Input to Int.
fun checkUserInput(activity: Activity, textView: TextView, defaultValue: Int): Int {
    var tempValue: Int
    try {
        tempValue = (textView.text.toString()).toInt()

    } catch (e: NumberFormatException) {
        Toast.makeText(activity, "Please Enter a Number", Toast.LENGTH_LONG).show()
        textView.text = defaultValue.toString()
        tempValue = defaultValue
    }

    if (tempValue <= 0) {
        Toast.makeText(activity, "Value Must be Greater than Zero", Toast.LENGTH_SHORT).show()
        textView.text = defaultValue.toString()
        tempValue = defaultValue
    }

    return tempValue
}

//Function Used to Create the Spinners
fun createSpinner(activity: Activity, arrayId: Int, passingSpinner: Spinner): Spinner {
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
fun calc(size: Int, sizeUnit: String, speed: Int, speedUnit: String): Long {

    val speedCalc: Long = speed * unitConversion(speedUnit)
    val sizeCalc: Long = size * unitConversion(sizeUnit)
    val timeInSeconds = sizeCalc / speedCalc

    Log.d("Btn", "Speed is $speed $speedUnit and the Size is $size $sizeUnit")
    return timeInSeconds
}

