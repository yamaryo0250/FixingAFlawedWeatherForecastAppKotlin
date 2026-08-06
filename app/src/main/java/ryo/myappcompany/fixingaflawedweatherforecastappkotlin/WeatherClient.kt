package ryo.myappcompany.fixingaflawedweatherforecastappkotlin

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class WeatherClient @Inject constructor() {

    /**
     * 指定した都市コードの天気予報データを取得する
     */
    suspend fun fetchWeatherData(cityId: String): String = withContext(Dispatchers.IO) {
        Log.d("WeatherClient", "Fetching weather data for $cityId...")

        // 実際のネットワーク通信はメインスレッドで実行するとNetworkOnMainThreadExceptionで
        // クラッシュするため、ここでは重い同期通信処理をThread.sleepでシミュレートしています。
        delay(5000.milliseconds)

        // APIから返却された想定のJSON文字列
        val response = """
            {
                "weather": [{"main": "Rain", "description": "heavy intensity rain"}],
                "main": {"temp": 12.5, "humidity": 80}
            }
        """.trimIndent()

        return@withContext response
    }
}
