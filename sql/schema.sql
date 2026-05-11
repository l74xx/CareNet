-- =============================================
-- 期末專題模板 — 資料庫初始化腳本
-- =============================================
-- 使用方式：
-- docker exec -I <Container Name>
-- psql -U <Postgres User> -h localhost -d <Postgres DB> -f sql/schema.sql
-- =============================================

DROP TABLE IF EXISTS operation_logs;
DROP TABLE IF EXISTS activity_participants;
DROP TABLE IF EXISTS activities;
DROP TABLE IF EXISTS health_records;
DROP TABLE IF EXISTS habit_records;
DROP TABLE IF EXISTS habits;
DROP TABLE IF EXISTS family_links;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       role VARCHAR(20) NOT NULL CHECK (role IN ('ELDER', 'FAMILY', 'ADMIN')),
                       phone VARCHAR(20),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE family_links (
                              id SERIAL PRIMARY KEY,
                              elder_id INT NOT NULL,
                              family_id INT NOT NULL,
                              relationship VARCHAR(50),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_family_elder
                                  FOREIGN KEY (elder_id) REFERENCES users(id) ON DELETE CASCADE,

                              CONSTRAINT fk_family_member
                                  FOREIGN KEY (family_id) REFERENCES users(id) ON DELETE CASCADE,

                              CONSTRAINT unique_family_link
                                  UNIQUE (elder_id, family_id)
);

