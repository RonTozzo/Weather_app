import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("data")
    val data: List<WeatherData>
)

data class WeatherData(
    @SerializedName("city_name")
    val city: String,

    @SerializedName("country_code")
    val country: String,

    @SerializedName("wind_spd")
    val windSpd: Double,

    @SerializedName("temp")
    val temp: Double,

    @SerializedName("precip")
    val precip: Double,

    @SerializedName("rh")
    val humidity: Double,

    @SerializedName("weather")
    val weather: Weather,


)


data class Weather(
    @SerializedName("description")
    val description: String
)
