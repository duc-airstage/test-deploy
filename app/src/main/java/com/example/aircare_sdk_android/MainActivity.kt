package com.example.aircare_sdk_android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import com.example.aircare_sdk.data.AircareManager
import com.example.aircare_sdk.readStepsByTimeRange
import com.example.aircare_sdk_android.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.time.Instant
import java.time.ZonedDateTime
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.SleepSessionRecord
import com.example.aircare_sdk.ActiveCaloriesBurnedAggregator
import com.example.aircare_sdk.DistanceTotalAggregator
import com.example.aircare_sdk.HeartRateAggregator
import com.example.aircare_sdk.SleepSessionAggregator
import com.example.aircare_sdk.StepCountAggregator
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {
    private val scope = CoroutineScope(newSingleThreadContext("health"))
    // Create a set of permissions for required data types
    val PERMISSIONS =
        setOf(
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getReadPermission(DistanceRecord::class),
            HealthPermission.getReadPermission(SleepSessionRecord::class),
            HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
        )

    // Create the permissions launcher
    val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

    val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
        if (granted.containsAll(PERMISSIONS)) {
            // Permissions successfully granted
        } else {
            // Lack of required permissions
        }
    }

    suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        if (granted.containsAll(PERMISSIONS)) {
            // Permissions already granted; proceed with inserting or reading data
        } else {
            requestPermissions.launch(PERMISSIONS)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val aircareManager = AircareManager(this)
        val stepAggregator = StepCountAggregator()


        scope.launch {
            checkPermissionsAndRun(aircareManager.healthConnectClient)
//            stepAggregator.aggregate(aircareManager.healthConnectClient, ZonedDateTime.now().minusMonths(1).toInstant(), Instant.now())
//            aggregateSteps(aircareManager.healthConnectClient, ZonedDateTime.now().minusMonths(1).toInstant(), Instant.now())
//            aggregateHeartRate(aircareManager.healthConnectClient, ZonedDateTime.now().minusMonths(1).toInstant(), Instant.now())
//            aggregateSleepSession(aircareManager.healthConnectClient, ZonedDateTime.now().minusMonths(1).toInstant(), Instant.now())
//            aggregateActiveCaloriesBurned(aircareManager.healthConnectClient, ZonedDateTime.now().minusMonths(1).toInstant(), Instant.now())
        }


        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    AggregateStepsCard(aircareManager)
                    AggregateStepsCardList(aircareManager)

                }
            }
        }
    }
}

@Composable
fun AggregateStepsCardList(aircareManager: AircareManager) {
    LazyColumn {
        items(1) { index ->
            // You can replace '5' with the number of cards you want in the list
            AggregateStepsCard(aircareManager = aircareManager)
            AggregateHeartRateCard(aircareManager = aircareManager, cardIndex = index)
            AggregateDistanceCard(aircareManager = aircareManager, cardIndex = index)
            AggregateSleepCard(aircareManager = aircareManager, cardIndex = index)
            AggregateCaloriesCard(aircareManager = aircareManager, cardIndex = index)
        }
    }
}

@Composable
fun AggregateStepsCard(aircareManager: AircareManager) {
    var stepCount by remember { mutableStateOf<String?>("Loading...") }

    val startTime = ZonedDateTime.now().minusMonths(1).toInstant()
    val endTime = Instant.now()


    val formattedStartTime = formatInstantToDate(startTime, "yyyy-MM-dd")
    val formattedEndTime = formatInstantToDate(endTime, "yyyy-MM-dd")

    val stepAggregator = StepCountAggregator()

    LaunchedEffect(key1 = aircareManager) {
        try {
            val reponse = stepAggregator.aggregate(aircareManager.healthConnectClient, startTime, endTime)
            stepCount = reponse.toString()

        } catch (e: Exception) {
            // Handle exceptions
            stepCount = null
        }
    }

    // Assuming this is your existing content
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

                elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
                )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Steps Total",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "From $formattedStartTime to $formattedEndTime",
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$stepCount steps",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

        }
    }

}

