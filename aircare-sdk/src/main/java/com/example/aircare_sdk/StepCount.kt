package com.example.aircare_sdk

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.aircare_sdk.util.AbstractHealthAggregator
import java.time.Instant


public suspend fun readStepsByTimeRange(
    healthConnectClient: HealthConnectClient,
    startTime: Instant,
    endTime: Instant
) {
    try {
        val response =
            healthConnectClient.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
        for (stepRecord in response.records) {

            // Process each step record
            Log.i("readStepsByTimeRange", stepRecord.toString())
        }
    } catch (e: Exception) {
        // Run error handling here.
        Log.e("Step Exception", e.message.toString())
    }
}

typealias AggregateStepsFunction = suspend (
    healthConnectClient: HealthConnectClient,
    startTime: Instant,
    endTime: Instant
) -> Unit

val aggregateSteps: AggregateStepsFunction = { healthConnectClient, startTime, endTime ->
    try {
        val response = healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(StepsRecord.COUNT_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        )
        // The result may be null if no data is available in the time range
        val stepCount = response[StepsRecord.COUNT_TOTAL] ?: 0
        Log.i("Step", stepCount.toString())
    } catch (e: Exception) {
        // Run error handling here
        Log.e("Step Exception", e.message.toString())
    }
}



class StepCountAggregator : AbstractHealthAggregator() {
    override suspend fun aggregate(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ):Int {
        try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val stepCount = response[StepsRecord.COUNT_TOTAL] ?: 0
            handleStepCount(stepCount)
            return stepCount.toInt()
        } catch (e: Exception) {
            handleException(e)
            return 0
        }
    }
}
