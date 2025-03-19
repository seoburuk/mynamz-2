
# **MyNamz - デジタル名刺サービスポートフォリオ**

## **1. プロジェクト概要**

### 📌 **プロジェクト紹介**
MyNamzは、モバイル環境で簡単に作成、管理、共有できるデジタル名刺サービスです。  
紙の名刺の不便さを解消し、QRコードを活用した共有機能により、効率的なネットワーキングを提供します。

### 🎯 **プロジェクトの目的と背景**
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
  ![서비스메인](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_main.png?raw=true)  
- **ログインページ**  
  ![ログインページ](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_login.png?raw=true)  
- **ログイン後のメインページ**  
  ![ログイン後](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_main2.png?raw=true)  
- **新規登録ページ**  
  ![가입페이지](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_first.png?raw=true)  

### 📌 **名刺管理機能**
- **名刺リストの表示**  
  ![카드리스트](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_list.JPG?raw=true)  
- **名刺の編集/削除**  
  ![수정/삭제](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_main3.png?raw=true)  
- **名刺検索機能**  
  ![검색기능](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_search.png?raw=true)  
- **自分の名刺一覧**  
  ![내 명함 리스트](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_mine.png?raw=true)  

### 📌 **QRコード機能**
- **QRコードの生成**  
  ![qr코드](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_qr.png?raw=true)  
- **QRコードスキャン時の名刺表示**  
  ![qr코드 입장시 명함 제공페이지](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_qrpage.png?raw=true)  

### 📌 **その他の機能**
- **名刺の追加**  
  ![명함 추가](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_add.png?raw=true)  
- **パスワードリセット（Googleメール認証）**  
  ![비밀번호 찾기](https://github.com/seoburuk/mynamz-2/blob/main/image/email.png?raw=true)  

---

## **4. トラブルシューティングおよび学んだこと**

### 🛠 **解決した問題および学習ポイント**

#### 🔹 AWSドメイン購入による追加コストの問題解決
- **問題**: AWS Route 53を使用する場合、ドメイン購入が必要で追加費用が発生  
- **解決策**: DuckDNSを利用し、無料のサブドメインを適用しコスト削減  

#### 🔹 HTTPS未適用時のスマートフォンのセキュリティ問題
- **問題**: HTTPを使用すると、QRコードスキャン時にURLがブロックされる  
- **解決策**: Let's Encryptを使用し、SSL/TLSを適用してHTTPSをサポート  

#### 🔹 EC2セキュリティグループ設定およびデプロイ
- **問題**: セキュリティグループの設定ミスで接続問題が発生  
- **解決策**: インバウンド/アウトバウンドルールを適切に設定  

---

## **5. システムアーキテクチャ**

### 📌 **サービス全体構造**
以下はMyNamzの全体的なシステム構成図です。

![システムアーキテクチャ](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz_sys.png?raw=true)  

### 📌 **ERD (エンティティ・リレーションシップ・ダイアグラム)**
データベースのテーブル構成を示すERD（エンティティ・リレーションシップ・ダイアグラム）は以下の通りです。

![ERD](https://github.com/seoburuk/mynamz-2/blob/main/image/mynamz-erd.png?raw=true)  

---

## **6. 結論**

### 🎯 **プロジェクトを通じて学んだこと**
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
