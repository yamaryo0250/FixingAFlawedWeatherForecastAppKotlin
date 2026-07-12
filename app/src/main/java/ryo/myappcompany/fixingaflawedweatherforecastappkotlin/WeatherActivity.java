package ryo.myappcompany.fixingaflawedweatherforecastappkotlin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class WeatherActivity extends AppCompatActivity {

    private TextView tvResult;
    private Button btnFetch;

    private WeatherClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvResult = findViewById(R.id.tv_result);
        btnFetch = findViewById(R.id.btn_fetch);

        // APIクライアントの初期化
        apiClient = new WeatherClient(this);

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ローディング中であることをユーザーに知らせる意図のコード
                tvResult.setText("天気データを取得中...");

                // 非同期で通信処理を行うためにFutureTaskを利用
                FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        // "130010" は東京の都市コードの想定
                        return apiClient.fetchWeatherData("130010");
                    }
                });

                Thread thread = new Thread(futureTask);
                thread.start();

                try {
                    // 通信結果を受け取る
                    String jsonResponse = futureTask.get();

                    // 取得したJSON文字列のパース処理
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    JSONArray weatherArray = jsonObject.getJSONArray("weather");
                    JSONObject weatherObj = weatherArray.getJSONObject(0);
                    String condition = weatherObj.getString("main");

                    JSONObject mainObj = jsonObject.getJSONObject("main");
                    double temp = mainObj.getDouble("temp");

                    // 画面に結果を表示
                    tvResult.setText("今日の天気: " + condition + "\n気温: " + temp + "度");

                } catch (Exception e) {
                    e.printStackTrace();
                    tvResult.setText("データの取得に失敗しました");
                }
            }
        });
    }
}
