CREATE TABLE T_USERS (
    user_id NVARCHAR(50) PRIMARY KEY,         -- ユーザーID
    password NVARCHAR(255) NOT NULL,  -- パスワード
    name NVARCHAR(100) NOT NULL      -- 氏名
);

CREATE TABLE T_HEADER (
   expense_id INT IDENTITY(1,1) PRIMARY KEY,         -- 申請ID
    user_id NVARCHAR(50),                     -- ユーザーID (外部キー)
   expense_month NVARCHAR(100),             -- 申請年月
    created_at DATE NOT NULL,           -- 入力日
    submission_date DATE NOT NULL,           -- 申請日
    payment_date DATE NOT NULL,           -- 支払日
    approval_status INT,   -- 承認ステータス (例: 0=未申請, 1=却下, 2=承認済, 3=申請中)
   　expense_flag INT,   -- 申請フラグ (例: 0=未申請, 1=申請済み)
   
    -- 外部キー制約 (userId)
    CONSTRAINT FK_HEADER_USER FOREIGN KEY (user_id)
    REFERENCES T_USERS(user_id)      -- T_USER テーブルの userId を参照
);

CREATE TABLE T_DETAILS (
   detail_id INT IDENTITY(1,1) PRIMARY KEY,         -- 明細ID
    expense_id INT,                     -- 申請ID (外部キー)
   usage_date Date,             -- 利用日付 (YYYYMMDD)
    valid_from DATE,           -- 定期適用開始日
    valid_until DATE,           -- 定期適用終了日
   transportation_facilities NVARCHAR(100),            -- 利用交通機関
   route NVARCHAR(200),            -- 利用区間
   round_trip_flag INT,            -- 往復か片道か(例: 1=片道, 2=往復)
   other_expense_item NVARCHAR(200),            -- その他項目名
   amount DECIMAL(10, 2) NOT NULL,           -- 利用金額
    purpose NVARCHAR(500) NOT NULL,           -- 利用目的
    receipt NVARCHAR(100),           -- 領収書
    receipt_uploaded_flag INT,   -- 領収書確認フラグ (例: 0=なし, 1=あり)
    display_flag INT,   -- 領収書確認フラグ (例: 0=未表示, 1=表示済)
    expense_type INT,
    save_flag INT,

    -- 外部キー制約 (userId)
    CONSTRAINT FK_DETAILS_HEADER_USER FOREIGN KEY (expense_id)
    REFERENCES T_HEADER(expense_id)      -- T_HEADER テーブルの expenseId を参照
);
