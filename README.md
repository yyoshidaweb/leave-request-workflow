# 🏝️ Leave Request Workflow

## 概要
本システムは、業務システムにおける休暇申請ワークフローを再現したデモアプリです。申請から承認、却下等の一連の流れをWeb上で体験できます。 

## 休暇申請作成〜承認のデモ


https://github.com/user-attachments/assets/1291bcbb-f6ab-483a-8730-ee44502a566f



## 環境構築手順
以下のWikiを参照してください。

* [開発環境構築方法（JavaをMacOSのVScodeで使う場合） · yyoshidaweb/leave\-request\-workflow Wiki](https://github.com/yyoshidaweb/Leave-request-workflow/wiki/%E9%96%8B%E7%99%BA%E7%92%B0%E5%A2%83%E6%A7%8B%E7%AF%89%E6%96%B9%E6%B3%95%EF%BC%88Java%E3%82%92MacOS%E3%81%AEVScode%E3%81%A7%E4%BD%BF%E3%81%86%E5%A0%B4%E5%90%88%EF%BC%89)

## 使用技術
- 言語: Java 17  
- フレームワーク: Spring Boot 3.5.6

- テンプレート: Thymeleaf  
- データベース: 
  - ローカル開発: H2（組み込み型インメモリDB）  
  - 本番環境: PostgreSQL
- ビルドツール: Gradle
- コンテナ: Docker
- デプロイ: Render


## 実装機能
- **ユーザー管理**
  - 社員 / 管理者アカウントを作成
  - Spring Security による認証・権限分離
- **休暇申請**
  - 社員が申請フォームから休暇を登録
- **承認フロー**
  - 管理者が申請一覧を確認し、「承認」または「却下」を選択可能
  - ステータスが `承認前 → 承認済 / 却下` に更新
 

## 制作背景
前職では、大規模業務システムのテスト工程（結合テスト〜総合テスト）に従事していました。  
実際のプロジェクトでは複雑なワークフローが存在しましたが、守秘義務の観点から詳細を再現することはできません。  

そこで本ポートフォリオでは、業務システムの典型例として  
一般的な「休暇申請フロー」を題材に選び、最小限の機能で再現しています。  



## 本プロジェクトのWiki
* [Home · yyoshidaweb/leave\-request\-workflow Wiki](https://github.com/yyoshidaweb/leave-request-workflow/wiki)
    * 重要な機能の実装手順や、Tipsなどをまとめています
