package com.example.aircare_sdk.data

import java.time.Instant
import java.time.ZoneOffset
import kotlin.Metadata

data class StepsRecord(
    val startTime: Instant,
    val startZoneOffset: ZoneOffset?,
    val endTime: Instant,
    val endZoneOffset: ZoneOffset?,
    val count: Long
)


