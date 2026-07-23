package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.WeatherClient
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.domain.WeatherInfo
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.dto.WeatherResponseDto
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.mapper.toDomain

/**
 * 天気情報リポジトリ 実ロジック
 */
class WeatherRepositoryImpl(
    private val weatherClient: WeatherClient
) : WeatherRepository {

    /**
     * 天気情報取得
     *
     * @param cityId 都市ID
     *
     * @return 天気情報
     */
    override suspend fun fetchWeatherData(cityId: String): WeatherInfo {

        return try {
            val weatherData = weatherClient.fetchWeatherData(cityId)

            withContext(Dispatchers.Default) {
                val weatherResponseDto = Json.decodeFromString<WeatherResponseDto>(weatherData)

                weatherResponseDto.toDomain()
            }
        } catch (e: Exception) {
            if (e is kotlinx.coroutines.CancellationException) {
                throw e
            }

            throw DataFetchException(e)
        }
    }
}

class DataFetchException(cause: Throwable? = null) : Exception(cause)
