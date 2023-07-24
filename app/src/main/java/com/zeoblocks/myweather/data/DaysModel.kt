package com.zeoblocks.myweather.data

class DaysModel (
    private val date: String,
    private val dateEpoch: Int,
    private val maxTemp: String,
    private val minTemp: String,
    private val conditionText: String,
    private val conditionImage: String,
    private val hours: List<HoursModel>
) {
    fun getDate(): String {
        return this.date
    }

    fun getDateEpoch(): Int {
        return this.dateEpoch
    }

    fun getMaxTemp(): String {
        return this.maxTemp
    }

    fun getMinTemp(): String {
        return this.minTemp
    }

    fun getConditionText(): String {
        return this.conditionText
    }

    fun getConditionImage(): String {
        return this.conditionImage
    }

    fun getHours(): List<HoursModel> {
        return this.hours
    }

}