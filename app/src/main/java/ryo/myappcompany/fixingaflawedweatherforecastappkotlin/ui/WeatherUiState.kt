package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.ui

import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.domain.WeatherInfo

/**
 * 天気情報の取得状態を管理するクラス
 */
sealed interface WeatherUiState {

    /**
     * 初期表示
     */
    object Default : WeatherUiState

    /**
     * 取得中
     */
    object Loading : WeatherUiState

    /**
     * 取得成功
     *
     * @param weatherInfo 取得天気情報の配列
     */
    data class Success(val weatherInfo: WeatherInfo) : WeatherUiState

    /**
     * 取得失敗
     */
    object Error : WeatherUiState
}
