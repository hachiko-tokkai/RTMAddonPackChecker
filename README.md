# RTM Addon Pack Checker

Minecraft Forge 1.7.10 / RealTrainMod向けの、サーバー・クライアント間の追加パック一致確認MODです。

## テスト済み環境

- Minecraft 1.7.10
- Minecraft Forge 10.13.4.1614
- RealTrainMod
- KaizPatchX 1.10.1

## 互換性

KaizPatchX 1.10.1向けに設計していますが、実際のサーバー・クライアント接続試験は未実施です。
KaizPatchXのほかのバージョン、および公式RTM環境は未検証です。

本MODはKaizPatchX固有機能に依存していないため、ほかのRTM環境でも動作する可能性がありますが、互換性は保証していません。

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
