package com.example.aircare_sdk

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.aircare_sdk.util.AbstractHealthAggregator
import java.time.Instant

//suspend fun aggregateHeartRate(
//    healthConnectClient: HealthConnectClient,
//    startTime: Instant,
//    endTime: Instant
//) {
//    try {
//        val response =
//            healthConnectClient.aggregate(
//                AggregateRequest(
////                    setOf(HeartRateRecord.BPM_MAX, HeartRateRecord.BPM_MIN)
//                    setOf(HeartRateRecord.BPM_AVG),
//                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
//                )
//            )
//        // The result may be null if no data is available in the time range
////        val minimumHeartRate = response[HeartRateRecord.BPM_MIN]
////        val maximumHeartRate = response[HeartRateRecord.BPM_MAX]
////        Log.i("minimumHeartRate", minimumHeartRate.toString())
////        Log.i("maximumHeartRate", maximumHeartRate.toString())
//
//        val avgHeartRate = response[HeartRateRecord.BPM_AVG]
//        Log.i("avgHeartRate", avgHeartRate.toString())
//
//
//
//    } catch (e: Exception) {
//        // Run error handling here
//    }
//}

class HeartRateAggregator : AbstractHealthAggregator() {
    override suspend fun aggregate(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ):Int {
        try {
            val response =
                healthConnectClient.aggregate(
                    AggregateRequest(
//                    setOf(HeartRateRecord.BPM_MAX, HeartRateRecord.BPM_MIN)
                        setOf(HeartRateRecord.BPM_AVG),
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
            // The result may be null if no data is available in the time range
//        val minimumHeartRate = response[HeartRateRecord.BPM_MIN]
//        val maximumHeartRate = response[HeartRateRecord.BPM_MAX]
//        Log.i("minimumHeartRate", minimumHeartRate.toString())
//        Log.i("maximumHeartRate", maximumHeartRate.toString())

            val avgHeartRate = response[HeartRateRecord.BPM_AVG]?: 0
            handleHeartRate(avgHeartRate)
            return avgHeartRate.toInt()
        } catch (e: Exception) {
            handleException(e)
            return 0
        }
    }
}
