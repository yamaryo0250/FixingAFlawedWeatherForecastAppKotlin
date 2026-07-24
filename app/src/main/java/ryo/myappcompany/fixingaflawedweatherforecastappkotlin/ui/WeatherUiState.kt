package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.ui

import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.domain.WeatherInfo

/**
 * 天気情報の取得状態を管理するクラス
 */
sealed interface WeatherUiState {

    /**
     * 初期表示
     */
    data object Default : WeatherUiState

    /**
     * 取得中
     */
    data object Loading : WeatherUiState

    /**
     * 取得成功
     *
     * @param weatherInfo 取得天気情報の配列
     */
    data class Success(val weatherInfo: WeatherInfo) : WeatherUiState

    /**
     * 取得失敗
     */
    data object Error : WeatherUiState
}
