package com.example.aircare_sdk

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.aircare_sdk.util.AbstractHealthAggregator
import java.time.Instant

//suspend fun aggregateSleepSession(
//    healthConnectClient: HealthConnectClient,
//    startTime: Instant,
//    endTime: Instant
//) {
//    try {
//        val response = healthConnectClient.aggregate(
//            AggregateRequest(
//                metrics = setOf(SleepSessionRecord.SLEEP_DURATION_TOTAL),
//                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
//            )
//        )
//        // The result may be null if no data is available in the time range
//        val sleepDurationTotal = response[SleepSessionRecord.SLEEP_DURATION_TOTAL]
//        Log.i("sleepDurationTotal", sleepDurationTotal.toString())
//
//    } catch (e: Exception) {
//        // Run error handling here
//    }
//}

class SleepSessionAggregator : AbstractHealthAggregator() {
    override suspend fun aggregate(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ):Int {
        try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(SleepSessionRecord.SLEEP_DURATION_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            val sleepDurationTotal = response[SleepSessionRecord.SLEEP_DURATION_TOTAL]?: 0
            Log.i("sleepDurationTotal", sleepDurationTotal.toString())

            handleSleepDurationTotal(sleepDurationTotal)
            return sleepDurationTotal as Int
        } catch (e: Exception) {
            handleException(e)
            return 0
        }
    }
}