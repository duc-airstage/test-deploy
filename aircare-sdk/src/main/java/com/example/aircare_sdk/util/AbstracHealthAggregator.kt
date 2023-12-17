package com.example.aircare_sdk.util

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import java.time.Instant

abstract class AbstractHealthAggregator {

    abstract suspend fun aggregate(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ):Int

    companion object {
        const val TAG = "HealthAggregator"
    }

    protected fun handleStepCount(stepCount: Long) {
        Log.i(TAG, "Step count: $stepCount")
    }

    protected fun handleDistanceToTal(distanceTotal: Int) {
        Log.i(TAG, "Distance total: $distanceTotal")
    }

    protected fun handleHeartRate(avgHeartRate: Long) {
        Log.i(TAG, "Heart Rate: $avgHeartRate")
    }

    protected fun handleCaloriesTotal(caloriesTotal: Comparable<*>) {
        Log.i(TAG, "Calories Total: $caloriesTotal")
    }

    protected fun handleSleepDurationTotal(sleepDurationTotal: Comparable<*>) {
        Log.i(TAG, "Calories Total: $sleepDurationTotal")
    }

    protected fun handleException(e: Exception) {
        Log.e(TAG, "Exception in health aggregation: ${e.message}", e)
        // Run additional error handling here
    }
}
