package com.example.aircare_sdk

import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.aircare_sdk.util.AbstractHealthAggregator
import java.time.Instant

class DistanceTotalAggregator : AbstractHealthAggregator() {
    override suspend fun aggregate(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ):Int {
        try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(DistanceRecord.DISTANCE_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            val distanceTotal = response[DistanceRecord.DISTANCE_TOTAL]?.inMeters ?: 0L
            handleDistanceToTal(distanceTotal.toInt())
            return distanceTotal.toInt()
        } catch (e: Exception) {
            handleException(e)
            return 0
        }
    }
}