@Composable
fun AggregateHeartRateCard(aircareManager: AircareManager, cardIndex: Int) {
    var heartRateAng by remember { mutableStateOf<String?>("Loading...") }
    val startTime = ZonedDateTime.now().minusMonths(1).toInstant()
    val endTime = Instant.now()

    val formattedStartTime = formatInstantToDate(startTime, "yyyy-MM-dd")
    val formattedEndTime = formatInstantToDate(endTime, "yyyy-MM-dd")

    val heartRateAggregator = HeartRateAggregator()
    LaunchedEffect(key1 = aircareManager) {
        try {

            val reponseHeartRate = heartRateAggregator.aggregate(aircareManager.healthConnectClient, startTime, endTime)
            heartRateAng = reponseHeartRate.toString()

        } catch (e: Exception) {
            // Handle exceptions
            heartRateAng = null
        }
    }

    // Assuming this is your existing content
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Heart Rate",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "From $formattedStartTime to $formattedEndTime",
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$heartRateAng bpm ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
}

@Composable
fun AggregateDistanceCard(aircareManager: AircareManager, cardIndex: Int) {
    var distanceCount by remember { mutableStateOf<String?>("Loading...") }
    val startTime = ZonedDateTime.now().minusMonths(1).toInstant()
    val endTime = Instant.now()

    val formattedStartTime = formatInstantToDate(startTime, "yyyy-MM-dd")
    val formattedEndTime = formatInstantToDate(endTime, "yyyy-MM-dd")

    val distanceAggregator = DistanceTotalAggregator()
    LaunchedEffect(key1 = aircareManager) {
        try {

            val reponseDistance = distanceAggregator.aggregate(aircareManager.healthConnectClient, startTime, endTime)
            distanceCount = reponseDistance.toString()

        } catch (e: Exception) {
            // Handle exceptions
            distanceCount = null
        }
    }

    // Assuming this is your existing content
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),


        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = "Total distance",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "From $formattedStartTime to $formattedEndTime",
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$distanceCount km ",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

        }
    }

}

@Composable
fun AggregateSleepCard(aircareManager: AircareManager, cardIndex: Int) {
    var sleepTotal by remember { mutableStateOf<String?>("Loading...") }
    val startTime = ZonedDateTime.now().minusMonths(1).toInstant()
    val endTime = Instant.now()

    val formattedStartTime = formatInstantToDate(startTime, "yyyy-MM-dd")
    val formattedEndTime = formatInstantToDate(endTime, "yyyy-MM-dd")

    val sleepAggregator = SleepSessionAggregator()
    LaunchedEffect(key1 = aircareManager) {
        try {
            val sleepSession = sleepAggregator.aggregate(aircareManager.healthConnectClient, startTime, endTime)
            sleepTotal = sleepSession.toString()
        } catch (e: Exception) {
            // Handle exceptions
            sleepTotal = null
        }
    }

    // Assuming this is your existing content
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Sleep hours",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "From $formattedStartTime to $formattedEndTime",
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$sleepTotal hours ",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }

}

@Composable
fun AggregateCaloriesCard(aircareManager: AircareManager, cardIndex: Int) {
    var caloriesTotal by remember { mutableStateOf<String?>("Loading...") }
    val startTime = ZonedDateTime.now().minusMonths(1).toInstant()
    val endTime = Instant.now()

    val formattedStartTime = formatInstantToDate(startTime, "yyyy-MM-dd")
    val formattedEndTime = formatInstantToDate(endTime, "yyyy-MM-dd")

    val caloriesAggregator = ActiveCaloriesBurnedAggregator()
    LaunchedEffect(key1 = aircareManager) {
        try {

            val caloriesBurnedTotal = caloriesAggregator.aggregate(aircareManager.healthConnectClient, startTime, endTime)
            caloriesTotal = caloriesBurnedTotal.toString()
        } catch (e: Exception) {
            // Handle exceptions
            caloriesTotal = null
        }
    }

    // Assuming this is your existing content
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Total calories",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "From $formattedStartTime to $formattedEndTime",
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$caloriesTotal kcal ",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }

}


fun formatInstantToDate(instant: Instant, pattern: String): String {
    val dateFormatter = DateTimeFormatter.ofPattern(pattern)
    return instant.atZone(ZonedDateTime.now().zone).format(dateFormatter)
}