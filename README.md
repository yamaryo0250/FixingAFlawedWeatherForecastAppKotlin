# FixingAFlawedWeatherForecastAppKotlin
欠陥のある天気予報アプリ(指摘、改修、練習用)　※Kotlin対応版

## Javaベースのレガシーコードをベースに、Kotlinでの実装を実現するために
* プロジェクト作成時は、「**Empty Activity**」を選択
  * ※`Basic Views Activity`を選択すると、所々でKotlinが動かず、別途対応が必要になる
* themes.xml
  * parentの指定を、「Theme.AppCompat.DayNight.NoActionBar」に修正
    * 例
      ```
      <?xml version="1.0" encoding="utf-8"?>
      <resources>
      
          <style name="Theme.FixingAFlawedWeatherForecastAppKotlin" parent="Theme.AppCompat.DayNight.NoActionBar" />
      </resources>
      ```
* Activity
  * **AppCompatActivity**を継承する
    * 「ComponentActivity」だと、一部レガシーコードが動かない可能性がある
