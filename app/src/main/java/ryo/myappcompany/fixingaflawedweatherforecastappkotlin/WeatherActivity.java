package ryo.myappcompany.fixingaflawedweatherforecastappkotlin;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.databinding.ActivityWeatherBinding;
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.ui.WeatherViewModel;
import ryo.myappcompany.fixingaflawedweatherforecastappkotlin.ui.WeatherUiState;

@AndroidEntryPoint
public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";
    private ActivityWeatherBinding binding;
    private WeatherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel =
                new ViewModelProvider(this).get(WeatherViewModel.class);

        observeViewModel();

        binding.btnFetch.setOnClickListener(view -> {
            Log.d(TAG, "btnFetch clicked");

            viewModel.fetchWeatherData("130010");
        });
    }

    /**
     * LiveData監視の登録
     */
    private void observeViewModel() {
        // 取得状況表示
        viewModel.getWeatherUiState().observe(this, weatherUiState -> {
            Log.d(TAG, "weatherUiState updating");
            if (weatherUiState instanceof WeatherUiState.Loading) {
                Log.d(TAG, "weatherUiState is Loading");
                // 取得中表示
                binding.tvResult.setText(getString(R.string.msg_fetching_weather));

                // ボタン連打制御
                binding.btnFetch.setEnabled(false);
                return;
            }

            binding.btnFetch.setEnabled(true);

            if (weatherUiState instanceof WeatherUiState.Success) {
                Log.d(TAG, "weatherUiState is Success");

                WeatherUiState.Success successState = (WeatherUiState.Success) weatherUiState;
                // 取得成功表示
                // 天気
                String condition =
                        successState.getWeatherInfo().getWeather();
                // 気温
                double temp =
                        successState.getWeatherInfo().getTemperature();

                binding.tvResult.setText(
                        getString(R.string.msg_weather_result, condition, String.valueOf(temp)));
            } else if (weatherUiState instanceof WeatherUiState.Error) {
                Log.d(TAG, "weatherUiState is Error");
                // 取得失敗表示
                binding.tvResult.setText(getString(R.string.msg_fetch_failed));

            } else {
                Log.d(TAG, "weatherUiState is Default");
                // 初期表示
                binding.tvResult.setText(getString(R.string.msg_default_weather));
            }
        });
    }
}
