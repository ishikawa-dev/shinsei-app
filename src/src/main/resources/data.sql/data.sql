INSERT INTO T_USERS
VALUES('19970914','0914','石川奈緒子');

INSERT INTO T_HEADER(user_id,expense_month,created_at,submission_date,payment_date,approval_status,expense_flag)
VALUES('19970914','2025年4月分','2025/04/15','2025/04/30','2025/05/01',0,0);

INSERT INTO T_DETAILS(expense_id,valid_from,valid_until,transportation_facilities,route,amount,porpose)
VALUES('1','2025/04/01','2025/04/30','大阪メトロ四ツ橋線','肥後橋～住之江公園',11030,'自宅～事務所への通勤のため');