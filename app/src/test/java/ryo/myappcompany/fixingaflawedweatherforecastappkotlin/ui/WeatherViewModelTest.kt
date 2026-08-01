@file:Suppress("NonAsciiCharacters")
@file:OptIn(ExperimentalCoroutinesApi::class)

package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.domain.WeatherInfo
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.repository.WeatherRepository
import kotlin.time.Duration.Companion.milliseconds

class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel

    // 依存するRepositoryのモック（MockKを使用してモック化する想定）
    // ヒント1: 実際のネットワーク通信を発生させないために、Repositoryは偽物(モック)を用意します
    private val weatherRepository: WeatherRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // ヒント2: 各テスト(@Test)が実行される前に毎回呼ばれる初期化処理です。
        // ここで Repositoryのモック生成 と ViewModelのインスタンス生成 を行います。
        viewModel = WeatherViewModel(weatherRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun 初期状態がDefaultであることを確認する() {
        // Arrange (準備): 今回はsetUpで完了しているため特になし

        // Act (実行): ViewModelの現在の状態(WeatherUiState)を取得する
        val weatherUiState = viewModel.weatherUiState.value

        // Assert (検証): 取得した状態が WeatherUiState.Default であるか Assert(アサート) する
        Assert.assertEquals(WeatherUiState.Default, weatherUiState)
    }

    @Test
    fun データ取得開始直後に_状態がLoadingになることを確認する() = runTest {
        // Arrange (準備):
        val mockWeatherInfo = WeatherInfo(
            weather = "Sunny",
            description = "perfectly clear day",
            temperature = 20.0,
            humidity = 70
        )

        coEvery { weatherRepository.fetchWeatherData(any()) } coAnswers {
            kotlinx.coroutines.delay(10000.milliseconds) // 10秒の遅延を仕込む
            mockWeatherInfo
        }

        // Act (実行):
        viewModel.fetchWeatherData("130010")

        // Assert (検証):
        Assert.assertEquals(WeatherUiState.Loading, viewModel.weatherUiState.value)
    }

    @Test
    fun データ取得に成功した場合_状態がSuccessになることを確認する() = runTest {
        // Arrange (準備):
        // ヒント3: Repositoryのモックが「fetchWeatherを呼ばれたら、正常なWeatherInfoを返す」ように振る舞い(スタブ)を定義します。MockKの「coEvery」について調べてみてください。
        val mockWeatherInfo = WeatherInfo(
            weather = "Sunny",
            description = "perfectly clear day",
            temperature = 20.0,
            humidity = 70
        )
        coEvery { weatherRepository.fetchWeatherData("130010") } returns mockWeatherInfo

        // Act (実行):
        // viewModel.fetchWeatherData("130010") を実行する
        viewModel.fetchWeatherData("130010")
        advanceUntilIdle()

        // Assert (検証):
        // ViewModelの状態が Success になっており、かつ中身の天気が期待通りかチェックする
        Assert.assertEquals(
            WeatherUiState.Success(mockWeatherInfo),
            viewModel.weatherUiState.value)
    }

    @Test
    fun データ取得に失敗した場合_状態がErrorになることを確認する() = runTest {
        // Arrange (準備):
        coEvery { weatherRepository.fetchWeatherData(any()) } throws Exception("通信エラー発生")

        // Act (実行):
        viewModel.fetchWeatherData("130010")
        advanceUntilIdle()

        // Assert (検証):
        Assert.assertEquals(
            WeatherUiState.Error,
            viewModel.weatherUiState.value
        )
    }

    @Test
    fun 取得中の場合に再度データ取得を要求すると_リポジトリの通信処理が実行されないことを確認する() = runTest {
        // Arrange (準備):
        val mockWeatherInfo = WeatherInfo(
            weather = "Sunny",
            description = "perfectly clear day",
            temperature = 20.0,
            humidity = 70
        )

        coEvery { weatherRepository.fetchWeatherData(any()) } coAnswers {
            kotlinx.coroutines.delay(10000.milliseconds) // 10秒の遅延を仕込み、その間に2回目の呼び出しを実行想定
            mockWeatherInfo
        }

        // Act (実行):
        viewModel.fetchWeatherData("130010") // 1回目の呼び出し
        viewModel.fetchWeatherData("130010") // 2回目の呼び出し

        // Assert (検証①): 2回目の呼び出し直後、かつ処理完了前の段階で、Loadingであること
        Assert.assertEquals(WeatherUiState.Loading, viewModel.weatherUiState.value)

        advanceUntilIdle()

        // Assert (検証②): 取得処理が1回だけ走っていること(2回目の要求はブロックされていること)
        coVerify(exactly = 1) { weatherRepository.fetchWeatherData(any()) }
    }

}
