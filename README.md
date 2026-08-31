# RTM Addon Pack Checker

Minecraft Forge 1.7.10 / RealTrainMod向けの、サーバー・クライアント間の追加パック一致確認MODです。

「パック更新してくださいって何度も何度もずっと言ってるのになぜ更新ぜずにサーバー来るんですか？。更新してないあなた、運転会参加皆様に迷惑かけてますよ。サーバー回線爆破犯になるってずっと言ってたんじゃないですか。言うことを聞いてください。TPS落ちるの絶対あなたのせい。」というやり取りを防ぐMODです。

## 動作確認環境

- Minecraft 1.7.10
- Minecraft Forge 10.13.4.1614
- KaizPatchX 1.10.1

KaizPatchX 1.10.1環境でサーバー・クライアント接続試験済みです。KaizPatchXのほかのバージョンおよび公式RTM環境は未検証です。

本MODはKaizPatchX固有APIには依存していないため、ほかのRTM環境でも動作する可能性がありますが、互換性は保証していません。

## 判定方法

`mods`以下を再帰検索し、`Model*.json`を含むZIP/JARをRTM追加パックとして扱います。ファイルごとに次を比較します。

- `mods`からの相対パス
- ファイルサイズ
- SHA-256
- 最終更新日時（設定で有効化した場合のみ）

SHA-256が一致すれば、更新日時が異なっても既定では同一内容として扱います。更新日時はコピー・ダウンロード・展開で変化しやすいためです。

## 導入

1. `RTMAddonPackChecker-1.0.0.jar`をサーバーと全クライアントの`mods`へ入れます。
2. サーバーとクライアントで同じ相対パスに追加パックを置きます。
3. 接続時に不一致があれば、クライアントを切断して差分を表示します。全差分はサーバーログにも出力します。

設定ファイルは`config/rtmaddonpackchecker.cfg`です。更新日時も完全一致させる場合だけ`compareLastModified=true`にします。

## ビルド

Java 8を使用して、リポジトリのルートで次を実行します。別途Gradleをインストールする必要はありません。

```powershell
.\gradlew.bat clean build
```

Linux・macOSでは次を実行します。

```bash
./gradlew clean build
```

生成物は`build/libs/RTMAddonPackChecker-1.0.0.jar`です。

## AI利用について

本MODはOpenAI Codexを使用したAI支援のバイブコーディングで開発しました。設計、コード生成、修正および文書作成にAIを使用しています。ビルドと動作試験は実施済みです。

## ライセンス

本MODは[MIT License](LICENSE)で公開しています。

Copyright (c) 2026 hachiko-tokkai
