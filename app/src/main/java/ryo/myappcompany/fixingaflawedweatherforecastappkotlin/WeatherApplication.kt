package ryo.myappcompany.fixingaflawedweatherforecastappkotlin

import android.app.Application
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.repository.WeatherRepository
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.repository.WeatherRepositoryImpl

class WeatherApplication : Application() {
    val weatherClient: WeatherClient by lazy { WeatherClient(this) }
    val weatherRepository: WeatherRepository by lazy { WeatherRepositoryImpl(weatherClient) }
}