package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.domain

/**
 * アプリ内で使う天気情報(ドメインクラス)
 *
 * @param weather 天気
 * @param description 説明
 * @param temperature 温度
 * @param humidity 湿度
 */
data class WeatherInfo(
    val weather: String,
    val description: String,
    val temperature: Double,
    val humidity: Int
)
