package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.repository.WeatherRepository
import javax.inject.Inject

/**
 * 天気情報を取得、状態保持するviewModel
 *
 * @param weatherRepository 天気情報リポジトリ(WeatherRepository)
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherUiState = MutableLiveData<WeatherUiState>(WeatherUiState.Default)

    val weatherUiState: LiveData<WeatherUiState>
        get() = _weatherUiState

    companion object {
        private const val TAG = "WeatherViewModel"
    }

    /**
     * 天気情報取得
     *
     * @param cityId 都市ID
     *
     * @return 天気情報
     */
    fun fetchWeatherData(cityId: String) {

        // 多重実行防止
        if (weatherUiState.value == WeatherUiState.Loading) {
            Log.d(TAG, "FetchWeatherData cancel. Because already executed")
            return
        }

        viewModelScope.launch {
            Log.i(TAG, "start fetchWeatherData!")
            _weatherUiState.value = WeatherUiState.Loading

            try {
                _weatherUiState.value =
                    WeatherUiState.Success(weatherRepository.fetchWeatherData(cityId))

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _weatherUiState.value = WeatherUiState.Error
                Log.e(TAG, "Failed to fetch weather data", e)
            }
        }
    }
}
