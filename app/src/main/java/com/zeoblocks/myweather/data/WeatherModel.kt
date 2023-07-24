package com.zeoblocks.myweather.data

data class WeatherModel(
    private val city: String,
    private var date: String,
    private var dateEpoch: Int,
    private var currentTemp: String,
    private var conditionText: String,
    private var conditionImage: String,
    private val days: List<DaysModel>
) {
    fun getCity(): String {
        return this.city
    }

    fun getDate(): String {
        return this.date
    }

    fun getDateEpoch(): Int {
        return this.dateEpoch
    }

    fun getCurrentTemp(): String {
        return this.currentTemp
    }

    fun getConditionText(): String {
        return this.conditionText
    }

    fun getConditionImage(): String {
        return this.conditionImage
    }

    fun getDays(): List<DaysModel> {
        return this.days
    }

    fun setDate(date: String){
        this.date = date
    }

    fun setDateEpoch(dateEpoch: Int) {
        this.dateEpoch = dateEpoch
    }

    fun setCurrentTemp(currentTemp: String){
        this.currentTemp = currentTemp
    }

    fun setConditionText(conditionText: String){
        this.conditionText = conditionText
    }

    fun setConditionImage(conditionImage: String){
        this.conditionImage = conditionImage
    }
}
