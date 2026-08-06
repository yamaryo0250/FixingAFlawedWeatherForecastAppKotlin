package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.repository.WeatherRepository
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.repository.WeatherRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherModule {

    @Singleton
    @Binds
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}
