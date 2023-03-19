package com.vluk4.parallaximage.sensor

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SensorDataViewModel(context: Context) : ViewModel() {
    private val sensorDataManager = SensorDataManager(context)
    private val _sensorData = MutableStateFlow<SensorData?>(null)
    val sensorData: StateFlow<SensorData?> get() = _sensorData

    fun startSensorUpdates(screenOrientation: Int) {
        sensorDataManager.init(screenOrientation)
        sensorDataManager.dataCallback = { data ->
            _sensorData.tryEmit(data)
        }
    }

    fun stopSensorUpdates() {
        sensorDataManager.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        sensorDataManager.cancel()
    }
}
