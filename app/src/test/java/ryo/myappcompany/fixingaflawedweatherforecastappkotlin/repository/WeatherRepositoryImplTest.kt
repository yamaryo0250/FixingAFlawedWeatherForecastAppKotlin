@file:Suppress("NonAsciiCharacters")

package ryo.myappcompany.fixingaflawedweatherforecastappkotlin.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.WeatherClient
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.domain.WeatherInfo
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    // テスト対象
    private lateinit var repository: WeatherRepositoryImpl
    // モック化
    private val weatherClient: WeatherClient = mockk()

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(weatherClient, UnconfinedTestDispatcher())
    }

    @Test
    fun Clientが例外を投げた場合_DataFetchExceptionに変換されてスローされることを確認する() = runTest {
        // Arrange (準備):
        coEvery { weatherClient.fetchWeatherData(any()) } throws RuntimeException("通信エラー発生")

        // Act (実行): / Assert (検証):
        assertFailsWith<DataFetchException> {
            repository.fetchWeatherData("130010")
        }
    }

    @Test
    fun Clientが正常なJSONを返した場合_WeatherInfoに変換されて返されることを確認する() = runTest {
        // Arrange (準備):
        val response = """
            {
                "weather": [{"main": "Rain", "description": "heavy intensity rain"}],
                "main": {"temp": 12.5, "humidity": 80}
            }
        """.trimIndent()

        coEvery { weatherClient.fetchWeatherData(any()) } returns response

        val mockWeatherInfo = WeatherInfo(
            weather = "Rain",
            description = "heavy intensity rain",
            temperature = 12.5,
            humidity = 80
        )

        // Act (実行): / Assert (検証):
        Assert.assertEquals(mockWeatherInfo, repository.fetchWeatherData("130010"))

        // Assert (検証): WeatherClientのfetchWeatherDataが呼ばれたこと
        coVerify(exactly = 1) { weatherClient.fetchWeatherData("130010") }
    }

    @Test
    fun ClientがCancellationExceptionを投げた場合_ラップされずにそのままスローされることを確認する() = runTest {
        // Arrange (準備):
        coEvery { weatherClient.fetchWeatherData(any()) } throws kotlinx.coroutines.CancellationException()

        // Act (実行): / Assert (検証):
        assertFailsWith<kotlinx.coroutines.CancellationException> {
            repository.fetchWeatherData("130010")
        }
    }
}
