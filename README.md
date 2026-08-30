# RTM Addon Pack Checker

Minecraft Forge 1.7.10 / RealTrainMod向けの、サーバー・クライアント間の追加パック一致確認MODです。

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

このワークスペースでは次を実行します。

```powershell
..\KaizPatchX-master\gradlew.bat -p . build
```

生成物は`build/libs/RTMAddonPackChecker-1.0.0.jar`です。
