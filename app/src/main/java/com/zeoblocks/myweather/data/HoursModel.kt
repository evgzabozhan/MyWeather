package com.zeoblocks.myweather.data

class HoursModel(
    private val time: String,
    private val timeEpoch: Int,
    private val temp: String,
    private val conditionText: String,
    private val conditionImage: String
) {
    fun getTime(): String {
        return this.time
    }

    fun getTimeEpoch(): Int {
        return this.timeEpoch
    }

     fun getTemp(): String {
         return this.temp
    }

    fun getConditionText(): String {
        return this.conditionText
    }

    fun getConditionImage(): String {
        return this.conditionImage
    }
}
