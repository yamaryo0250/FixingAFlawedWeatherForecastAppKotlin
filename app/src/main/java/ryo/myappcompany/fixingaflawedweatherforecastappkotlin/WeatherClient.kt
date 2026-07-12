package ryo.myappcompany.fixingaflawedweatherforecastappkotlin

import android.content.Context
import android.util.Log

class WeatherClient(private var context: Context?) {

    /**
     * 指定した都市コードの天気予報データを取得する
     */
    fun fetchWeatherData(cityId: String): String {
        Log.d("WeatherClient", "Fetching weather data for $cityId...")

        // 実際のネットワーク通信はメインスレッドで実行するとNetworkOnMainThreadExceptionで
        // クラッシュするため、ここでは重い同期通信処理をThread.sleepでシミュレートしています。
        Thread.sleep(5000)

        // APIから返却された想定のJSON文字列
        val response = """
            {
                "weather": [{"main": "Rain", "description": "heavy intensity rain"}],
                "main": {"temp": 12.5, "humidity": 80}
            }
        """.trimIndent()

        return response
    }
}
