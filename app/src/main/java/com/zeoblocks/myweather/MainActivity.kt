package com.zeoblocks.myweather

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.zeoblocks.myweather.data.DaysModel
import com.zeoblocks.myweather.data.HoursModel
import com.zeoblocks.myweather.data.WeatherModel
import com.zeoblocks.myweather.screens.DialogSearch
import com.zeoblocks.myweather.screens.MainCard
import com.zeoblocks.myweather.screens.TabLayout
import com.zeoblocks.myweather.ui.theme.MyWeatherTheme
import org.json.JSONObject
import java.io.File


val API_KEY = getApiKey()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWeatherTheme {
                val weatherModel = remember {
                    mutableStateOf(
                        WeatherModel(
                            "",
                            "",
                            0,
                            "",
                            "",
                            "",
                            listOf()
                        )
                    )
                }
                val dialogState = remember {
                    mutableStateOf(false)
                }
                if (dialogState.value){
                    DialogSearch(dialogState, onSubmit = {
                        getData(it, this, weatherModel)
                    })
                }
                getData("Vladimir", this, weatherModel)
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "background",
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.7f),
                    contentScale = ContentScale.FillBounds
                )
                Column {
                    MainCard(
                        weatherModel = weatherModel,
                        onClickSync = {
                            getData("Vladimir", this@MainActivity, weatherModel)
                        },
                        onClickSearch = {
                            dialogState.value = true
                        })
                    TabLayout(weatherModel = weatherModel)
                }
            }
        }
    }
}

private fun getData(city: String, context: Context, weatherModel: MutableState<WeatherModel>) {
    val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
            API_KEY +
            "&q=$city" +
            "&days=7" +
            "&aqi=no" +
            "&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            weatherModel.value = getWeatherModel(response)
        },
        {
            Log.d("MyLog", "VolleyError: $it")
        }
    )
    queue.add(request)
}

private fun getWeatherModel(response: String): WeatherModel {
    val mainObject = JSONObject(response)
    val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
    val daysList = mutableListOf<DaysModel>()
    for (i in 0 until days.length()) {
        val day = days[i] as JSONObject
        val hours = day.getJSONArray("hour")
        val hoursList = mutableListOf<HoursModel>()
        for (z in 0 until hours.length() - 1) {
            val hour = hours[z] as JSONObject
            hoursList.add(
                HoursModel(
                    hour.getString("time"),
                    hour.getInt("time_epoch"),
                    hour.getString("temp_c"),
                    hour.getJSONObject("condition").getString("text"),
                    hour.getJSONObject("condition").getString("icon")
                )
            )
        }
        Log.d("MyLog", "Start $i")
        daysList.add(
            DaysModel(
                day.getString("date"),
                day.getInt("date_epoch"),
                day.getJSONObject("day").getString("maxtemp_c"),
                day.getJSONObject("day").getString("mintemp_c"),
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                hoursList
            )
        )
    }
    val city = mainObject.getJSONObject("location").getString("name")
    val lastUpdated = mainObject.getJSONObject("current").getString("last_updated")
    val lastUpdatedEpoch = mainObject.getJSONObject("current").getInt("last_updated_epoch")
    val currentTemp = mainObject.getJSONObject("current").getString("temp_c")
    val conditionText =
        mainObject.getJSONObject("current").getJSONObject("condition").getString("text")
    val conditionIcon =
        mainObject.getJSONObject("current").getJSONObject("condition").getString("icon")

    return WeatherModel(
        city,
        lastUpdated,
        lastUpdatedEpoch,
        currentTemp,
        conditionText,
        conditionIcon,
        daysList
    )
}

private fun getApiKey(): String {
    return "163f344b982545a1ac352310232107"
}

