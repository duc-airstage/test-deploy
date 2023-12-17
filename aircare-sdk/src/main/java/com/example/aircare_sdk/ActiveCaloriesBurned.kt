package com.example.aircare_sdk

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.aircare_sdk.util.AbstractHealthAggregator
import java.time.Instant

//suspend fun aggregateActiveCaloriesBurned(
//    healthConnectClient: HealthConnectClient,
//    startTime: Instant,
//    endTime: Instant
//) {
//    try {
//        val response = healthConnectClient.aggregate(
//            AggregateRequest(
//                metrics = setOf(ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL),
//                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
//            )
//        )
//        // The result may be null if no data is available in the time range
//        val caloriesTotal = response[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?:0
//        Log.i("caloriesTotal", caloriesTotal.toString())
//    } catch (e: Exception) {
//        // Run error handling here
//        Log.e("Calories Total Exception", e.message.toString())
//    }
//}


class ActiveCaloriesBurnedAggregator : AbstractHealthAggregator() {
    override suspend fun aggregate(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ):Int {
        try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            val caloriesTotal = response[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?:0
            handleCaloriesTotal(caloriesTotal)
            return (caloriesTotal as Int)
        } catch (e: Exception) {
            handleException(e)
            return 0
        }
    }
}