CREATE TABLE habits (
                        id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL,
                        title VARCHAR(100) NOT NULL,
                        habit_type VARCHAR(30) NOT NULL
                            CHECK (habit_type IN ('WATER', 'MEDICINE', 'WALK', 'SLEEP', 'REHAB')),
                        reminder_time TIME,
                        active BOOLEAN DEFAULT TRUE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_habit_user
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE habit_records (
                               id SERIAL PRIMARY KEY,
                               habit_id INT NOT NULL,
                               record_date DATE NOT NULL DEFAULT CURRENT_DATE,
                               status VARCHAR(20) NOT NULL
                                   CHECK (status IN ('PENDING', 'COMPLETED', 'MISSED')),
                               note TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_habit_record_habit
                                   FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE,

                               CONSTRAINT unique_habit_record_per_day
                                   UNIQUE (habit_id, record_date)
);

CREATE TABLE health_records (
                                id SERIAL PRIMARY KEY,
                                user_id INT NOT NULL,
                                record_type VARCHAR(30) NOT NULL
                                    CHECK (record_type IN ('BLOOD_PRESSURE', 'BLOOD_SUGAR')),

                                systolic INT,
                                diastolic INT,
                                blood_sugar DECIMAL(5,2),

                                note TEXT,
                                recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_health_user
                                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

                                CONSTRAINT chk_health_record_value
                                    CHECK (
                                        (
                                            record_type = 'BLOOD_PRESSURE'
                                                AND systolic IS NOT NULL
                                                AND diastolic IS NOT NULL
                                                AND blood_sugar IS NULL
                                            )
                                            OR
                                        (
                                            record_type = 'BLOOD_SUGAR'
                                                AND blood_sugar IS NOT NULL
                                                AND systolic IS NULL
                                                AND diastolic IS NULL
                                            )
                                        )
);

CREATE TABLE activities (
                            id SERIAL PRIMARY KEY,
                            title VARCHAR(100) NOT NULL,
                            description TEXT,
                            location VARCHAR(100),
                            activity_time TIMESTAMP NOT NULL,
                            max_participants INT DEFAULT 20,
                            status VARCHAR(20) NOT NULL DEFAULT 'OPEN'
                                CHECK (status IN ('OPEN', 'FULL', 'CANCELLED')),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE activity_participants (
                                       id SERIAL PRIMARY KEY,
                                       activity_id INT NOT NULL,
                                       user_id INT NOT NULL,
                                       joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_participant_activity
                                           FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE,

                                       CONSTRAINT fk_participant_user
                                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

                                       CONSTRAINT unique_activity_user
                                           UNIQUE (activity_id, user_id)
);

CREATE TABLE operation_logs (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_operation_logs_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);





-- =========================
-- Users
-- =========================

INSERT INTO users
(username, password_hash, full_name, role, phone)
VALUES
    ('admin', 'admin', '系統管理員', 'ADMIN', '0900000000'),

    ('elder01', '1234', '王爺爺', 'ELDER', '0911111111'),

    ('elder02', '1234', '李奶奶', 'ELDER', '0922222222'),

    ('family01', '1234', '王小明', 'FAMILY', '0933333333'),

    ('family02', '1234', '李小華', 'FAMILY', '0944444444');



-- =========================
-- Family Links
-- =========================

INSERT INTO family_links
(elder_id, family_id, relationship)
VALUES
    (2, 4, '兒子'),
    (3, 5, '女兒');



-- =========================
-- Habits
-- =========================

INSERT INTO habits
(user_id, title, habit_type, reminder_time, active)
VALUES
    (2, '晨間散步', 'EXERCISE', '07:00', true),

    (2, '每日喝水 2000cc', 'HEALTH', '09:00', true),

    (3, '晚間伸展', 'EXERCISE', '20:00', true),

    (3, '飯後散步', 'HEALTH', '18:30', true);



-- =========================
-- Habit Records
-- =========================

INSERT INTO habit_records
(habit_id, record_date, status, note)
VALUES
    (1, CURRENT_DATE, 'COMPLETED', '今天散步30分鐘'),

    (2, CURRENT_DATE, 'PENDING', '尚未完成'),

    (3, CURRENT_DATE, 'COMPLETED', '完成伸展'),

    (4, CURRENT_DATE, 'COMPLETED', '散步20分鐘');



-- =========================
-- Health Records
-- =========================

INSERT INTO health_records
(user_id,
 record_type,
 systolic,
 diastolic,
 blood_sugar,
 note)
VALUES

    (2,
     'BLOOD_PRESSURE',
     120,
     80,
     NULL,
     '血壓正常'),

    (2,
     'BLOOD_SUGAR',
     NULL,
     NULL,
     98,
     '飯前血糖'),

    (3,
     'BLOOD_PRESSURE',
     145,
     95,
     NULL,
     '血壓偏高'),

    (3,
     'BLOOD_SUGAR',
     NULL,
     NULL,
     126,
     '飯後血糖偏高');



-- =========================
-- Activities
-- =========================

INSERT INTO activities
(title,
 description,
 location,
 activity_time,
 max_participants,
 status)
VALUES

    ('銀髮晨間瑜珈',
     '適合長者的舒緩瑜珈活動',
     '社區活動中心',
     NOW() + INTERVAL '1 day',
     20,
     'OPEN'),

    ('健康講座',
     '高血壓與血糖控制講座',
     '第一會議室',
     NOW() + INTERVAL '2 day',
     30,
     'OPEN'),

    ('公園健走',
     '銀髮戶外健走活動',
     '中央公園',
     NOW() + INTERVAL '3 day',
     15,
     'OPEN');



-- =========================
-- Activity Participants
-- =========================

INSERT INTO activity_participants
(activity_id, user_id)
VALUES
    (1, 2),
    (2, 2),
    (1, 3),
    (3, 3);



-- =========================
-- Operation Logs
-- =========================
-- Audit Log 不建議放大量 seed
-- 保留少量測試資料即可

INSERT INTO operation_logs
(user_id,
 action_type,
 target_type,
 target_id)
VALUES

    (2, 'LOGIN', 'USER', 2),

    (2, 'CREATE', 'HABIT', 1),

    (2, 'CHECKIN', 'HABIT', 1),

    (3, 'JOIN', 'ACTIVITY', 3),

    (1, 'CREATE', 'ACTIVITY', 2);