-- Chỉ số nhân vật: chỉ lưu config và điểm đã cộng, chỉ số cuối được tính khi cần:
--   chỉ số = base_value + min(points, max_points) * per_point
-- Sửa config ở stat_config là áp dụng cho mọi nhân vật (sau khi restart character-service).
--
-- Chạy: psql -h localhost -U postgres -d treasure_hunter -f db/2026-11-05_stat_config.sql
-- Sau đó gen lại jOOQ: mvnw generate-sources

BEGIN;

-- Bảng cũ lưu sẵn chỉ số của từng nhân vật, không còn dùng.
DROP TABLE IF EXISTS public.character_stats;

-- Config chung: mỗi chỉ số một dòng. Thêm chỉ số mới = thêm một dòng.
CREATE TABLE public.stat_config (
    stat_key    varchar(50)      PRIMARY KEY,                -- tên dùng trong code, ví dụ max_hp
    name        varchar(100)     NOT NULL,                   -- tên hiển thị
    base_value  double precision NOT NULL DEFAULT 0,         -- giá trị khi chưa cộng điểm
    per_point   double precision NOT NULL DEFAULT 0,         -- mỗi điểm cộng thêm bao nhiêu
    allocatable boolean          NOT NULL DEFAULT true,      -- người chơi có được cộng điểm vào không
    max_points  integer,                                     -- tối đa bao nhiêu điểm, NULL = không giới hạn
    sort_order  integer          NOT NULL DEFAULT 0,         -- thứ tự hiển thị
    CONSTRAINT chk_stat_config_values CHECK (base_value >= 0 AND per_point >= 0),
    CONSTRAINT chk_stat_config_max_points CHECK (max_points IS NULL OR max_points >= 0)
);

-- Điểm từng nhân vật đã cộng. Không có dòng = 0 điểm.
CREATE TABLE public.character_stat_point (
    character_id uuid        NOT NULL REFERENCES public."character"(id) ON DELETE CASCADE,
    stat_key     varchar(50) NOT NULL REFERENCES public.stat_config(stat_key) ON UPDATE CASCADE,
    points       integer     NOT NULL,
    PRIMARY KEY (character_id, stat_key),
    CONSTRAINT chk_character_stat_point_points CHECK (points >= 0)
);

INSERT INTO public.stat_config (stat_key, name, base_value, per_point, allocatable, max_points, sort_order) VALUES
    ('max_hp',       'Máu',              100,  10,   true,  NULL, 1),
    ('max_mp',       'Mana',              50,   8,   true,  NULL, 2),
    ('max_stamina',  'Thể lực',          100,   5,   true,  NULL, 3),
    ('atk',          'Tấn công',          10,   2,   true,  NULL, 4),
    ('physical_def', 'Phòng thủ vật lý',   5,   1,   true,  NULL, 5),
    ('magic_def',    'Phòng thủ phép',     5,   1,   true,  NULL, 6),
    ('crit_rate',    'Chí mạng (%)',       5,   0.2, true,  100,  7),   -- 100 điểm: 5 + 20 = 25%
    ('speed',        'Tốc độ (pixel/giây)', 160, 0,  false, 0,    8);   -- không cho cộng điểm

COMMIT;
