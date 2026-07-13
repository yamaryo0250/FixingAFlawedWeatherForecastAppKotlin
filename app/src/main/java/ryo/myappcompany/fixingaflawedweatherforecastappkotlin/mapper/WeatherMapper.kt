package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.mapper

import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.domain.WeatherInfo
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.dto.WeatherResponseDto

/**
 * responseDto→ドメインクラスへの変換
 *
 * @return ドメインクラス(WeatherInfo)
 */
fun WeatherResponseDto.toDomain(): WeatherInfo {
    val weatherItem = weather.firstOrNull()

    return WeatherInfo(
        weather = weatherItem?.main.orEmpty(),
        description = weatherItem?.description.orEmpty(),
        temperature = main.temp,
        humidity = main.humidity
    )
}
