# 経費申請システム（交通費・業務経費対応）

## 🌟 アプリ概要

このアプリは、社員が月ごとの交通費・業務経費をオンラインで申請できるシステムです。
申請内容は管理者画面から確認・承認ができ、領収書画像のアップロードもサポートしています。
本アプリはレスポンシブ対応しており、スマートフォンからも快適に申請・操作できます。

## 🎯 開発の目的

- Excelや紙で行っていた経費申請を効率化したい
- 領収書の添付忘れや申請内容の入力漏れを防ぎたい
- Spring Boot + フロントエンドの技術で実践的なWebアプリを作りたかった

## 🔧 使用技術

| 項目         | 使用技術                       |
|--------------|--------------------------------|
| 言語         | Java (21), HTML/CSS/JavaScript |
| フレームワーク | Spring Boot, Thymeleaf          |
| DB           | SQLServer                          |
| その他       | Bootstrap, Maven, Eclipse       |

## 📸 画面イメージ

**【申請者用画面】**
- ログイン画面

![login](images/login.png)

- （スマホ表示）

<img src="images/login-mobile.png" alt="login" width="400" />

- 申請一覧（マイページ）<br> 

※初期画面には表示した年に申請した申請一覧が表示されます。

![mypage](images/mypage.png)

- （スマホ表示）

<img src="images/mypage-mobile.png" alt="mypage" width="400" />

- 申請詳細画面<br>

※各業務経費の「追加」「コピー（チェックボックスを入れた内容をコピーしてモーダル画面表示）」ボタンで入力ができます。<br>
※チェックボックスで選択→「削除」ボタンで削除したい行の削除ができます。
※領収書の画像部分はクリックすると別タブで拡大表示されます。

![detail](images/detail-1.png)
![detail](images/detail-2.png)

- （スマホ表示）

<img src="images/detail-mobile-1.png" alt="detail" width="400" />
<img src="images/detail-mobile-2.png" alt="detail" width="400" />

- 新規申請フォーム  

※空欄のまま「追加」ボタンを押すとエラーメッセージが返ってきます

![modal](images/detail-3.png)

- （スマホ表示）

<img src="images/detail-mobile-3.png" alt="modal" width="400" />

- 領収書画像アップロード機能

![modal](images/detail-5.png)

- （スマホ表示）

<img src="images/detail-mobile-5.png" alt="detail" width="400" />


**【管理者用画面】**
- ログイン画面

![login](images/admin-login.png)

- （スマホ表示）

<img src="images/admin-mobile-login.png" alt="login" width="400" />

- 申請一覧（マイページ）

※初期画面はステータス「申請中」の申請一覧が表示されます。

![mypage](images/admin-mypage-2.png)

- （スマホ表示）

<img src="images/admin-moblie-mypage-2.png" alt="detail" width="400" />

- 申請詳細画面

![detail](images/admin-detail-1.png)
![detail](images/admin-detail-2.png)

- （スマホ表示）

<img src="images/admin-mobile-detail-1.png" alt="detail" width="400" />
<img src="images/admin-mobile-detail-2.png" alt="detail" width="400" />

- 申請内容の承認・却下機能

※承認：支払日を入力すると承認が完了します。

![detail](images/admin-detail-3.png)

- （スマホ表示）

<img src="images/admin-mobile-detail-3.png" alt="detail" width="400" />

※却下：却下理由を入力すると却下が完了します。

![detail](images/admin-detail-4.png)

- （スマホ表示）

<img src="images/admin-mobile-detail-4.png" alt="detail" width="400" />


## 🔗 GitHubリポジトリ

https://github.com/ishikawa-dev/shinsei-app

## 🚀 公開URL（Render）

https://shinsei-app-2.onrender.com/login<br>
※無料枠使用のため初回読み込みに時間がかかることがあります

【通常ログイン（申請者用）】<br>
ログインID：19970914<br>
パスワード：nnnn1111<br>

【管理者用ログイン】<br>
ログインID：admin<br>
パスワード：admin<br>
