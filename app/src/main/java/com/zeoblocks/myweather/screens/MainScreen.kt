package com.zeoblocks.myweather.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zeoblocks.myweather.R
import com.zeoblocks.myweather.data.DaysModel
import com.zeoblocks.myweather.data.HoursModel
import com.zeoblocks.myweather.data.WeatherModel
import com.zeoblocks.myweather.ui.theme.PurpleGrey80
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Composable
fun MainList(weatherModel: MutableState<WeatherModel>, selectedScreen: MutableState<Int>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(weatherModel.value.getDays()) { _, day ->
            when (selectedScreen.value) {
                0 -> getWeatherByHours(
                    day.getHours(),
                    weatherModel.value.getDateEpoch()
                ).forEach { hour ->
                    Hours(item = hour)
                }

                1 -> Days(item = day, weatherModel)
            }
        }
    }
}

@Composable
fun MainCard(weatherModel: MutableState<WeatherModel>) {
    Column(
        modifier = Modifier.padding(7.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PurpleGrey80),
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(top = 5.dp, start = 5.dp),
                        text = weatherModel.value.getDate(),
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White
                    )
                    AsyncImage(
                        model = "https:${weatherModel.value.getConditionImage()}",
                        contentDescription = "weatherImage",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(top = 3.dp, end = 8.dp)
                    )
                }
                Text(
                    text = weatherModel.value.getCity(),
                    style = TextStyle(fontSize = 30.sp),
                    color = Color.White
                )
                Text(
                    text = "${weatherModel.value.getCurrentTemp()} C",
                    style = TextStyle(fontSize = 70.sp),
                    color = Color.White
                )
                Text(
                    text = weatherModel.value.getConditionText(),
                    style = TextStyle(fontSize = 15.sp),
                    color = Color.White
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "icon1",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.sync),
                            contentDescription = "icon1",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TabLayout(weatherModel: MutableState<WeatherModel>) {
    val tabList = listOf("ЧАСЫ", "ДНИ")
    val selectedScreen = remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(5.dp))
    ) {
        TabRow(
            selectedTabIndex = selectedScreen.value, containerColor = PurpleGrey80
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = selectedScreen.value == index,
                    onClick = { selectedScreen.value = index },
                    text = {
                        Text(text = text, color = Color.White, overflow = TextOverflow.Clip)
                    },
                )
            }
        }
        MainList(weatherModel, selectedScreen)
    }
}

@Composable
fun Hours(item: HoursModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = PurpleGrey80)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.padding(
                    start = 5.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
            ) {
                Text(
                    text = item.getTime(),
                    color = Color.White
                )
                Text(
                    text = item.getConditionText(),
                    color = Color.White
                )
            }
            Text(
                text = item.getTemp(),
                color = Color.White
            )
            AsyncImage(
                model = "https:${item.getConditionImage()}",
                contentDescription = "weatherImage",
                modifier = Modifier
                    .size(35.dp)
                    .padding(top = 3.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun Days(item: DaysModel, currentDay: MutableState<WeatherModel>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clickable {
                val currentDataByHour = getCurrentDataByHour(item.getHours())
                currentDay.value = WeatherModel(
                    currentDay.value.getCity(),
                    item.getDate(),
                    item.getDateEpoch(),
                    currentDataByHour.getTemp(),
                    currentDataByHour.getConditionText(),
                    currentDataByHour.getConditionImage(),
                    currentDay.value.getDays()
                )
            },
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = PurpleGrey80)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.padding(
                    start = 5.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
            ) {
                Text(
                    text = item.getDate(),
                    color = Color.White
                )
                Text(
                    text = item.getConditionText(),
                    color = Color.White
                )
            }
            Text(
                text = "${item.getMinTemp()}/${item.getMaxTemp()}",
                color = Color.White
            )
            AsyncImage(
                model = "https:${item.getConditionImage()}",
                contentDescription = "weatherImage",
                modifier = Modifier
                    .size(35.dp)
                    .padding(top = 3.dp, end = 8.dp)
            )
        }
    }
}


fun checkDates(date1: Int, date2: Int): Boolean {
    val sdf = SimpleDateFormat("MM/dd/yyyy")
    val firstDate = sdf.format(Date(date1.toLong() * 1000))
    val secondDate = sdf.format(Date(date2.toLong() * 1000))
    Log.d("MyLog", "$firstDate and $secondDate")
    return firstDate == secondDate
}

fun getWeatherByHours(hoursList: List<HoursModel>, date: Int): List<HoursModel> {
    val dayByHours = mutableListOf<HoursModel>()
    hoursList.forEach { hour ->
        if (checkDates(date, hour.getTimeEpoch())) {
            dayByHours.add(hour)
        }
    }
    return dayByHours
}

fun getCurrentDataByHour(hours: List<HoursModel>): HoursModel {
    val sdf = SimpleDateFormat("MM/dd/yyyy HH")
    val epochNanoseconds = Instant.now().toEpochMilli()
    val firstDate = sdf.format(Date(epochNanoseconds))
    hours.forEach { item ->
        if (firstDate == sdf.format(Date(item.getTimeEpoch().toLong() * 1000)))
            return item
    }
    return HoursModel(
        "empty",
        0,
        "empty",
        "empty",
        "empty"
    )
}
