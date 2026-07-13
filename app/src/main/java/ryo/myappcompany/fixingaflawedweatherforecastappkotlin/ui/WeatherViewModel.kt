package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.repository.WeatherRepository

/**
 * 天気情報を取得、状態保持するviewModel
 *
 * @param weatherRepository 天気情報リポジトリ(WeatherRepository)
 */
class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherUiState = MutableLiveData<WeatherUiState>(WeatherUiState.Default)

    val weatherUiState: LiveData<WeatherUiState>
        get() = _weatherUiState

    /**
     * 天気情報取得
     *
     * @param cityId 都市ID
     *
     * @return 天気情報
     */
    fun fetchWeatherData(cityId: String) {
        viewModelScope.launch {
            _weatherUiState.value = WeatherUiState.Loading

            try {
                _weatherUiState.value =
                    WeatherUiState.Success(weatherRepository.fetchWeatherData(cityId))

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _weatherUiState.value = WeatherUiState.Error
                throw e
            }
        }
    }
}
