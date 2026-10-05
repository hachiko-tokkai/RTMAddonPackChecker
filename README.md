# RTM Addon Pack Checker

Minecraft Forge 1.7.10 / RTM向けの、サーバーとクライアントの追加パックが一致するか確認するMODです。

[ダウンロード](https://github.com/hachiko-tokkai/RTMAddonPackChecker/releases/latest) · [不具合報告](https://github.com/hachiko-tokkai/RTMAddonPackChecker/issues)

## 概要

サーバー接続時にRTM追加パックを比較し、不一致があるクライアントの接続を拒否します。
パックの更新漏れや、サーバーと異なるファイルの使用を接続時に確認できます。サーバーと全クライアントへの導入が必要です。

## 対応環境

| 必要なもの | バージョン |
|---|---|
| Minecraft | 1.7.10 |
| Minecraft Forge | 10.13.4.1614 |
| 接続試験を実施したRTM環境 | KaizPatchX 1.10.1 |

KaizPatchX 1.10.1環境でサーバー・クライアント接続試験済みです。
KaizPatchX固有APIには依存していませんが、ほかのバージョンおよび公式RTM環境は未検証です。

## 注意事項

- サーバーとクライアントで、追加パックの内容と`mods`からの相対パスを揃える必要があります。
- パックを自動ダウンロード・更新する機能はありません。不一致が表示された場合は手動で更新してください。
- 最終更新日時の比較は初期設定では無効です。コピーや展開で日時が変わるため、内容が同じでも拒否される場合があります。

## 導入方法

1. サーバーとMinecraftを終了します。
2. [リリースページ](https://github.com/hachiko-tokkai/RTMAddonPackChecker/releases/latest)から最新版のJARをダウンロードします。
3. サーバーと全クライアントの`mods`フォルダーへ入れます。
4. 追加パックをサーバーとクライアントで同じ相対パスに配置します。
5. サーバーとMinecraftを起動します。

同じMODの古いJARがある場合は、取り除いてから新しいJARを入れてください。

## 主な機能

| 機能 | 内容 |
|---|---|
| パック検出 | `mods`以下を再帰検索し、`Model*.json`を含むZIP/JARを追加パックとして扱う |
| 内容比較 | 相対パス・ファイルサイズ・SHA-256を比較 |
| 日時比較 | 設定を有効にした場合のみ、最終更新日時も比較 |
| 不一致通知 | 接続を拒否し、切断画面へ差分を表示 |
| ログ出力 | 全差分をサーバーログへ出力 |

SHA-256が一致すれば、初期設定では更新日時が違っても同一内容として扱います。

## 設定

設定ファイルは`config/rtmaddonpackchecker.cfg`です。サーバーまたはMinecraftを終了してから編集してください。

| 項目 | 内容 | 初期値 |
|---|---|---|
| `compareLastModified` | 最終更新日時も比較する | OFF |
| `maximumPacks` | 検出する追加パック数の上限 | 2048 |
| `maximumDifferencesInKickMessage` | 切断画面に表示する差分の最大数 | 8 |

更新日時まで完全一致させる場合にのみ`compareLastModified=true`にしてください。
切断画面の表示数を超えた差分も、サーバーログには出力されます。

## 無効化・削除

サーバーとMinecraftを終了し、サーバーと全クライアントの`mods`から本MODのJARを取り除いてください。

## 不具合報告

[Issues](https://github.com/hachiko-tokkai/RTMAddonPackChecker/issues)に、Minecraft・Forge・RTM環境のバージョン、再現手順、切断画面の表示、サーバーログを記載してください。

## 開発

Java 8を使用し、リポジトリのルートで実行します。別途Gradleをインストールする必要はありません。

Windows：

```powershell
.\gradlew.bat clean build
```

Linux・macOS：

```bash
./gradlew clean build
```

生成先は`build/libs/RTMAddonPackChecker-1.0.0.jar`です。

## 生成AIの利用

設計、コード生成・修正、文書作成にOpenAI Codexを使用しています。

## ライセンス・免責事項

本プロジェクトのソースコードとドキュメントには[MIT License](LICENSE)を適用しています。

Copyright (c) 2026 hachiko-tokkai

本MODは現状のまま提供します。動作・互換性・安全性を保証せず、使用に伴う不具合や損害について、作者は適用法令で認められる範囲において責任を負いません。詳細はLICENSEを確認してください。
