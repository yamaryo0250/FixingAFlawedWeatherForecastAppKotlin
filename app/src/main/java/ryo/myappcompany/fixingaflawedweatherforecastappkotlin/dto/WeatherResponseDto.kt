package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.dto

import kotlinx.serialization.Serializable

/**
 * APIの結果レスポンス
 *
 * @param weather 天気(main)と、説明(description)をペアとしたひとまとまりの情報
 * @param main　温度(temp)や、湿度(humidity)の情報
 */
@Serializable
data class WeatherResponseDto(
    val weather: List<WeatherDto>,
    val main: MainDto
)

/**
 * 天気(main)と、説明(description)をペアとした
 *
 * @param main 天気
 * @param description 説明
 */
@Serializable
data class WeatherDto(
    val main: String,
    val description: String
)

/**
 * 温度(temp)や、湿度(humidity)の情報
 *
 * @param temp 温度
 * @param humidity 湿度
 */
@Serializable
data class MainDto(
    val temp: Double,
    val humidity: Int
)
