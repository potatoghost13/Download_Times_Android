package com.example.download_times

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

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

        //Variables Required
        var speedUnitSelected: String
        var sizeUnitSelected: String

        //Create the Speed Spinner
        val speedSpinner: Spinner = findViewById(R.id.userSpeedUnits)
        if (speedSpinner != null) {
            ArrayAdapter.createFromResource(
                this,
                R.array.speedUnits,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                speedSpinner.adapter = adapter
            }
        }
        speedUnitSelected = speedSpinner.selectedItem.toString()
        Log.d("Check_1", "Speed Unit Selected is:  $speedUnitSelected")

        //Create the Size Spinner
        val sizeSpinner: Spinner = findViewById(R.id.userSizeUnits)
        if (sizeSpinner != null) {
            ArrayAdapter.createFromResource(
                this,
                R.array.sizeUnits,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                sizeSpinner.adapter = adapter
            }
        }
        sizeUnitSelected = sizeSpinner.selectedItem.toString()
        Log.d("Check_2", "Speed Unit Selected is:  $sizeUnitSelected")
    }
}

fun main(userSpeed: Int, userSpeedUnit: String, userSize: Int, userSizeUnit: String) {
    //User Inputs
    var userSpeed = userSpeed           //7
    var userSpeedUnit = userSpeedUnit   //"MB/s"
    var userSize = userSize             //45
    var userSizeUnit = userSizeUnit     //"GB"

    //Call Calc Function
    val tempTimeInSeconds: Long = calc(userSize, userSpeed, userSpeedUnit, userSizeUnit)

    //Print OutPut
    timeOutput(tempTimeInSeconds)
    println("NOTE: Times shown are Approximate")

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

