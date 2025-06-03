# 経費申請システム（交通費・業務経費対応）

## 🌟 アプリ概要

このアプリは、社員が月ごとの交通費・業務経費をオンラインで申請できるシステムです。
申請内容は管理者画面から確認・承認ができ、領収書画像のアップロードもサポートしています。

## 🎯 開発の目的

- Excelや紙で行っていた経費申請を効率化したい
- 領収書の添付忘れや入力ミスを防ぎたい
- Spring Boot + フロントエンドの技術で実践的なWebアプリを作りたかった

## 🔧 使用技術

| 項目         | 使用技術                       |
|--------------|--------------------------------|
| 言語         | Java (21), HTML/CSS/JavaScript |
| フレームワーク | Spring Boot, Thymeleaf          |
| DB           | SQLServer                          |
| その他       | Bootstrap, Maven, Eclipse       |

## 📸 画面イメージ

【申請者用画面】
- ログイン画面
- 申請一覧（マイページ）  
- 申請詳細画面
- 新規申請フォーム  
- 領収書画像アップロード機能

【管理者用画面】
- ログイン画面
- 申請一覧（マイページ）
- 申請詳細画面
- 申請内容の承認・却下機能

（→ スクリーンショットを画像として貼る）

## 🔗 GitHubリポジトリ

https://github.com/ishikawa-dev/shinsei-app

## 🚀 起動方法

```bash
git clone https://github.com/your-account/expense-app.git
cd expense-app
./mvnw spring-boot:run