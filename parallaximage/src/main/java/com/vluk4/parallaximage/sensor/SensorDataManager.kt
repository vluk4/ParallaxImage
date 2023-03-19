package com.vluk4.parallaximage.sensor

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorDataManager(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    var dataCallback: ((SensorData) -> Unit)? = null
    private var screenOrientation: Int = Configuration.ORIENTATION_PORTRAIT

    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    fun init(newScreenOrientation: Int) {
        screenOrientation = newScreenOrientation
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun cancel() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_GRAVITY -> gravity = it.values
                Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = it.values
            }

            if (gravity != null && geomagnetic != null) {
                processSensorData()
            }
        }
    }

    private fun processSensorData() {
        val r = FloatArray(9)
        val i = FloatArray(9)

        if (SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(r, orientation)

            val (pitch, roll) = when (screenOrientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    Pair(orientation[1] + INITIAL_PORTRAIT_OFFSET, orientation[2])
                }
                else -> {
                    Pair(orientation[2] + INITIAL_LANDSCAPE_OFFSET, -orientation[1])
                }
            }

            if (pitch in -0.9f..0.9f && roll in -2.5f..2.5f) {
                dataCallback?.invoke(SensorData(roll = roll, pitch = pitch))
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    companion object {
        private const val INITIAL_PORTRAIT_OFFSET = 0.5f
        private const val INITIAL_LANDSCAPE_OFFSET = 1f
    }
}

