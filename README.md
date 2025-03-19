
# **MyNamz - デジタル名刺サービスポートフォリオ**

## **1. プロジェクト概要**

### 📌 **プロジェクト紹介**
MyNamzは、モバイル環境で簡単に作成、管理、共有できるデジタル名刺サービスです。  
紙の名刺の不便さを解消し、QRコードを活用した共有機能により、効率的なネットワーキングを提供します。

### 🎯 **プロジェクトの目的**
- 紙の名刺は管理が難しく、紛失する可能性が高い。  
- AI機能をデジタル名刺に追加し、価値を向上させる。  
- QRコードを利用した名刺共有機能により、便利なネットワーキング環境を構築する。  

---

## **2. 開発プロセス**

### 🏗 **開発期間およびチーム構成**
- **開発期間**: 2024年11月6日 ~ 12月2日  
- **チーム構成**:
  - **PM**: ドンゴン（プロジェクト企画、スケジュール管理）  
  - **Frontend & UI/UX**: ソンジェ、ジヌ（UI/UXデザイン）  
  - **Backend**: スイン（Spring Boot API開発、DB設計）  
  - **AI**: イェリョン（OCR、TextRankの研究・実装）  

### ⚙ **使用技術**
| 分野       | 技術スタック                                              |
| -------- | --------------------------------------------------- |
| Backend  | Spring Boot, Thymeleaf, Spring Security             |
| Database | MySQL, JPA (Hibernate)                              |
| AI       | OCR, TextRank (未実装)                                 |
| DevOps   | AWS (EC2, RDS), Let's Encrypt (SSL/TLS) |

---

## **3. 主な機能の詳細**

以下はMyNamzの主要な機能と、その対応するUIスクリーンショットです。

### 📌 **ユーザー機能**
- **メイン画面**  
  ![メイン画面](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_main.png?raw=true)  
  → アプリのホーム画面。登録済みの名刺が一覧表示されます。

- **ログインページ**  
  ![ログインページ](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_login.png?raw=true)  
  → ユーザーはメールアドレスとパスワードでログインできます。

- **新規登録ページ**  
  ![新規登録](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_first.png?raw=true)  
  → 新規ユーザーは名前、メールアドレス、パスワードを入力してアカウントを作成できます。

### 📌 **名刺管理機能**
- **名刺リストの表示 & 検索**  
  ![名刺リスト](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_list.JPG?raw=true)  
  → 自分の名刺や他のユーザーの名刺を一覧で表示・検索できます。

- **名刺の編集/削除**  
  ![編集・削除](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_main3.png?raw=true)  
  → 既存の名刺情報を編集・削除する機能。

### 📌 **QRコード機能**
- **QRコードの生成 & 共有**  
  ![QRコード](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_qr.png?raw=true)  
  → QRコードを生成し、スキャンした相手が名刺情報を取得可能。

- **QRコードスキャン時の名刺表示**  
  ![QRコード読み取り](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_qrpage.png?raw=true)  
  → QRコードをスキャンすると、ユーザーの名刺情報が表示されます。

---

## **4. システムアーキテクチャ**

### 📌 **全体構成**
このサービスはAWS上でホストされ、以下のコンポーネントで構成されています。

1. **フロントエンド (React)**  
   - ユーザーが名刺を作成・編集・共有  
   - バックエンドAPIと通信  
2. **バックエンド (Spring Boot)**  
   - ユーザー認証、名刺管理、QRコード生成を担当  
3. **データベース (AWS RDS - MySQL)**  
   - ユーザーデータ、名刺情報、QRコード情報を保存  

![システム構成](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_sys.png?raw=true)  

### 📌 **データベース設計 (ERD)**
各テーブルの関係性を示すERD（エンティティ・リレーションシップ・ダイアグラム）。

![ERD](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz-erd.png?raw=true)  

---
## **5. 結論 & 学んだこと**

### 🎯 **学んだこと**
このプロジェクトを通じて、以下の重要なポイントを学びました。  

- **サービス設計の重要性**: 設計を明確にすることで、後の修正作業を最小限に抑えられる  
- **例外処理とユーザー体験**: 実際の使用環境でのテストが不可欠  
- **セキュリティとHTTPSの適用**: ユーザーデータの保護と信頼性向上のために重要  
- **AWSのコスト管理**: 予想外の課金を防ぐためにリソース管理を徹底  
- **TDDの重要性**: 事前にテストを作成し、保守性を向上させるべき  

### 🚀 **今後の発展可能性**
現在、本プロジェクトは運用段階ですが、今後さらなる機能拡張の可能性もあります。  
例えば、以下のような追加開発を検討できます。  

- **OAuthログインの導入**  
- **AIを活用したネットワーキング分析機能**  
- **企業向けのダッシュボード機能**  
- **Nginxを利用したさらなる最適化**  

