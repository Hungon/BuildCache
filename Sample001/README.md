# Globee

```
App name: Abceed
OS: Android
language: Kotlin 1.3.72
IDE: Android Studio 4.0
Author: Kohei Moroi
```

## 概要
アプリ起動時にサーバーから書籍データを取得し、アプリ内で種類毎に画像をリスト形式で
表示します。<br>また、書籍紹介ページでは`MyBook`へ登録することができます。

### 機能 
* リスト更新

上から下に画面をスワイプすることで、リストを最新のデータに更新できます。<br>
（サーバーへ問い合わせて最新のデータを取得し、再表示します）
* フィルタリング

タイトルバーのタブから任意の種別を選択する事でフィルタリングできます。
* 書籍紹介

リストから任意の書籍の画像を押下すると、書籍紹介のページへ遷移します。
書籍紹介ページでは、著者や出版社など書籍の説明に加えて、`MyBook`へ追加することができます。

### 主な使用API
* Coroutine
* SwipeRefreshLayout
* OkHttp
* Gson
* RecyclerView
* dataBinding
* ViewModel
* LifecycleObserver
* Picasso
* Realm

### 注意点
`dataBinding`を使用しているため、Android Studioのバグでたまに`ActivityMainBinding`<br> `ActivityDetailBinding`が読み込めないエラーが発生します。<br>
その際は、お手数ですが`Invalidate and Restart`でAndroid Studioを再起動するか、<br>
`Clean Project`を試してください。